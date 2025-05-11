package lu.kolja.expandedae.mixin.misc;

import lu.kolja.expandedae.helper.ISlot;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Slot.class)
public class MixinSlot implements ISlot {

    @Shadow @Final private int slot;

    @Override
    public int expandedae$getSlot() {
        return slot;
    }
}
