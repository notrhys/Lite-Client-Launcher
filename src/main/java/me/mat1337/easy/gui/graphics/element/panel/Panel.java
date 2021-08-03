package me.mat1337.easy.gui.graphics.element.panel;

import lombok.Getter;
import me.mat1337.easy.gui.graphics.Renderer;
import me.mat1337.easy.gui.graphics.element.Element;
import me.mat1337.easy.gui.graphics.element.button.Button;
import me.mat1337.easy.gui.graphics.element.button.dropdown.DropDown;
import me.mat1337.easy.gui.graphics.element.button.toggle.ToggleButton;
import me.mat1337.easy.gui.graphics.element.checkbox.CheckBox;
import me.mat1337.easy.gui.graphics.element.input.InputField;
import me.mat1337.easy.gui.graphics.element.label.Label;
import me.mat1337.easy.gui.graphics.element.progressbar.ProgressBar;
import me.mat1337.easy.gui.graphics.element.slider.Slider;
import me.mat1337.easy.gui.util.Constraint;
import me.mat1337.easy.gui.util.manager.Manager;

@Getter
public class Panel extends Element {

    protected Manager<Element> manager;

    public Panel(Constraint constraint, int width, int height) {
        super(constraint, width, height);
        this.manager = new Manager<>();
    }

    @Override
    public void onUpdate(Renderer renderer) {
        manager.forEach(element -> element.onUpdate(renderer));
    }

    public int getContentHeight() {
        return getItemHeight(manager.size() - 1);
    }

    public int getItemHeight(int index) {
        Element element = manager.at(Math.max(0, Math.min(manager.size() - 1, index)));
        return (int) ((element.getPos().y + element.height + (manager.at(0).getPos().y - pos.y)) - pos.y);
    }

    public Label label(String text, Constraint constraint) {
        Label label = new Label(text, constraint);
        label.setGui(gui);
        label.parent = this;
        manager.push(label);
        return label;
    }

    public Button button(String label, int width, int height, Constraint constraint) {
        Button button = new Button(label, constraint, width, height);
        button.setGui(gui);
        button.parent = this;
        manager.push(button);
        return button;
    }

    public ToggleButton toggleButton(String label, int width, int height, Constraint constraint) {
        ToggleButton button = new ToggleButton(label, constraint, width, height);
        button.setGui(gui);
        button.parent = this;
        manager.push(button);
        return button;
    }

    public DropDown dropDown(int width, int height, Constraint constraint) {
        DropDown dropDown = new DropDown("", constraint, width, height);
        dropDown.setGui(gui);
        dropDown.parent = this;
        manager.push(dropDown);
        return dropDown;
    }

    public ProgressBar progressBar(int width, int height, float min, float max, float current, Constraint constraint) {
        ProgressBar progressBar = new ProgressBar(constraint, width, height, min, max, current);
        progressBar.setGui(gui);
        progressBar.parent = this;
        manager.push(progressBar);
        return progressBar;
    }

    public Slider slider(int width, int height, float min, float max, float current, Constraint constraint) {
        Slider slider = new Slider(constraint, width, height, min, max, current);
        slider.setGui(gui);
        slider.parent = this;
        manager.push(slider);
        return slider;
    }

    public CheckBox checkbox(int width, int height, Constraint constraint) {
        CheckBox checkBox = new CheckBox(constraint, width, height);
        checkBox.setGui(gui);
        checkBox.parent = this;
        manager.push(checkBox);
        return checkBox;
    }

    public InputField inputField(int width, int height, Constraint constraint) {
        InputField inputField = new InputField(constraint, width, height);
        inputField.setGui(gui);
        inputField.parent = this;
        manager.push(inputField);
        return inputField;
    }

}
