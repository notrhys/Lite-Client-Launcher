package me.mat1337.easy.gui.graphics.theme.impl;

import me.mat1337.easy.gui.graphics.theme.Theme;

import java.awt.*;

public class LightTheme extends Theme {

    public LightTheme() {
        super(
                new Color(226, 230, 231),
                new Color(255, 57, 43),
                Color.BLACK,

                new Labels(
                        new Color(44, 44, 44)
                ),

                new Buttons(
                        new Color(77, 77, 77),
                        new Color(255, 255, 255),
                        new Color(187, 187, 187),
                        new Color(167, 167, 167)
                ),

                new Buttons.Toggle(
                        new Color(77, 77, 77),
                        new Color(255, 255, 255),
                        new Color(187, 187, 187),
                        new Color(167, 167, 167)
                ),

                new Buttons.DropDown(
                        new Color(44, 44, 44),
                        new Color(255, 255, 255),
                        new Color(187, 187, 187),
                        new Color(167, 167, 167),
                        new Color(200, 200, 200)
                ),

                new ProgressBars(
                        new Color(77, 77, 77),
                        new Color(255, 255, 255),
                        new Color(46, 204, 113)
                ),

                new Sliders(
                        new Color(255, 255, 255),
                        new Color(46, 204, 113),
                        new Color(255, 255, 255),
                        new Color(197, 197, 187)
                ),

                new CheckBoxes(
                        new Color(255, 255, 255),
                        new Color(195, 195, 195),
                        new Color(46, 204, 113)
                )
        );
    }

}
