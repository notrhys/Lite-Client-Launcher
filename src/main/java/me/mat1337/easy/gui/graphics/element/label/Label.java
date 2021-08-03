package me.mat1337.easy.gui.graphics.element.label;

import lombok.Getter;
import lombok.Setter;
import me.mat1337.easy.gui.graphics.Renderer;
import me.mat1337.easy.gui.graphics.element.Element;
import me.mat1337.easy.gui.util.Constraint;

import java.awt.*;

@Getter
public class Label extends Element {

    protected String text;

    @Setter
    protected Color textColor;

    public Label(String text, Constraint constraint) {
        super(constraint);
        this.text = text;
        this.textColor = null;
    }

    @Override
    public void onUpdate(Renderer renderer) {
        this.width = (int) renderer.getStringWidth(text);
        this.height = (int) renderer.getStringHeight(text);

        this.pos = translate();

        renderer.drawString(text, pos.clone().add(0, height - height / 4.0f), textColor != null ? textColor : gui.getTheme().labels.text);
    }

    public void setText(String text) {
        this.text = text;
        gui.getDisplay().repaint();
    }

}
