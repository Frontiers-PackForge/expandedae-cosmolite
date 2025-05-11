package lu.kolja.expandedae.client.gui.widgets;

import appeng.client.gui.style.Blitter;
import lu.kolja.expandedae.Expandedae;
import net.minecraft.resources.ResourceLocation;

public enum ExpIcon {

    MODIFY_PATTERNS(0, 0),
    MULTIPLY_2(16, 0),
    MULTIPLY_3(32, 0),
    MULTIPLY_8(48, 0),
    DIVISION_2(64, 0),
    DIVISION_3(80, 0),
    DIVISION_8(96, 0),
    TOOLBAR_BUTTON_BACKGROUND(240, 240);

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
        return Blitter.texture(TEXTURE, TEXTURE_WIDTH, TEXTURE_HEIGHT).src(this.x, this.y, this.width, this.height);
    }
}
