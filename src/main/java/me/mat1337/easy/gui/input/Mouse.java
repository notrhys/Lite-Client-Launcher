package me.mat1337.easy.gui.input;

import lombok.Getter;
import me.mat1337.easy.gui.EasyGui;
import me.mat1337.easy.gui.util.vec.Vec2;

import java.awt.event.*;

public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {

    private EasyGui easyGui;

    @Getter
    private Vec2 pos;

    @Getter
    private boolean isMouseInApp;

    public Mouse(EasyGui easyGui) {
        this.easyGui = easyGui;
        this.pos = new Vec2();
        this.isMouseInApp = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        pos = new Vec2(e.getPoint());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pos = new Vec2(e.getPoint());
        easyGui.onMouseClick(pos, e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pos = new Vec2(e.getPoint());
        easyGui.onMouseReleased(pos, e.getButton());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        pos = new Vec2(e.getPoint());
        isMouseInApp = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        pos = new Vec2(e.getPoint());
        isMouseInApp = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        pos = new Vec2(e.getPoint());
        easyGui.onMouseDrag(pos);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        pos = new Vec2(e.getPoint());
        easyGui.onMouseMove(new Vec2(e.getPoint()));
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        pos = new Vec2(e.getPoint());
        easyGui.onMouseScroll(pos, e.getWheelRotation(), e.getScrollAmount());
    }

}
