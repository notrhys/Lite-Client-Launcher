package me.mat1337.easy.gui.util.vec;

import lombok.AllArgsConstructor;

import java.awt.*;

@AllArgsConstructor
public class Vec2 {

    public float x;
    public float y;

    public Vec2() {
        this(0, 0);
    }

    public Vec2(Point point) {
        this((float) point.getX(), (float) point.getY());
    }

    public Vec2(Dimension dimension) {
        this(dimension.width, dimension.height);
    }

    public Vec2 add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vec2 sub(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vec2 mul(float x, float y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public Vec2 div(float x, float y) {
        this.x /= x;
        this.y /= y;
        return this;
    }

    public Vec2 add(Vec2 vec) {
        return add(vec.x, vec.y);
    }

    public Vec2 sub(Vec2 vec) {
        return sub(vec.x, vec.y);
    }

    public Vec2 mul(Vec2 vec) {
        return mul(vec.x, vec.y);
    }

    public Vec2 div(Vec2 vec) {
        return div(vec.x, vec.y);
    }

    public Vec2 max(Vec2 vec) {
        return max(vec.x, vec.y);
    }

    public Vec2 max(float x, float y) {
        this.x = Math.max(x, this.x);
        this.y = Math.max(y, this.y);
        return this;
    }

    public Vec2 min(Vec2 vec) {
        return min(vec.x, vec.y);
    }

    public Vec2 min(float x, float y) {
        this.x = Math.min(x, this.x);
        this.y = Math.min(y, this.y);
        return this;
    }

    public Vec2 setY(float y) {
        this.y = y;
        return this;
    }

    public Vec2 setX(float x) {
        this.x = x;
        return this;
    }

    public Vec2 flatY() {
        return setY(0);
    }

    public Vec2 flatX() {
        return setX(0);
    }

    public Vec2 clone() {
        return new Vec2(x, y);
    }

    public Point toPoint() {
        return new Point((int) x, (int) y);
    }

    public Dimension toDimension() {
        return new Dimension((int) x, (int) y);
    }

    @Override
    public String toString() {
        return "Vec2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

}
