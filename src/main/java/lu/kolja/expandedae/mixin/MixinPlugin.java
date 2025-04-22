package lu.kolja.expandedae.mixin;

import java.util.List;
import java.util.Set;

import lu.kolja.expandedae.helper.Tuple;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class MixinPlugin implements IMixinConfigPlugin {

    /**
     * Basically if mod A is loaded, load B, else load C
     * @A modId
     * @B Mixin Class 1
     * @C Mixin Class 2
     */
    public static final Object2ObjectMap<String, Tuple<String, String>> mixinMap = new Object2ObjectOpenHashMap<>( //TODO Refactor, probably
            new String[]{},
            new Tuple[]{}
    );

    private static boolean isModLoaded(String modId) {
        if (ModList.get() == null) {
            return LoadingModList.get().getMods().stream()
                    .map(ModInfo::getModId)
                    .anyMatch(modId::equals);
        } else {
            return ModList.get().isLoaded(modId);
        }
    }

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    /*
     * Kinda hacky, but it works
     */
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        /*if (mixinMap.containsKey(mixinClassName)) return !isModLoaded(mixinMap.get(mixinClassName));
        return isModLoaded(mixinMap.); //TODO FIX THIS*/
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() { return null; }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
