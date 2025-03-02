package lu.kolja.expandedae.common;

import appeng.api.parts.PartModels;
import appeng.block.AEBaseBlockItem;
import appeng.block.AEBaseEntityBlock;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.blockentity.ClientTickingBlockEntity;
import appeng.blockentity.ServerTickingBlockEntity;
import appeng.core.AppEng;
import appeng.items.AEBaseItem;
import com.glodblock.github.glodium.registry.RegistryHandler;
import com.glodblock.github.glodium.util.GlodUtil;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.common.container.ContainerExpPatternProvider;
import lu.kolja.expandedae.common.parts.PartExpPatternProvider;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;

public class EAERegistryHandler extends RegistryHandler {

    public static final EAERegistryHandler INSTANCE = new EAERegistryHandler();

    public EAERegistryHandler() {
        super(Expandedae.MODID);
    }

    public <T extends AEBaseBlockEntity> void block(String name, AEBaseEntityBlock<T> block, Class<T> clazz, BlockEntityType.BlockEntitySupplier<? extends T> supplier) {
        bindTileEntity(clazz, block, supplier);
        block(name, block, b -> new AEBaseBlockItem(b, new Item.Properties()));
        tile(name, block.getBlockEntityType());
    }

    @Override
    public void register(RegisterEvent event) {
        super.register(event);
        this.onRegisterContainer();
        this.onRegisterModels();
        this.onRegisterRecipe();
    }

    public Collection<Block> getBlocks() {
        return this.blocks.stream().map(Pair::getRight).toList();
    }

    private void onRegisterRecipe() {}

    private void onRegisterContainer() {
        ForgeRegistries.MENU_TYPES.register(AppEng.makeId("exp_pattern_provider"), ContainerExpPatternProvider.TYPE);
        /*if (ModList.get().isLoaded("ae2wtlib")) {
            WTCommonLoad.container();
        }
        if (ModList.get().isLoaded("appliede")) {
            APECommonLoad.container();
        }*/
    }

    private <T extends AEBaseBlockEntity> void bindTileEntity(Class<T> clazz, AEBaseEntityBlock<T> block, BlockEntityType.BlockEntitySupplier<? extends T> supplier) {
        BlockEntityTicker<T> serverTicker = null;
        if (ServerTickingBlockEntity.class.isAssignableFrom(clazz)) {
            serverTicker = (level, pos, state, entity) -> ((ServerTickingBlockEntity) entity).serverTick();
        }
        BlockEntityTicker<T> clientTicker = null;
        if (ClientTickingBlockEntity.class.isAssignableFrom(clazz)) {
            clientTicker = (level, pos, state, entity) -> ((ClientTickingBlockEntity) entity).clientTick();
        }
        block.setBlockEntity(clazz, GlodUtil.getTileType(clazz, supplier, block), clientTicker, serverTicker);
    }

    public void onInit() {
        for (Pair<String, Block> entry : blocks) {
            Block block = ForgeRegistries.BLOCKS.getValue(Expandedae.id(entry.getKey()));
            if (block instanceof AEBaseEntityBlock<?>) {
                AEBaseBlockEntity.registerBlockEntityItem(
                        ((AEBaseEntityBlock<?>) block).getBlockEntityType(),
                        block.asItem()
                );
            }
        }
        this.registerAEUpgrade();
        this.registerStorageHandler();
        /*if (ModList.get().isLoaded("ae2wtlib")) {
            WTCommonLoad.init();
        }
        if (ModList.get().isLoaded("appflux")) {
            AFCommonLoad.init();
        }
        if (ModList.get().isLoaded("appliede")) {
            APECommonLoad.init();
        }*/
    }

    private void registerAEUpgrade() {}

    private void registerStorageHandler() {}

    private void onRegisterModels() {
        PartModels.registerModels(PartExpPatternProvider.MODELS);
    }

    //private void initPackageList() {EAEConfig.tapeWhitelist.forEach(ItemMEPackingTape::registerPackableDevice);}

    public void registerTab(Registry<CreativeModeTab> registry) {
        var tab = CreativeModeTab.builder()
                .icon(() -> new ItemStack(EAEItemAndBlock.EXP_PATTERN_PROVIDER))
                .title(Component.translatable("itemGroup.eae"))
                .displayItems((__, o) -> {
                    for (Pair<String, Item> entry : items) {
                        if (entry.getRight() instanceof AEBaseItem aeItem) {
                            aeItem.addToMainCreativeTab(o);
                        } else {
                            o.accept(entry.getRight());
                        }
                    }
                    for (Pair<String, Block> entry : blocks) {
                        o.accept(entry.getRight());
                    }
                })
                .build();
        Registry.register(registry, Expandedae.id("tab_main"), tab);
    }
}
