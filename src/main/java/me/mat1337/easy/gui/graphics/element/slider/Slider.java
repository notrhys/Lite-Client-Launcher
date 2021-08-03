package me.mat1337.easy.gui.graphics.element.slider;

import lombok.Getter;
import me.mat1337.easy.gui.graphics.Renderer;
import me.mat1337.easy.gui.graphics.element.Element;
import me.mat1337.easy.gui.graphics.theme.Theme;
import me.mat1337.easy.gui.util.Constraint;
import me.mat1337.easy.gui.util.action.ActionType;
import me.mat1337.easy.gui.util.vec.Vec2;

@Getter
public class Slider extends Element {

    private float min;
    private float max;
    private float current;

    private float cornerRadius;
    private int margin;
    private boolean selectorHeld;

    public Slider(Constraint constraint, int width, int height, float min, float max, float current) {
        super(constraint, width, height);
        this.min = min;
        this.max = max;
        this.current = current;
        this.cornerRadius = 5f;
        this.margin = 2;
        this.selectorHeld = false;
    }

    @Override
    public void onClick(Vec2 mouse, int button) {
        float radius = height;
        Vec2 selectorPos = pos.clone().add((width - margin * 2) * (current / max) - radius / 2.0f, (height - radius) / 2.0f);
        if (mouse.x >= selectorPos.x && mouse.x <= (selectorPos.x + radius) && mouse.y >= selectorPos.y && mouse.y <= (selectorPos.y + radius)) {
            selectorHeld = true;
        }
    }

    @Override
    public void onClicked(Vec2 mouse, int button) {
        selectorHeld = false;
    }

    @Override
    public void onDragged(Vec2 mouse) {
        if (selectorHeld) {
            Vec2 progress = mouse.clone().sub(pos).div(width, height);
            float value = Math.max(min, Math.min(max, max * progress.x));
            if (value > current) {
                gui.actionPerformed(this, ActionType.SLIDER_INCREMENT);
            } else {
                gui.actionPerformed(this, ActionType.SLIDER_DECREMENT);
            }
            current = value;
            gui.getDisplay().repaint();
        }
    }

    @Override
    public void onUpdate(Renderer renderer) {
        super.onUpdate(renderer);
        Theme theme = gui.getTheme();
        float progress = current / max;

        renderer.drawRoundedRect(pos, width, height, cornerRadius, theme.sliders.background);
        renderer.drawRoundedRect(pos.clone().add(margin, margin), (width - margin * 2) * progress, height - margin * 2, cornerRadius, theme.sliders.fill);

        float radius = height;
        renderer.drawCircle(pos.clone().add((width - margin * 2) * progress - radius / 2.0f, (height - radius) / 2.0f), radius, theme.sliders.selectorBorder);
        renderer.drawCircle(pos.clone().add((width - margin * 2) * progress - radius / 2.0f + 1, (height - radius) / 2.0f + 1), radius - 2, theme.sliders.selector);
    }

    public void increment(float increment) {
        setCurrent(current + increment);
        gui.actionPerformed(this, ActionType.SLIDER_INCREMENT);
    }

    public void decrement(float decrement) {
        increment(-decrement);
        gui.actionPerformed(this, ActionType.SLIDER_DECREMENT);
    }

    public void setCurrent(float current) {
        this.current = Math.max(min, Math.min(max, current));
        gui.getDisplay().repaint();
    }

}
