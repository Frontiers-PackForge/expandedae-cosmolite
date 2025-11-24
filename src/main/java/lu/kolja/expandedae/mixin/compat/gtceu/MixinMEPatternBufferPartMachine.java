package lu.kolja.expandedae.mixin.compat.gtceu;

import appeng.api.inventories.InternalInventory;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.helpers.patternprovider.PatternContainer;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.ButtonConfigurator;
import com.gregtechceu.gtceu.api.machine.feature.IDataStickInteractable;
import com.gregtechceu.gtceu.integration.ae2.machine.MEBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEPatternBufferPartMachine;
import com.llamalad7.mixinextras.sugar.Local;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import lu.kolja.expandedae.definition.ExpLang;
import lu.kolja.expandedae.helper.misc.KeybindUtil;
import lu.kolja.expandedae.helper.misc.PatternHelper;
import lu.kolja.expandedae.xmod.gtceu.ExpGtceu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = MEPatternBufferPartMachine.class, remap = false)
public abstract class MixinMEPatternBufferPartMachine extends MEBusPartMachine implements ICraftingProvider, PatternContainer, IDataStickInteractable {

    public MixinMEPatternBufferPartMachine(IMachineBlockEntity holder, IO io, Object... args) {
        super(holder, io, args);
    }

    @Shadow @Final private InternalInventory internalPatternInventory;

    @Inject(
            method = "attachConfigurators",
            at = @At("TAIL")
    )
    private void attachConfigurators(CallbackInfo ci, @Local(argsOnly = true) ConfiguratorPanel configuratorPanel) {
        configuratorPanel.attachConfigurators(new ButtonConfigurator(
                new GuiTextureGroup(GuiTextures.BUTTON, ExpGtceu.MULTIPLY_OVERLAY),
                c -> {
                    for (int i = 0; i < this.internalPatternInventory.size(); i++) {
                        var currentStack = this.internalPatternInventory.getStackInSlot(i);
                        if (currentStack.isEmpty()) continue;
                        var newStack = PatternHelper.modifyPatterns(
                                currentStack,
                                (c.isShiftClick ? KeybindUtil.SHIFT_MULTIPLIER : 1)
                                        * (c.isCtrlClick ? KeybindUtil.CTRL_MULTIPLIER : 1)
                                        * (c.button == 1 ? -1 : 1),
                                this.getLevel());
                        this.internalPatternInventory.setItemDirect(i, newStack);
                    }
                })
                .setTooltips(List.of(
                        ExpLang.GUI_TOOLTIPS_MODIFY_PATTERNS_GT.text(),
                        ExpLang.GUI_TOOLTIPS_MODIFY_PATTERNS_HINT_GT.text()
                ))
        );
    }
}
