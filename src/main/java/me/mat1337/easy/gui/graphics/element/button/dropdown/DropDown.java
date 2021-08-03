package me.mat1337.easy.gui.graphics.element.button.dropdown;

import me.mat1337.easy.gui.EasyGui;
import me.mat1337.easy.gui.graphics.Renderer;
import me.mat1337.easy.gui.graphics.element.button.Button;
import me.mat1337.easy.gui.graphics.element.label.Label;
import me.mat1337.easy.gui.graphics.element.panel.ScrollPanel;
import me.mat1337.easy.gui.graphics.theme.Theme;
import me.mat1337.easy.gui.util.Constraint;
import me.mat1337.easy.gui.util.action.ActionType;
import me.mat1337.easy.gui.util.vec.Vec2;

import java.util.concurrent.atomic.AtomicInteger;

public class DropDown extends Button {

    private ScrollPanel panel;

    // in items
    private int dropHeight;
    private int forceRepaints;

    private boolean isExpanded;
    private boolean isHeaderHovered;
    private boolean isBodyHovered;

    public DropDown(String label, Constraint constraint, int width, int height) {
        super(label, constraint, width, height);
        this.dropHeight = 3;
        this.isExpanded = false;
        this.isHeaderHovered = false;
        this.isBodyHovered = false;
        this.forceRepaints = 0;
        this.panel = new ScrollPanel(new Constraint(new Vec2()), width, height);
    }

    @Override
    public void onMouseScroll(Vec2 pos, double wheelRotation, int scrollAmount) {
        panel.onMouseScroll(pos, wheelRotation, scrollAmount);
    }

    @Override
    public void onMouseMoved(Vec2 mouse) {
        if (panel.isHovered(mouse)) {
            panel.getManager().forEach(element -> {
                boolean isHovered = element.isHovered(mouse.clone().add(0, panel.getScrollIndex()));
                if (element.isHovered != isHovered) {
                    element.isHovered = isHovered;
                    gui.getDisplay().repaint();
                }
            });
        }
    }

    public void addItem(String item) {
        Item tmp = new Item(item, new Constraint(new Vec2(0f, 0f)));
        tmp.setGui(gui);
        tmp.parent = panel;

        if (panel.getManager().isEmpty()) {
            setLabel(tmp.getText());
        }

        panel.getManager().push(tmp);
    }

    public void removeItem(String item) {
        panel.getManager().removeIf(element -> ((element instanceof Label) && ((Label) element).getText().equals(item)));
    }

    @Override
    public void onClick(Vec2 mouse, int button) {
        if (!isBodyHovered) {
            isBeingClicked = true;
            gui.getDisplay().repaint();
        }
    }

    @Override
    public void onClicked(Vec2 mouse, int button) {
        super.onClicked(mouse, button);
        if (super.isHovered(mouse)) {
            setExpanded(!isExpanded);
            if (isExpanded) {
                forceRepaints = (int) Math.pow(2, dropHeight);
            }
        }
        if (isBodyHovered) {
            panel.getManager().filter(element -> element.isHovered).forEach(element -> {
                if (element instanceof DropDown.Item) {
                    panel.resetScroll();
                    setLabel(((Item) element).getText());
                    setExpanded(false);
                    gui.actionPerformed(this, ActionType.DROPDOWN_ITEM_SELECT);
                }
            });
        }
    }

    @Override
    public void onUpdate(Renderer renderer) {
        this.pos = translate();

        Theme theme = gui.getTheme();

        renderer.drawRoundedRect(pos, width, height, cornerRadius, isBeingClicked ? theme.dropDowns.click : isHeaderHovered ? theme.dropDowns.hover : theme.dropDowns.background);
        renderer.drawCenteredString(label, pos.clone().add(width / 2.0f, height / 2.0f), theme.dropDowns.label);

        int dropHeight = panel.getItemHeight(Math.max(0, Math.min(panel.getManager().size(), this.dropHeight)));

        panel.setPos(new Vec2(pos.x, pos.y + height + 1));
        panel.width = width;
        panel.height = dropHeight;

        if (isExpanded) {
            renderer.drawRoundedRect(panel.getPos(), width, dropHeight, cornerRadius, theme.dropDowns.background);

            AtomicInteger offset = new AtomicInteger();
            panel.getManager().forEach(element -> element.constraint = new Constraint(new Vec2(0.5f, 0)).offset(0, offset.getAndAdd(element.height + panel.getItemMargin())));

            panel.onUpdate(renderer);
        }

        if (forceRepaints > 0) {
            gui.getDisplay().repaint();
            forceRepaints--;
        }
    }

    @Override
    public boolean isHovered(Vec2 mouse) {
        boolean isHeaderHovered = super.isHovered(mouse);
        boolean isBodyHovered = super.isHovered(pos.clone().add(0, height), width, panel.getItemHeight(Math.max(0, Math.min(panel.getManager().size(), this.dropHeight))), mouse);

        if (this.isHeaderHovered != isHeaderHovered) {
            gui.getDisplay().repaint();
        }
        if (this.isBodyHovered != isBodyHovered) {
            gui.getDisplay().repaint();
        }

        this.isHeaderHovered = isHeaderHovered;
        this.isBodyHovered = isBodyHovered;

        return isExpanded ? (isBodyHovered || isHeaderHovered) : isHeaderHovered;
    }

    public void setExpanded(boolean expanded) {
        if (this.isExpanded == expanded) {
            return;
        }
        this.isExpanded = expanded;
        gui.getDisplay().repaint();
    }

    @Override
    public void setGui(EasyGui gui) {
        panel.setGui(gui);
        super.setGui(gui);
    }

    public class Item extends Label {

        public Item(String text, Constraint constraint) {
            super(text, constraint);
        }

        @Override
        public void onUpdate(Renderer renderer) {
            this.width = (int) renderer.getStringWidth(text);
            this.height = (int) renderer.getStringHeight(text);

            this.pos = translate().clone().add(width / 4.0f + 5, -(height / 2.0f));

            Theme theme = gui.getTheme();
            renderer.drawString(text, pos.clone().add(0, height - height / 4.0f), Item.this.isHovered ? theme.dropDowns.itemHover : theme.dropDowns.label);
        }

    }

}
