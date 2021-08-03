package me.mat1337.easy.gui.graphics.element.button;

import lombok.Getter;
import lombok.Setter;
import me.mat1337.easy.gui.graphics.Renderer;
import me.mat1337.easy.gui.graphics.element.Element;
import me.mat1337.easy.gui.graphics.theme.Theme;
import me.mat1337.easy.gui.util.Constraint;
import me.mat1337.easy.gui.util.vec.Vec2;

@Getter
public class Button extends Element {

    @Setter
    protected String label;

    @Setter
    protected float cornerRadius;
    protected boolean isBeingClicked;

    public Button(String label, Constraint constraint, int width, int height) {
        super(constraint, width, height);
        this.label = label;
        this.cornerRadius = 5f;
        this.isBeingClicked = false;
    }

    @Override
    public void onClick(Vec2 mouse, int button) {
        isBeingClicked = true;
        gui.getDisplay().repaint();
    }

    @Override
    public void onClicked(Vec2 mouse, int button) {
        isBeingClicked = false;
        gui.getDisplay().repaint();
    }

    @Override
    public void onUpdate(Renderer renderer) {
        super.onUpdate(renderer);

        Theme theme = gui.getTheme();

        renderer.drawRoundedRect(pos, width, height, cornerRadius, isBeingClicked ? theme.buttons.click : isHovered ? theme.buttons.hover : gui.getTheme().buttons.background);
        renderer.drawCenteredString(label, pos.clone().add(width / 2.0f, height / 2.0f), theme.buttons.label);
    }

}
