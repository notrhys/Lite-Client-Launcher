package me.mat1337.easy.gui.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MathUtil {

    public static double roundToPlace(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @SuppressWarnings("UnnecessaryCallToStringValueOf")
    public static float trimFloat(int degree, float d) {
        String format = "#.#";
        for (int i = 1; i < degree; ++i) {
            format = String.valueOf(format) + "#";
        }

        DecimalFormat twoDForm = new DecimalFormat(format);
        return Float.parseFloat(twoDForm.format(d).replaceAll(",", "."));
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd2 = new BigDecimal(value);
        bd2 = bd2.setScale(places, RoundingMode.HALF_UP);
        return bd2.doubleValue();
    }

    public static int clamp_int(int num, int min, int max) {
        return num < min ? min : Math.min(num, max);
    }

}