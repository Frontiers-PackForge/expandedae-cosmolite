package lu.kolja.expandedae.helper;

import appeng.api.behaviors.ExternalStorageStrategy;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyType;
import appeng.api.storage.MEStorage;
import appeng.capabilities.Capabilities;
import appeng.me.storage.CompositeStorage;
import appeng.parts.automation.StackWorldBehaviors;
import com.google.common.util.concurrent.Runnables;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public interface PatternProviderTarget {
    @Nullable
    static PatternProviderTarget get(Level l, BlockPos pos, @Nullable BlockEntity be, Direction side, IActionSource src) {
        if (be == null) {
            return null;
        } else {
            MEStorage storage = be.getCapability(Capabilities.STORAGE, side).orElse((MEStorage) null);
            if (storage != null) {
                return wrapMeStorage(storage, src);
            } else {
                Map<AEKeyType, ExternalStorageStrategy> strategies = StackWorldBehaviors.createExternalStorageStrategies((ServerLevel)l, pos, side);
                IdentityHashMap<AEKeyType, MEStorage> externalStorages = new IdentityHashMap(2);

                for(Map.Entry<AEKeyType, ExternalStorageStrategy> entry : strategies.entrySet()) {
                    MEStorage wrapper = entry.getValue().createWrapper(false, Runnables.doNothing());
                    if (wrapper != null) {
                        externalStorages.put((AEKeyType) entry.getKey(), wrapper);
                    }
                }

                if (!externalStorages.isEmpty()) {
                    return wrapMeStorage(new CompositeStorage(externalStorages), src);
                } else {
                    return null;
                }
            }
        }
    }

    private static PatternProviderTarget wrapMeStorage(final MEStorage storage, final IActionSource src) {
        return new PatternProviderTarget() {
            public long insert(AEKey what, long amount, Actionable type) {
                return storage.insert(what, amount, type, src);
            }

            public boolean containsPatternInput(Set<AEKey> patternInputs) {
                for(Object2LongMap.Entry<AEKey> stack : storage.getAvailableStacks()) {
                    if (patternInputs.contains(((AEKey)stack.getKey()).dropSecondary())) {
                        return true;
                    }
                }
                return false;
            }

            public MEStorage getStorage() {
                return storage;
            }
        };
    }

    long insert(AEKey var1, long var2, Actionable var4);

    boolean containsPatternInput(Set<AEKey> var1);

    MEStorage getStorage();
}