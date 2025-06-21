package lu.kolja.expandedae.datagen;

import com.google.gson.JsonObject;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.enums.ExpCraftingCPU;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class CPUEnabledCondition implements ICondition {
    private static final ResourceLocation NAME = Expandedae.makeId("cpu_enabled");
    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test(IContext context) {
        return cpu.isEnabled();
    }
    private final ExpCraftingCPU cpu;

    public CPUEnabledCondition(ExpCraftingCPU cpu) {
        this.cpu = cpu;
    }

    @Override
    public String toString() {
        return "cpu_enabled(\"" + cpu.name() + "\")";
    }

    public static class Serializer implements IConditionSerializer<CPUEnabledCondition> {
        public static final CPUEnabledCondition.Serializer INSTANCE = new CPUEnabledCondition.Serializer();

        @Override
        public void write(JsonObject json, CPUEnabledCondition value) {
            json.addProperty("cpu_enabled", value.cpu.name());
        }

        @Override
        public CPUEnabledCondition read(JsonObject json) {
            return new CPUEnabledCondition(ExpCraftingCPU.valueOf(GsonHelper.getAsString(json, "cpu_enabled")));
        }

        @Override
        public ResourceLocation getID() {
            return CPUEnabledCondition.NAME;
        }
    }
}
