package lu.kolja.expandedae.item.misc;

import appeng.block.crafting.CraftingBlockItem;
import appeng.core.AEConfig;
import appeng.core.definitions.AEItems;
import appeng.util.InteractionUtil;
import lu.kolja.expandedae.definition.ExpBlocks;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ExpCPUItem extends CraftingBlockItem {
    private final Block id;

    public ExpCPUItem(Block id, Properties props, Supplier<ItemLike> disassemblyExtra) {
        super(id, props, disassemblyExtra);
        this.id = id;
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (AEConfig.instance().isDisassemblyCraftingEnabled() && InteractionUtil.isInAlternateUseMode(player)) {
            int count = player.getItemInHand(hand).getCount();
            var extra = disassemblyExtra.get();
            player.setItemInHand(hand, ItemStack.EMPTY);

            if (id == ExpBlocks.CPU_2.block()) {
                player.getInventory().placeItemBackInInventory(ExpBlocks.EXP_CRAFTING_UNIT.stack(count));
                player.getInventory().placeItemBackInInventory(AEItems.ENGINEERING_PROCESSOR.stack(count));
            } else {
                player.getInventory().placeItemBackInInventory(new ItemStack(extra).copyWithCount(count * 2));
            }

            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
        }
        return super.use(level, player, hand);
    }
}
