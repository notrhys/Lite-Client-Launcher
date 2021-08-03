package me.mat1337.easy.gui.util.vec;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Vec4 {

    public float x;
    public float y;
    public float z;
    public float a;

    public Vec4(float a) {
        this(a, a, a, a);
    }

    public Vec4() {
        this(0);
    }

}