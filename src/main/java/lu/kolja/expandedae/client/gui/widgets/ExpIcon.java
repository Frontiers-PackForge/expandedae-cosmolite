package lu.kolja.expandedae.client.gui.widgets;

import lu.kolja.expandedae.Expandedae;
import appeng.client.gui.style.Blitter;
import net.minecraft.resources.ResourceLocation;

public enum ExpIcon {
    MODIFY_PATTERNS(0, 0);

    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public static final ResourceLocation TEXTURE = Expandedae.makeId("textures/gui/states.png");
    public static final int TEXTURE_WIDTH = 256;
    public static final int TEXTURE_HEIGHT = 256;

    ExpIcon(int x, int y) {
        this(x, y, 16, 16);
    }

    ExpIcon(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Blitter getBlitter() {
        return Blitter.texture(TEXTURE, 256, 256).src(this.x, this.y, this.width, this.height);
    }
}
