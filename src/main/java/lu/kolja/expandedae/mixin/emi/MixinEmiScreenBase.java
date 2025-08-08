package lu.kolja.expandedae.mixin.emi;

import appeng.menu.AEBaseMenu;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.mixin.accessor.HandledScreenAccessor;
import dev.emi.emi.screen.EmiScreenBase;
import dev.emi.emi.screen.RecipeScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = EmiScreenBase.class, remap = false)
public abstract class MixinEmiScreenBase {

    /**
     * This invoker gives me access to the private constructor
     */
    @Invoker("<init>")
    public static EmiScreenBase newEmiScreenBase(Screen screen, Bounds bounds) {
        throw new AssertionError("Mixin invoker body called");
    }

    /**
     * @author Kolja
     * @reason Render emi in the craft confirm menu
     */
    @Overwrite(remap = false)
    public static EmiScreenBase of(Screen screen) {
        if (screen instanceof AbstractContainerScreen hs) {
            HandledScreenAccessor hsa = (HandledScreenAccessor) hs;
            AbstractContainerMenu sh = hs.getMenu();
            if (!sh.slots.isEmpty() || sh instanceof AEBaseMenu) {
                int extra = 0;
                if (hs instanceof RecipeUpdateListener provider) {
                    if (provider.getRecipeBookComponent().isVisible()) {
                        extra = 177;
                    }
                }
                Bounds bounds = new Bounds(hsa.getX() - extra, hsa.getY(), hsa.getBackgroundWidth() + extra, hsa.getBackgroundHeight());
                return newEmiScreenBase(screen, bounds);
            }
        } else if (screen instanceof RecipeScreen rs) {
            return newEmiScreenBase(rs, rs.getBounds());
        }
        return newEmiScreenBase(null, Bounds.EMPTY);
    }
}
