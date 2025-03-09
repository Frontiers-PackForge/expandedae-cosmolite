package lu.kolja.expandedae.item;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import appeng.items.materials.UpgradeCardItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemAutoCompleteCard extends UpgradeCardItem {
    public ItemAutoCompleteCard(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag advancedTooltips) {
        tooltip.add(Component.translatable("item.expandedae.auto_complete_card.tooltip").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, level, tooltip, advancedTooltips);
    }
}
