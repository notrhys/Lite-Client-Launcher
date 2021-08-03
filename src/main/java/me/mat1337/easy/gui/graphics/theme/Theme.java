package me.mat1337.easy.gui.graphics.theme;

import lombok.AllArgsConstructor;

import java.awt.*;

@AllArgsConstructor
public class Theme {

    public Color background;
    public Color base;
    public Color border;

    public Labels labels;
    public Buttons buttons;
    public Buttons.Toggle toggleButtons;
    public Buttons.DropDown dropDowns;
    public ProgressBars progressBars;
    public Sliders sliders;
    public CheckBoxes checkBoxes;

    @AllArgsConstructor
    public static class Labels {

        public Color text;

    }

    @AllArgsConstructor
    public static class Buttons {

        public Color label;
        public Color background;
        public Color hover;
        public Color click;

        @AllArgsConstructor
        public static class Toggle {

            public Color label;
            public Color background;
            public Color hover;
            public Color toggled;

        }

        @AllArgsConstructor
        public static class DropDown {

            public Color label;
            public Color background;
            public Color hover;
            public Color click;
            public Color itemHover;

        }

    }

    @AllArgsConstructor
    public static class ProgressBars {

        public Color label;
        public Color background;
        public Color fill;

    }

    @AllArgsConstructor
    public static class Sliders {

        public Color background;
        public Color fill;
        public Color selector;
        public Color selectorBorder;

    }

    @AllArgsConstructor
    public static class CheckBoxes {

        public Color background;
        public Color border;
        public Color fill;

    }

}
