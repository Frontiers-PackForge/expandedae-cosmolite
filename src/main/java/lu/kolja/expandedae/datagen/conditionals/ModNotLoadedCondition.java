package lu.kolja.expandedae.datagen.conditionals;

import com.google.gson.JsonObject;
import lu.kolja.expandedae.Expandedae;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

public record ModNotLoadedCondition(String modId) implements ICondition {
    private static final ResourceLocation NAME = Expandedae.makeId("mod_not_loaded");

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test(@NotNull ICondition.IContext context) {
        return !ModList.get().isLoaded(this.modId);
    }

    public @NotNull String toString() {
        return "mod_not_loaded(\"" + this.modId + "\")";
    }

    public static class Serializer implements IConditionSerializer<ModNotLoadedCondition> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject jsonObject, ModNotLoadedCondition modNotLoadedCondition) {
            jsonObject.addProperty("modid", modNotLoadedCondition.modId);
        }

        @Override
        public ModNotLoadedCondition read(JsonObject jsonObject) {
            return new ModNotLoadedCondition(GsonHelper.getAsString(jsonObject, "modid"));
        }

        @Override
        public ResourceLocation getID() {
            return NAME;
        }
    }
}