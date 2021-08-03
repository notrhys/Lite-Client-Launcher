package me.mat1337.easy.gui.graphics.element.progressbar;

import lombok.Getter;
import lombok.Setter;
import me.mat1337.easy.gui.graphics.Renderer;
import me.mat1337.easy.gui.graphics.element.Element;
import me.mat1337.easy.gui.graphics.theme.Theme;
import me.mat1337.easy.gui.util.Constraint;
import me.mat1337.easy.gui.util.MathUtil;
import me.mat1337.easy.gui.util.action.ActionType;

@Getter
public class ProgressBar extends Element {

    private float min;
    private float max;

    private float current;

    @Setter
    private float cornerRadius;

    @Setter
    private int margin;

    @Setter
    private boolean showLabel;

    public ProgressBar(Constraint constraint, int width, int height, float min, float max, float current) {
        super(constraint, width, height);
        this.min = min;
        this.max = max;
        this.current = current;
        this.cornerRadius = 5f;
        this.margin = 2;
        this.showLabel = true;
    }

    @Override
    public void onUpdate(Renderer renderer) {
        super.onUpdate(renderer);

        Theme theme = gui.getTheme();
        float progress = current / max;

        renderer.drawRoundedRect(pos, width, height, cornerRadius, theme.progressBars.background);
        renderer.drawRoundedRect(pos.clone().add(margin, margin), (width - margin * 2) * progress, height - margin * 2, cornerRadius, theme.progressBars.fill);
        if (showLabel) {
            renderer.drawCenteredString(MathUtil.trimFloat(2, (progress * 100.0f)) + "%", pos.clone().add(width / 2.0f, height / 2.0f), theme.progressBars.label);
        }
    }

    public void increment(float increment) {
        setCurrent(current + increment);
        gui.actionPerformed(this, ActionType.PROGRESS_BAR_INCREMENT);
    }

    public void decrement(float decrement) {
        increment(-decrement);
        gui.actionPerformed(this, ActionType.PROGRESS_BAR_DECREMENT);
    }

    public void setCurrent(float current) {
        this.current = Math.max(min, Math.min(max, current));
        gui.getDisplay().repaint();
    }

}
