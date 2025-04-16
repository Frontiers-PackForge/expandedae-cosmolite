package lu.kolja.expandedae.helper;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;

public class KeybindUtil {

    public static boolean isShiftDown() {
        return isKeyDown(InputConstants.KEY_LSHIFT);
    }
    public static boolean isCtrlDown() {
        return isKeyDown(InputConstants.KEY_LCONTROL);
    }
    public static boolean isLeftClick() {
        return isKeyDown(InputConstants.MOUSE_BUTTON_LEFT);
    }
    public static boolean isRightClick() {
        return isKeyDown(InputConstants.MOUSE_BUTTON_RIGHT);
    }
    public static boolean isKeyDown(int key) {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key);
    }

    // Pattern modifying logic
    public static final int SHIFT_MULTIPLIER = 2;
    public static final int CTRL_MULTIPLIER = 8;


    public static int shiftMultiplier() {
        return isShiftDown() ? SHIFT_MULTIPLIER : 1;
    }
    public static int ctrlMultiplier() {
        return isCtrlDown() ? CTRL_MULTIPLIER : 1;
    }
}
