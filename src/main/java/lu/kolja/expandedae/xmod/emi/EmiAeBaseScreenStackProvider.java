package lu.kolja.expandedae.xmod.emi;

import appeng.api.stacks.GenericStack;
import appeng.client.gui.AEBaseScreen;
import dev.emi.emi.api.EmiStackProvider;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.EmiStackInteraction;
import lu.kolja.expandedae.xmod.emi.converters.EmiStackConverters;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

class EmiAeBaseScreenStackProvider implements EmiStackProvider<Screen> {
    @Override
    public EmiStackInteraction getStackAt(Screen screen, int x, int y) {
        if (!(screen instanceof AEBaseScreen<?> aeScreen)) return EmiStackInteraction.EMPTY;
        var stack = aeScreen.getStackUnderMouse(x, y);
        if (stack == null) return EmiStackInteraction.EMPTY;
        var stackEMI = toEmiStack(stack.stack());
        if (stackEMI != null) return new EmiStackInteraction(stackEMI, null, false);
        return EmiStackInteraction.EMPTY;
    }

    @Nullable
    private EmiStack toEmiStack(GenericStack stack) {
        for (var converter : EmiStackConverters.getConverters()) {
            var emiStack = converter.toEmiStack(stack);
            if (emiStack != null) {
                return emiStack;
            }
        }
        return null;
    }
}
