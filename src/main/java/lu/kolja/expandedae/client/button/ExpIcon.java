package lu.kolja.expandedae.client.button;

import lu.kolja.expandedae.Expandedae;
import appeng.client.gui.style.Blitter;
import net.minecraft.resources.ResourceLocation;

public class ExpIcon {
    private static final ResourceLocation TEXTURE = Expandedae.makeId(""); //TODO choose path

    public static final Blitter HIGHLIGHT = Blitter.texture(TEXTURE, 64, 64).src(16, 0, 16, 16);
}
