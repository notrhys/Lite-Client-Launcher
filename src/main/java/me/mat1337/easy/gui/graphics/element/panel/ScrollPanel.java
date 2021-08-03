package me.mat1337.easy.gui.graphics.element.panel;

import lombok.Getter;
import lombok.Setter;
import me.mat1337.easy.gui.graphics.Renderer;
import me.mat1337.easy.gui.util.Constraint;
import me.mat1337.easy.gui.util.MathUtil;
import me.mat1337.easy.gui.util.vec.Vec2;

import java.awt.*;

@Getter
@Setter
public class ScrollPanel extends Panel {

    protected float scrollIndex;
    protected float scrollSpeed;
    protected float scrollBarWidth;
    protected int scrollBarMargin;
    protected int itemMargin;
    protected boolean isShowingScrollBar;
    protected boolean canVerticalScroll;

    public ScrollPanel(Constraint constraint, int width, int height) {
        super(constraint, width, height);
        this.scrollIndex = 0;
        this.scrollSpeed = 2f;
        this.scrollBarWidth = 3f;
        this.scrollBarMargin = 5;
        this.itemMargin = 10;
        this.isShowingScrollBar = true;
        this.canVerticalScroll = true;
    }

    @Override
    public void onMouseScroll(Vec2 pos, double wheelRotation, int scrollAmount) {
        if (canVerticalScroll) {
            scrollIndex += (scrollAmount * Math.abs(scrollSpeed)) * wheelRotation;
            scrollIndex = Math.max(0, Math.min(getMaxScrollAmount(), scrollIndex));
            manager.forEach(element -> {
                boolean isHovered = element.isHovered(pos.clone().add(0, scrollIndex));
                if (element.isHovered != isHovered) {
                    element.isHovered = isHovered;
                    gui.getDisplay().repaint();
                }
            });
            gui.getDisplay().repaint();
        }
    }

    @Override
    public void onUpdate(Renderer renderer) {
        renderer.pushGraphics();
        renderer.clip(pos, width, height);
        renderer.translate(0, -scrollIndex);

        super.onUpdate(renderer);

        renderer.popGraphics();
        renderScrollBar(renderer);
    }

    public void renderScrollBar(Renderer renderer) {
        if (!isShowingScrollBar || !canVerticalScroll) {
            return;
        }
        float maxScroll = getMaxScrollAmount();
        if (maxScroll > 0) {
            double height = this.height - scrollBarMargin;
            double yPos = pos.y + (scrollBarMargin / 2.0f);

            int scale = (int) (height * height / getContentHeight());
            scale = MathUtil.clamp_int(scale, 32, (int) (height - 8));

            int scrollY = (int) ((int) scrollIndex * (height - scale) / maxScroll + yPos);
            if (scrollY < yPos)
                scrollY = (int) yPos;

            renderer.drawRoundedRect(new Vec2(pos.x + width - (scrollBarMargin + scrollBarWidth), scrollY), (int) scrollBarWidth, scale, scrollBarWidth * 2.0f, Color.GRAY);
        }
    }

    public float getMaxScrollAmount() {
        return getContentHeight() - height;
    }

    public void resetScroll() {
        scrollIndex = 0;
        gui.getDisplay().repaint();
    }

}
