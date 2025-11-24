package lu.kolja.expandedae.helper.misc;

import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.stacks.GenericStack;
import appeng.crafting.pattern.AEProcessingPattern;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Arrays;

public class PatternHelper {

    public static final int BASE_FACTOR = 2;

    public static ItemStack modifyPatterns(ItemStack stack, int mult, Level level) {
        var detail = PatternDetailsHelper.decodePattern(stack, level);
        boolean division = mult < 0;
        mult = Math.abs(mult);
        if (detail instanceof AEProcessingPattern processingPattern) {
            var input = Arrays.stream(processingPattern.getSparseInputs()).toArray(GenericStack[]::new);
            var output = Arrays.stream(processingPattern.getOutputs()).toArray(GenericStack[]::new);
            if (checkModify(input, getScale(mult), division) && checkModify(output, getScale(mult), division)) {
                var mulInput = new GenericStack[input.length];
                var mulOutput = new GenericStack[output.length];
                var author = detail.getAuthor();
                modifyStacks(input, mulInput, getScale(mult), division);
                modifyStacks(output, mulOutput, getScale(mult), division);
                return PatternDetailsHelper.encodeProcessingPattern(
                        mulInput,
                        mulOutput,
                        author
                );
            }
        }
        return stack;
    }

    public static int getScale(int multiplier) {
        return BASE_FACTOR * multiplier;
    }

    public static boolean checkModify(GenericStack[] stacks, int scale, boolean division) {
        for (var stack : stacks) {
            if (stack == null) continue;
            boolean invalid = division
                    ? stack.amount() % scale != 0
                    : stack.amount() * scale > 999999L * stack.what().getAmountPerUnit();

            if (invalid) return false;
        }
        return true;
    }

    public static void modifyStacks(GenericStack[] stacks, GenericStack[] des, int scale, boolean division) {
        for (int i = 0; i < stacks.length; i ++) {
            if (stacks[i] != null) {
                long amt = division ? stacks[i].amount() / scale : stacks[i].amount() * scale;
                des[i] = new GenericStack(stacks[i].what(), amt);
            }
        }
    }
}
