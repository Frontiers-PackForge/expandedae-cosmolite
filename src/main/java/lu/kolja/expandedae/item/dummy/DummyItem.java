package lu.kolja.expandedae.item.dummy;

import appeng.items.AEBaseItem;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import lu.kolja.expandedae.enums.Addons;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class DummyItem extends AEBaseItem {
    private final Addons addon;

    public DummyItem(Item.Properties properties, Addons addon) {
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
