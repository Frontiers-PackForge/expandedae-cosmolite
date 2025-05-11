package lu.kolja.expandedae.helper;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IMenuTooltipHandler {

    default List<String> handleItemTooltip(final ItemStack stack, final int mouseX, final int mouseY,
                                           final List<String> currentToolTip) {
        return currentToolTip;
    }

    default ItemStack getHoveredStack() {
        return null;
    }
}