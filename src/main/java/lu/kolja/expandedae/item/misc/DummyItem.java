package lu.kolja.expandedae.item.misc;

import appeng.items.AEBaseItem;
import lu.kolja.expandedae.xmod.XMod;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class DummyItem extends AEBaseItem {
    private final XMod.Mods addon;

    public DummyItem(Item.Properties properties, XMod.Mods addon) {
        super(properties);
        this.addon = addon;
    }

    @ParametersAreNonnullByDefault
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {
        lines.add(addon.getUnavailableTooltip());
    }

    @Override
    public void addToMainCreativeTab(CreativeModeTab.Output output) {}
}
