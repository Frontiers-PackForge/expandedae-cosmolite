package lu.kolja.expandedae.item.misc;

import appeng.block.crafting.CraftingBlockItem;
import appeng.core.AEConfig;
import appeng.util.InteractionUtil;
import lu.kolja.expandedae.definition.ExpBlocks;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class ExpCPUItem extends CraftingBlockItem {
    public ExpCPUItem(Block id, Properties props, Supplier<ItemLike> disassemblyExtra) {
        super(id, props, disassemblyExtra);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (AEConfig.instance().isDisassemblyCraftingEnabled() && InteractionUtil.isInAlternateUseMode(player)) {
            int count = player.getItemInHand(hand).getCount();
            player.setItemInHand(hand, ItemStack.EMPTY);

            player.getInventory().placeItemBackInInventory(ExpBlocks.EXP_PATTERN_PROVIDER.stack(count));
            player.getInventory().placeItemBackInInventory(new ItemStack(disassemblyExtra.get(), count));
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
        }
        return super.use(level, player, hand);
    }
}
