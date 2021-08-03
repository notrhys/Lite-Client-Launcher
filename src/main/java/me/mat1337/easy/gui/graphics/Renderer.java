package me.mat1337.easy.gui.graphics;

import lombok.Getter;
import me.mat1337.easy.gui.util.vec.Vec2;

import java.awt.*;

public class Renderer {

    private static final Color SHADOW_COLOR = new Color(15, 15, 15);

    private Graphics2D graphics;
    private Graphics2D lastGraphics;

    @Getter
    private Vec2 offset;

    @Getter
    private Vec2 mousePos;

    @Getter
    private int width;

    @Getter
    private int height;

    public Renderer(Graphics2D graphics, Vec2 mousePos, int width, int height) {
        this.graphics = graphics;
        this.offset = new Vec2();
        this.lastGraphics = graphics;
        this.mousePos = mousePos;
        this.width = width;
        this.height = height;
    }

    public void drawCircle(Vec2 pos, float radius, Color color) {
        drawCircle(pos.x, pos.y, radius, color);
    }

    public void drawCircle(double x, double y, float radius, Color color) {
        Color cache = graphics.getColor();
        graphics.setColor(color);
        graphics.fillOval((int) x, (int) y, (int) radius, (int) radius);
        graphics.setColor(cache);
    }

    public void setLineSize(float size) {
        graphics.setStroke(new BasicStroke(size));
    }

    public void drawShape(Shape shape, Vec2 pos, Color color) {
        Color cache = graphics.getColor();
        pushGraphics();
        graphics.setColor(color);
        if (pos != null) {
            translate(pos);
        }
        graphics.fill(shape);
        graphics.setColor(cache);
        popGraphics();
    }

    public void drawShape(Shape shape, Color color) {
        drawShape(shape, null, color);
    }

    public void translateScene(double x, double y) {
        offset = new Vec2((float) x, (float) y);
    }

    public void translateScene(Vec2 pos) {
        translate(pos.x, pos.y);
    }

    public void pushGraphics() {
        lastGraphics = graphics;
        graphics = (Graphics2D) graphics.create();
    }

    public void popGraphics() {
        graphics = lastGraphics;
    }

    public void drawOutlineRoundedRect(Vec2 pos, double width, double height, float radius, Color color) {
        drawOutlineRoundedRect(pos.x, pos.y, width, height, radius, color);
    }

    public void drawOutlineRoundedRect(double x, double y, double width, double height, float radius, Color color) {
        Color cache = graphics.getColor();
        graphics.setColor(color);
        graphics.drawRoundRect((int) x, (int) y, (int) width, (int) height, (int) radius, (int) radius);
        graphics.setColor(cache);
    }

    public void drawRoundedRect(Vec2 pos, double width, double height, double radius, Color color) {
        drawRoundedRect(pos.x, pos.y, width, height, radius, color);
    }

    public void drawRoundedRect(double x, double y, double width, double height, double radius, Color color) {
        Color cache = graphics.getColor();
        graphics.setColor(color);
        graphics.fillRoundRect((int) (offset.x + x), (int) (offset.y + y), (int) width, (int) height, (int) radius, (int) radius);
        graphics.setColor(cache);
    }

    public void drawRect(Vec2 pos, int width, int height, Color color) {
        drawRect(pos.x, pos.y, width, height, color);
    }

    public void drawRect(double x, double y, int width, int height, Color color) {
        Color cache = graphics.getColor();
        graphics.setColor(color);
        graphics.fillRect((int) (offset.x + x), (int) (offset.y + y), width, height);
        graphics.setColor(cache);
    }

    public void scale(double x, double y) {
        graphics.scale(x, y);
    }

    public void scale(Vec2 pos) {
        scale(pos.x, pos.y);
    }

    public void translate(double x, double y) {
        graphics.translate(x, y);
    }

    public void translate(Vec2 pos) {
        translate(pos.x, pos.y);
    }

    public void clip(Shape shape) {
        graphics.clip(shape);
    }

    public void clip(double x, double y, int width, int height) {
        graphics.clipRect((int) (offset.x + x), (int) (offset.y + y), width, height);
    }

    public void clip(Vec2 pos, int width, int height) {
        clip(pos.x, pos.y, width, height);
    }

    public void drawCenteredStringWithShadow(String str, Vec2 pos, Color color) {
        drawCenteredStringWithShadow(str, pos.x, pos.y, color);
    }

    public void drawCenteredStringWithShadow(String str, double x, double y, Color color) {
        drawStringWithShadow(str, x - getStringWidth(str) / 2.0D, y + getStringHeight(str) / 4.0D, color);
    }

    public void drawStringWithShadow(String str, Vec2 pos, Color color) {
        drawStringWithShadow(str, pos.x, pos.y, color);
    }

    public void drawStringWithShadow(String str, double x, double y, Color color) {
        drawString(str, x + 1, y + 1, SHADOW_COLOR);
        drawString(str, x, y, color);
    }

    public void drawCenteredString(String str, Vec2 pos, Color color) {
        drawCenteredString(str, pos.x, pos.y, color);
    }

    public void drawCenteredString(String str, double x, double y, Color color) {
        drawString(str, x - getStringWidth(str) / 2.0D, y + getStringHeight(str) / 4.0D, color);
    }

    public void drawString(String str, double x, double y, Color color) {
        Color cache = graphics.getColor();
        graphics.setColor(color);
        graphics.drawString(str, (int) (offset.x + x), (int) (offset.y + y));
        graphics.setColor(cache);
    }

    public void drawString(String str, Vec2 pos, Color color) {
        drawString(str, pos.x, pos.y, color);
    }

    public float getStringWidth(String str) {
        return (float) fontMetrics().getStringBounds(str, graphics).getWidth();
    }

    public float getStringHeight(String str) {
        return (float) fontMetrics().getStringBounds(str, graphics).getHeight();
    }

    public float getFontHeight() {
        return graphics.getFont().getSize();
    }

    public Vec2 getStringCenter(String str) {
        return new Vec2(getStringCenterX(str), getStringCenterY(str));
    }

    public void setFont(Font font) {
        graphics.setFont(font);
    }

    private float getStringCenterX(String str) {
        return (float) fontMetrics().getStringBounds(str, graphics).getCenterX();
    }

    private float getStringCenterY(String str) {
        return (float) fontMetrics().getStringBounds(str, graphics).getCenterY();
    }

    private FontMetrics fontMetrics() {
        return graphics.getFontMetrics();
    }

}
