package me.mat1337.easy.gui.graphics.theme.impl;

import me.mat1337.easy.gui.graphics.theme.Theme;

import java.awt.*;

public class DarkTheme extends Theme {

    public DarkTheme() {
        super(
                new Color(33, 33, 33),
                new Color(192, 57, 43),
                new Color(66, 66, 66),

                new Labels(
                        Color.WHITE
                ),

                new Buttons(
                        Color.WHITE,
                        new Color(23, 23, 23),
                        new Color(61, 61, 61),
                        new Color(23, 23, 23)
                ),

                new Buttons.Toggle(
                        Color.WHITE,
                        new Color(66, 66, 66),
                        new Color(61, 61, 61),
                        new Color(45, 45, 45)
                ),

                new Buttons.DropDown(
                        Color.WHITE,
                        new Color(23, 23, 23),
                        new Color(61, 61, 61),
                        new Color(23, 23, 23),
                        new Color(100, 100, 100)
                ),

                new ProgressBars(
                        Color.WHITE,
                        new Color(45, 45, 45),
                        new Color(46, 204, 113)
                ),

                new Sliders(
                        new Color(23, 23, 23),
                        new Color(46, 204, 113),
                        new Color(46, 204, 113),
                        new Color(39, 174, 96)
                ),

                new CheckBoxes(
                        new Color(66, 66, 66), // background
                        new Color(81, 81, 81),
                        new Color(46, 204, 113)
                )
        );
    }

}
