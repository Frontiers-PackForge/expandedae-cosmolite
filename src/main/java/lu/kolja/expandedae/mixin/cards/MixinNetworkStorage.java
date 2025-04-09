package lu.kolja.expandedae.mixin.cards;

import org.spongepowered.asm.mixin.Mixin;
import appeng.me.storage.NetworkStorage;

@Mixin(value = NetworkStorage.class, remap = false)
public class MixinNetworkStorage {

}
