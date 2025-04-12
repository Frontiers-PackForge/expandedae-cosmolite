package lu.kolja.expandedae.client.button;

import lu.kolja.expandedae.client.render.ExpHighlightHandler;
import lu.kolja.expandedae.client.util.GuiUtil;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import appeng.client.gui.style.Blitter;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class HighlightButton extends ExpButton {
    private float multiplier;
    private Runnable successJob;
    private BlockPos pos;
    private ResourceKey<Level> dim;
    @Nullable
    private Direction face;

    public HighlightButton() {
        super(HighlightButton::highlight);
    }

    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
    }
    public void setSuccessJob(Runnable successJob) {
        this.successJob = successJob;
    }
    public void setTarget(BlockPos pos, ResourceKey<Level> dim, @Nullable Direction face) {
        this.pos = pos;
        this.dim = dim;
        this.face = face;
    }
    public void setTarget(BlockPos pos, ResourceKey<Level> world) {
        this.setTarget(pos, world, null);
    }

    private static void highlight(Button btn) {
        GL11.glPushMatrix();
        if (btn instanceof HighlightButton hb) {
            if (hb.dim != null && hb.pos != null) {
                if (hb.face == null) {
                    ExpHighlightHandler.highlight(hb.pos, hb.dim, System.currentTimeMillis() + (long) (600 * hb.multiplier));
                } else {
                    var origin = getNorthPartModel().move(hb.pos);
                    var center = new AABB(hb.pos).getCenter();
                    switch (hb.face) {
                        case WEST -> origin = GuiUtil.rotor(origin, center, Direction.Axis.Y,  (float) (Math.PI / 2));
                        case SOUTH -> origin = GuiUtil.rotor(origin, center, Direction.Axis.Y,  (float) Math.PI);
                        case EAST -> origin = GuiUtil.rotor(origin, center, Direction.Axis.Y,  (float) (-Math.PI / 2));
                        case UP -> origin = GuiUtil.rotor(origin, center, Direction.Axis.X,  (float) (-Math.PI / 2));
                        case DOWN -> origin = GuiUtil.rotor(origin, center, Direction.Axis.X,  (float) (Math.PI / 2));
                    }
                }
                if (hb.successJob != null) {
                    hb.successJob.run();
                }
            }
        }
    }

    private static AABB getNorthPartModel() {
        return new AABB(2 / 16D, 2 / 16D, 0, 14 / 16D, 14 / 16D, 2 / 16D);
    }

    @Override
    Blitter getBlitterIcon() {
        return ExpIcon.HIGHLIGHT;
    }
}
