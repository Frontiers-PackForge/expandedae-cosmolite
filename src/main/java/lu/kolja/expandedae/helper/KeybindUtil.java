package lu.kolja.expandedae.helper;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;

public class KeybindUtil {
    public static boolean isShiftDown() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LSHIFT);
    }
}
