package me.mat1337.easy.gui.util;

import lombok.AllArgsConstructor;
import me.mat1337.easy.gui.graphics.element.Element;
import me.mat1337.easy.gui.graphics.element.label.Label;
import me.mat1337.easy.gui.util.vec.Vec2;

@AllArgsConstructor
public class Constraint {

    // in percentage
    public Vec2 pos;
    private Vec2 offset;
    public boolean[] isCentered;

    public Constraint(Vec2 pos) {
        this(pos, new Vec2(), new boolean[]{false, false});
    }

    public Constraint(Type type) {
        this(type.align, new Vec2(), type.center);
    }

    public Vec2 apply(Element element, Vec2 origin, int width, int height, int maxWidth, int maxHeight) {
        // calculate the offsets on x & y axis
        float xOffset = maxWidth * pos.x;
        float yOffset = maxHeight * pos.y;

        // get the new position
        Vec2 pos = origin.clone().add(xOffset, yOffset);

        // and subtract the width and height if we want to center it
        pos.sub(((element instanceof Label) && isCentered[0]) ? width / 2.0f : 0, ((element instanceof Label) && isCentered[1]) ? height / 2.0f : 0);

        // clamping the Y axis so it fits the screen
        Vec2 scalePos = pos.clone().sub(origin).div(maxWidth, maxHeight).add(new Vec2(width, height).div(maxWidth, maxHeight));
        if (this.pos.y >= 0.5f) {
            if (scalePos.y >= this.pos.y) {
                pos.sub(0, height - (height * (scalePos.y - this.pos.y)));
            }
        } else {
            scalePos = pos.clone().sub(origin).div(maxWidth, maxHeight);
            if (scalePos.y <= this.pos.y) {
                pos.add(0, height - (height * (this.pos.y - scalePos.y)));
            }
        }

        // clamping the X axis so it fits the screen
        scalePos = pos.clone().sub(origin).div(maxWidth, maxHeight).add(new Vec2(width, height).div(maxWidth, maxHeight));
        if (this.pos.x >= 0.5f) {
            if (scalePos.x >= this.pos.x) {
                pos.x = (maxWidth - width) * this.pos.x;
            }
        } else {
            scalePos = pos.clone().sub(origin).div(maxWidth, maxHeight);
            if (scalePos.x <= this.pos.x) {
                pos.x = xOffset;
            }
        }

        // return the calculated position
        return pos.clone().add(offset);
    }

    public Constraint offset(float x, float y) {
        this.offset = new Vec2(x, y);
        return this;
    }

    public Constraint offset(Vec2 offset) {
        this.offset = offset;
        return this;
    }

    @AllArgsConstructor
    public enum Type {

        TOP_LEFT(new Vec2(0.05f, 0.05f), new boolean[]{false, false}),
        TOP_CENTER(new Vec2(0.5f, 0.05f), new boolean[]{true, false}),
        TOP_RIGHT(new Vec2(0.95f, 0.05f), new boolean[]{false, false}),
        BOTTOM_LEFT(new Vec2(0.05f, 0.95f), new boolean[]{false, true}),
        BOTTOM_CENTER(new Vec2(0.5f, 0.95f), new boolean[]{true, false}),
        BOTTOM_RIGHT(new Vec2(0.95f, 0.95f), new boolean[]{false, false}),
        LEFT_CENTER(new Vec2(0.05f, 0.5f), new boolean[]{false, true}),
        RIGHT_CENTER(new Vec2(0.95f, 0.5f), new boolean[]{false, true}),
        FULL_CENTER(new Vec2(0.5f, 0.5f), new boolean[]{true, true});

        Vec2 align;
        boolean[] center;

        public Type center(boolean x, boolean y) {
            center[0] = x;
            center[1] = y;
            return this;
        }

        public Type unCenter() {
            center[0] = false;
            center[1] = false;
            return this;
        }

    }

}
