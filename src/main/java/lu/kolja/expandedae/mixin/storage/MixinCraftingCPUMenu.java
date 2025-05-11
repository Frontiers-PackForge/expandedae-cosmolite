package lu.kolja.expandedae.mixin.storage;

import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.util.DimensionalBlockPos;
import appeng.blockentity.crafting.PatternProviderBlockEntity;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.me.service.CraftingService;
import appeng.menu.AEBaseMenu;
import appeng.menu.me.crafting.CraftingCPUMenu;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lu.kolja.expandedae.helper.ICPUAccessor;
import lu.kolja.expandedae.helper.IPatternProviderFinder;
import lu.kolja.expandedae.helper.Maths;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Mixin(value = CraftingCPUMenu.class, remap = false)
public class MixinCraftingCPUMenu extends AEBaseMenu implements IPatternProviderFinder, ICPUAccessor {
    @Shadow
    @Final
    private IGrid grid;

    @Unique
    private static final String ACTION_FIND_PATTERN_PROVIDER = "findPatternProvider";

    protected MixinCraftingCPUMenu(MenuType<?> menuType, int id, Inventory ip, Object te) {
        super(menuType, id, ip, te);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(MenuType<?> menuType, int id, Inventory ip, Object te, CallbackInfo ci) {
        //registerClientAction(ACTION_FIND_PATTERN_PROVIDER, GenericStack.class, this::findPatternProvider);
    }

    @Override
    public void findPatternProvider(GenericStack stack) {
        if (this.isClientSide()) {
            //this.sendClientAction(ACTION_FIND_PATTERN_PROVIDER, stack);
        }
        check(stack.what());
        System.out.println("DEBUG: findPatternProvider called with key: " + stack.what());
        try {
            //var grid0 = this.getGrid();
            //var grid1 = grid;
            //var grid2 = this.getActionSource().machine().get().getActionableNode().getGrid();
            var grid3 = this.getActionHost().getActionableNode().getGrid();
        } catch (NullPointerException ex) {
            System.out.println("DEBUG: NullPointerException" + Arrays.toString(ex.getStackTrace()));
            System.out.println(ex.getMessage());
            System.out.println(ex.getLocalizedMessage());
        }
        if (grid != null) {
            System.out.println("DEBUG: Grid is not null, proceeding");
            var patterns = grid.getCraftingService().getCraftingFor(stack.what());
            System.out.println("DEBUG: Found " + (patterns.isEmpty() ? "no" : patterns.size()) + " patterns");

            if (!patterns.isEmpty()) {
                var pattern = patterns.iterator().next();
                System.out.println("DEBUG: Using first pattern: " + pattern);

                Iterable<ICraftingProvider> providers = ((CraftingService)grid.getCraftingService()).getProviders(pattern);
                System.out.println("DEBUG: Got providers for pattern");

                boolean foundProvider = false;
                for (ICraftingProvider provider : providers) {
                    System.out.println("DEBUG: Checking provider: " + provider.getClass().getName());

                    if (provider instanceof PatternProviderLogicHost host) {
                        System.out.println("DEBUG: Found PatternProviderLogicHost");
                        var blockEntity = host.getBlockEntity();

                        if (blockEntity != null) {
                            System.out.println("DEBUG: Block entity is not null");
                            var pos = blockEntity.getBlockPos();
                            System.out.println("DEBUG: Sending message with position: " + pos);

                            getPlayerInventory().player.sendSystemMessage(
                                    Component.literal(String.format("Pattern provider at: %d, %d, %d",
                                            pos.getX(), pos.getY(), pos.getZ())));
                            System.out.println("DEBUG: Message sent to player");
                            foundProvider = true;
                            break;
                        } else {
                            System.out.println("DEBUG: Block entity is null");
                        }
                    }
                }

                if (!foundProvider) {
                    System.out.println("DEBUG: No pattern provider found");
                }
            } else {
                System.out.println("DEBUG: No patterns found for key");
            }
        } else {
            System.out.println("DEBUG: Grid is null");
        }
    }

    private CompoundTag expandedae$openNbtData(final ItemStack stack) {
        CompoundTag compound = stack.getTag();
        if (compound == null) stack.setTag(new CompoundTag());
        return compound;
    }

    private static final List<DimensionalBlockPos> highlightedBlocks = new ArrayList<>();
    private static long expireHighlightTime;
    private static final int MIN_TIME = 3000;
    private static final int MAX_TIME = MIN_TIME * 10;

    private ItemStack hoveredStack;

    @Unique
    private static void expandedae$highlight(Player player, DimensionalBlockPos pos) {
        expandedae$clear();
        BlockPos bPos = pos.getPos();
        int highlightDuration = MIN_TIME;
        if (player.level().dimension() == pos.getLevel().dimension()) {
            player.sendSystemMessage(Component.literal(String.format("Found pattern at %d %d %d", bPos.getX(), bPos.getY(), bPos.getZ())));
        } else {
            player.sendSystemMessage(Component.literal("Pattern not found in this dimension"));
        }
        highlightedBlocks.add(pos);
        highlightDuration = Math.max(
                highlightDuration,
                Maths.clamp_int((int) (500 * Maths.getTaxicabDistance(pos.getPos(), player.blockPosition())),
                        MIN_TIME,
                        MAX_TIME
                ));
        expireHighlightTime = System.currentTimeMillis() + highlightDuration;
    }

    @Unique
    @SubscribeEvent
    public void expandedae$onRenderLevelStage(RenderLevelStageEvent event) {
        if (highlightedBlocks.isEmpty()) return;
        long time = System.currentTimeMillis();
        if (time > expireHighlightTime) {
            expandedae$clear();
            return;
        }
        if (((time / 500) & 1) == 0) return;
        Minecraft mc = Minecraft.getInstance();
        ResourceKey<Level> dimension = mc.level.dimension();

        Player localPlayer = mc.player;
        double doubleX = localPlayer.xOld + (localPlayer.getX() - localPlayer.xOld) * event.getPartialTick();
        double doubleY = localPlayer.yOld + (localPlayer.getY() - localPlayer.yOld) * event.getPartialTick();
        double doubleZ = localPlayer.zOld + (localPlayer.getZ() - localPlayer.zOld) * event.getPartialTick();

        for (var pos : highlightedBlocks) {
            if (dimension != pos.getLevel().dimension()) continue;
            GL11.glPushMatrix();
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glLineWidth(3);
            GL11.glTranslated(-doubleX, -doubleY, -doubleZ);

            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
            expandedae$renderHighLightedBlocksOutline(event.getPoseStack(), bufferSource, doubleX, doubleY, doubleZ);

            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
    }

    @Unique
    private static void expandedae$clear() {
        highlightedBlocks.clear();
        expireHighlightTime = -1;
    }

    @Unique
    private static void expandedae$renderHighLightedBlocksOutline(PoseStack poseStack, MultiBufferSource bufferSource, double x, double y, double z) {
        // Get the buffer
        VertexConsumer builder = bufferSource.getBuffer(RenderType.lines());

        // Create a matrix for transformations
        Matrix4f matrix = poseStack.last().pose();

        // Red color (RGBA)
        float red = 1.0f;
        float green = 0.0f;
        float blue = 0.0f;
        float alpha = 1.0f;

        // Draw first outline (x edge)
        builder.vertex(matrix, (float)x, (float)y, (float)z).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, (float)x, (float)(y + 1), (float)z).color(red, green, blue, alpha).endVertex();

        builder.vertex(matrix, (float)x, (float)(y + 1), (float)z).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, (float)x, (float)(y + 1), (float)(z + 1)).color(red, green, blue, alpha).endVertex();

        builder.vertex(matrix, (float)x, (float)(y + 1), (float)(z + 1)).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, (float)x, (float)y, (float)(z + 1)).color(red, green, blue, alpha).endVertex();

