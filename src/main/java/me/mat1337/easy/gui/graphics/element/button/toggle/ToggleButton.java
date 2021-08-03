package me.mat1337.easy.gui.graphics.element.button.toggle;

import lombok.Getter;
import me.mat1337.easy.gui.graphics.Renderer;
import me.mat1337.easy.gui.graphics.element.button.Button;
import me.mat1337.easy.gui.graphics.theme.Theme;
import me.mat1337.easy.gui.util.Constraint;
import me.mat1337.easy.gui.util.vec.Vec2;

@Getter
public class ToggleButton extends Button {

    private boolean toggled;

    public ToggleButton(String label, Constraint constraint, int width, int height) {
        super(label, constraint, width, height);
        this.toggled = false;
    }

    @Override
    public void onClicked(Vec2 mouse, int button) {
        toggle();
        gui.getDisplay().repaint();
    }

    @Override
    public void onUpdate(Renderer renderer) {
        this.pos = translate();

        Theme theme = gui.getTheme();

        renderer.drawRoundedRect(pos, width, height, cornerRadius, isHovered ? theme.buttons.hover : toggled ? theme.toggleButtons.toggled : theme.toggleButtons.background);
        renderer.drawCenteredString(label, pos.clone().add(width / 2.0f, height / 2.0f), theme.toggleButtons.label);
    }

    public void toggle() {
        toggle(!toggled);
    }

    public void toggle(boolean toggled) {
        if (this.toggled == toggled) {
            return;
        }
        this.toggled = toggled;
    }

}
