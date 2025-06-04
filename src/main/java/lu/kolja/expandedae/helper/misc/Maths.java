package lu.kolja.expandedae.helper.misc;

import net.minecraft.core.BlockPos;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Maths {

    public static float clamp(float a, float b, float val){
        return max(a,min(b,val));
    }

    public static double clamp(double a, double b, double val) {
        return max(a,min(b,val));
    }

    public static int clamp(int a, int b, int val) {
        return max(a, min(b, val));
    }

    public static long getTaxicabDistance(BlockPos blockPos, BlockPos blockPos2) {
        long dx = Math.abs(blockPos.getX() - blockPos2.getX());
        long dy = Math.abs(blockPos.getY() - blockPos2.getY());
        long dz = Math.abs(blockPos.getZ() - blockPos2.getZ());
        return dx + dy + dz;
    }

    public static int clamp_int(int num, int min, int max) {
        return num < min ? min : (Math.min(num, max));
    }
}
