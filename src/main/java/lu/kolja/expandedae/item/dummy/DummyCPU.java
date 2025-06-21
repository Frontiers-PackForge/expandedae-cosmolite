package lu.kolja.expandedae.item.dummy;

import appeng.block.AEBaseBlockItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class DummyCPU extends AEBaseBlockItem {
    public DummyCPU(Block block, Properties properties) {
        super(block ,properties);
    }

    @Override
    public void addCheckedInformation(ItemStack itemStack, Level level, List<Component> toolTip, TooltipFlag advancedTooltips) {
        toolTip.add(Component.literal("Item disabled via config").withStyle(ChatFormatting.DARK_RED));
    }
}