        builder.vertex(matrix, (float)x, (float)y, (float)(z + 1)).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, (float)x, (float)y, (float)z).color(red, green, blue, alpha).endVertex();

        // Draw second outline (x+1 edge)
        builder.vertex(matrix, (float)(x + 1), (float)y, (float)z).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, (float)(x + 1), (float)(y + 1), (float)z).color(red, green, blue, alpha).endVertex();

        builder.vertex(matrix, (float)(x + 1), (float)(y + 1), (float)z).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, (float)(x + 1), (float)(y + 1), (float)(z + 1)).color(red, green, blue, alpha).endVertex();

        builder.vertex(matrix, (float)(x + 1), (float)(y + 1), (float)(z + 1)).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, (float)(x + 1), (float)y, (float)(z + 1)).color(red, green, blue, alpha).endVertex();

        builder.vertex(matrix, (float)(x + 1), (float)y, (float)(z + 1)).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, (float)(x + 1), (float)y, (float)z).color(red, green, blue, alpha).endVertex();

        // Connect the two outlines
        builder.vertex(matrix, (float)x, (float)y, (float)z).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, (float)(x + 1), (float)y, (float)z).color(red, green, blue, alpha).endVertex();

        builder.vertex(matrix, (float)(x + 1), (float)y, (float)(z + 1)).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, (float)x, (float)y, (float)(z + 1)).color(red, green, blue, alpha).endVertex();

        builder.vertex(matrix, (float)x, (float)(y + 1), (float)(z + 1)).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, (float)(x + 1), (float)(y + 1), (float)(z + 1)).color(red, green, blue, alpha).endVertex();

        builder.vertex(matrix, (float)(x + 1), (float)(y + 1), (float)z).color(red, green, blue, alpha).endVertex();
        builder.vertex(matrix, (float)x, (float)(y + 1), (float)z).color(red, green, blue, alpha).endVertex();
    }

    @Override
    public IGrid getGrid() {
        return this.grid;
    }
    private void check(AEKey hoveredStack) {
        if (grid == null) return;
        for (var machine : grid.getMachines(PatternProviderBlockEntity.class)) {
            BlockPos pos = machine.getBlockPos();

            for (var pattern : machine.getLogic().getAvailablePatterns()) {
                if (Arrays.stream(pattern.getOutputs()).anyMatch(output -> output.what().equals(hoveredStack))) {
                    // Found a match! Send coordinates to player
                    this.getActionSource().player().get().sendSystemMessage(Component.literal(
                            String.format("§aPattern Provider found at: §e%d, %d, %d",
                                    pos.getX(), pos.getY(), pos.getZ())));
                    return;
                }
            }
        }
    }
}
