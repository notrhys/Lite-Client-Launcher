package me.mat1337.easy.gui.graphics.element.checkbox;

import lombok.Getter;
import me.mat1337.easy.gui.graphics.Renderer;
import me.mat1337.easy.gui.graphics.element.Element;
import me.mat1337.easy.gui.graphics.theme.Theme;
import me.mat1337.easy.gui.util.Constraint;
import me.mat1337.easy.gui.util.action.ActionType;
import me.mat1337.easy.gui.util.vec.Vec2;

@Getter
public class CheckBox extends Element {

    private boolean checked;
    private float radius;

    public CheckBox(Constraint constraint, int width, int height) {
        super(constraint, width, height);
        this.checked = false;
        this.radius = 4;
    }

    @Override
    public void onClicked(Vec2 mouse, int button) {
        setChecked(!checked);
        if (checked) {
            gui.actionPerformed(this, ActionType.CHECKBOX_CHECK);
        } else {
            gui.actionPerformed(this, ActionType.CHECKBOX_UNCHECK);
        }
    }

    @Override
    public void onUpdate(Renderer renderer) {
        super.onUpdate(renderer);

        Theme theme = gui.getTheme();

        renderer.drawRoundedRect(pos, width, height, radius, theme.checkBoxes.border);
        renderer.drawRoundedRect(pos.clone().add(1, 1), width - 2, height - 2, radius, theme.checkBoxes.background);

        if (checked) {
            renderer.drawRoundedRect(pos.clone().add(3, 3), width - 6, height - 6, radius, theme.checkBoxes.fill);
        }
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        gui.getDisplay().repaint();
    }

    public void setRadius(float radius) {
        this.radius = radius;
        gui.getDisplay().repaint();
    }

}
