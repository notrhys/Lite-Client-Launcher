package me.mat1337.easy.gui.util;

import java.awt.*;

public class ColorUtil {

    private ColorUtil() {
    }

    public static Color darken(Color color, int percent) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        return new Color((int) Math.max(0, Math.min(255, (r - (r * (percent / 100.0f))))), (int) Math.max(0, Math.min(255, (g - (g * (percent / 100.0f))))), (int) Math.max(0, Math.min(255, (b - (b * (percent / 100.0f))))));
    }

}
