package me.mat1337.easy.gui.graphics.element;

import lombok.Getter;
import lombok.Setter;
import me.mat1337.easy.gui.EasyGui;
import me.mat1337.easy.gui.graphics.Renderer;
import me.mat1337.easy.gui.util.Constraint;
import me.mat1337.easy.gui.util.vec.Vec2;

public class Element {

    @Setter
    protected EasyGui gui;

    public Element parent;
    public Constraint constraint;

    @Getter
    @Setter
    protected Vec2 pos;

    public int width;
    public int height;

    public boolean isHovered;

    public Element(Constraint constraint, int width, int height) {
        this.constraint = constraint;
        this.parent = null;
        this.width = width;
        this.height = height;
        this.pos = new Vec2();
    }

    public Element(Constraint constraint) {
        this(constraint, 0, 0);
    }

    public void onKeyPress(char keyChar, int keyCode) {

    }

    public void onMouseMoved(Vec2 mouse) {
    }

    public void onMouseEnter(Vec2 mouse) {
    }

    public void onMouseLeave(Vec2 mouse) {
    }

    public void onMouseScroll(Vec2 pos, double wheelRotation, int scrollAmount) {
    }

    public void onDragged(Vec2 mouse) {
    }

    public void onClick(Vec2 mouse, int button) {
    }

    public void onClicked(Vec2 mouse, int button) {
    }

    public void onUpdate(Renderer renderer) {
        this.pos = translate();
    }

    public Vec2 translate() {
        return constraint.apply(this, parent.pos, this.width, this.height, parent.width, parent.height);
    }

    protected boolean isHovered(Vec2 pos, int width, int height, Vec2 mouse) {
        return mouse.x >= pos.x && mouse.y >= pos.y && mouse.x <= (pos.x + width) && mouse.y <= (pos.y + height);
    }

    public boolean isHovered(Vec2 mouse) {
        return isHovered(pos, width, height, mouse);
    }

}
