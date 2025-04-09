package lu.kolja.expandedae.mixin.storage;

import org.spongepowered.asm.mixin.Mixin;
import appeng.api.networking.IGridService;
import appeng.parts.storagebus.StorageBusPart;

@Mixin(value = StorageBusPart.class, remap = false)
public class MixinStorageBus implements IGridService {
}
