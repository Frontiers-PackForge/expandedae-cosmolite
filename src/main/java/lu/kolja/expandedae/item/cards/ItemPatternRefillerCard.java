package lu.kolja.expandedae.item.cards;

import java.util.List;

import appeng.items.materials.UpgradeCardItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemPatternRefillerCard extends UpgradeCardItem {
    public ItemPatternRefillerCard(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag advancedTooltips) {
        tooltip.add(Component.translatable("item.expandedae.pattern_refiller_card.tooltip.1").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, level, tooltip, advancedTooltips);
    }
}
