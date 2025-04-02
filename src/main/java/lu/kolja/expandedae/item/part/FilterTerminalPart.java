package lu.kolja.expandedae.item.part;

import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.menu.FilterTermMenu;
import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import appeng.api.util.IConfigManager;
import appeng.api.util.IConfigurableObject;
import appeng.items.parts.PartModels;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import appeng.parts.PartModel;
import appeng.parts.reporting.AbstractDisplayPart;
import appeng.util.ConfigManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class FilterTerminalPart extends AbstractDisplayPart implements IConfigurableObject {
    @PartModels
    public static final ResourceLocation MODEL_OFF = Expandedae.makeId("part/filter_terminal_off");
    @PartModels
    public static final ResourceLocation MODEL_ON = Expandedae.makeId("part/filter_terminal_on");
    public static final IPartModel MODELS_OFF;
    public static final IPartModel MODELS_ON;
    public static final IPartModel MODELS_HAS_CHANNEL;
    private final ConfigManager configManager = new ConfigManager(() -> this.getHost().markForSave());

    public FilterTerminalPart(IPartItem<?> partItem) {
        super(partItem, true);
        //this.configManager.registerSetting();
    }

    public boolean onPartActivate(Player player, InteractionHand hand, Vec3 pos) {
        if (!super.onPartActivate(player, hand, pos) && !this.isClientSide()) {
            MenuOpener.open(FilterTermMenu.TYPE, player, MenuLocators.forPart(this));
        }
        return true;
    }

    public IPartModel getStaticModels() {
        return this.selectModel(MODELS_OFF, MODELS_ON, MODELS_HAS_CHANNEL);
    }

    public IConfigManager getConfigManager() {
        return this.configManager;
    }

    public void writeToNBT(CompoundTag tag) {
        super.writeToNBT(tag);
        this.configManager.writeToNBT(tag);
    }

    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);
        this.configManager.readFromNBT(tag);
    }

    static {
        MODELS_OFF = new PartModel(MODEL_BASE, MODEL_OFF, MODEL_STATUS_OFF);
        MODELS_ON = new PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_ON);
        MODELS_HAS_CHANNEL = new PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_HAS_CHANNEL);
    }
}
