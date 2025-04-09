package lu.kolja.expandedae.helper;

import java.util.List;

import net.minecraft.world.item.ItemStack;

public interface IMenuTooltipHandler {

    default List<String> handleItemTooltip(final ItemStack stack, final int mouseX, final int mouseY,
                                           final List<String> currentToolTip) {
        return currentToolTip;
    }

    default ItemStack getHoveredStack() {
        return null;
    }
}