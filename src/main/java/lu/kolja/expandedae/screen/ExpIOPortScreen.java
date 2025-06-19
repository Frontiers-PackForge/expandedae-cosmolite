package lu.kolja.expandedae.screen;

import appeng.api.config.FullnessMode;
import appeng.api.config.OperationMode;
import appeng.api.config.RedstoneMode;
import appeng.api.config.Settings;
import appeng.client.gui.implementations.UpgradeableScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.ServerSettingToggleButton;
import appeng.client.gui.widgets.SettingToggleButton;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.menu.implementations.IOPortMenu;
import lu.kolja.expandedae.menu.ExpIOPortMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ExpIOPortScreen extends UpgradeableScreen<ExpIOPortMenu> {
    private final SettingToggleButton<FullnessMode> fullMode;
    private final SettingToggleButton<OperationMode> operationMode;
    private final SettingToggleButton<RedstoneMode> redstoneMode;

    public ExpIOPortScreen(ExpIOPortMenu menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
        this.fullMode = new ServerSettingToggleButton<>(Settings.FULLNESS_MODE, FullnessMode.EMPTY);
        this.addToLeftToolbar(this.fullMode);
        this.redstoneMode = new ServerSettingToggleButton<>(Settings.REDSTONE_CONTROLLED, RedstoneMode.IGNORE);
        this.addToLeftToolbar(this.redstoneMode);
        this.operationMode = new ServerSettingToggleButton<>(Settings.OPERATION_MODE, OperationMode.EMPTY);
        this.widgets.add("operationMode", this.operationMode);
    }

    @Override
    protected void updateBeforeRender() {
        super.updateBeforeRender();
        this.redstoneMode.set(this.menu.getRedStoneMode());
        this.redstoneMode.setVisibility(this.menu.hasUpgrade(AEItems.REDSTONE_CARD));
        this.operationMode.set(this.menu.getOperationMode());
        this.fullMode.set(this.menu.getFullMode());
    }

    @Override
    public void drawBG(GuiGraphics guiGraphics, int offsetX, int offsetY, int mouseX, int mouseY, float partialTicks) {
        super.drawBG(guiGraphics, offsetX, offsetY, mouseX, mouseY, partialTicks);
        this.drawItem(guiGraphics, offsetX + 66 - 8, offsetY + 17, AEItems.ITEM_CELL_1K.stack());
        this.drawItem(guiGraphics, offsetX + 94 + 8, offsetY + 17, AEBlocks.DRIVE.stack());
    }
}
