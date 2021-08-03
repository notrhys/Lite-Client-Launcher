package me.mat1337.easy.gui;

import lombok.Getter;
import lombok.Setter;
import me.mat1337.easy.gui.graphics.Display;
import me.mat1337.easy.gui.graphics.Renderer;
import me.mat1337.easy.gui.graphics.element.Element;
import me.mat1337.easy.gui.graphics.element.button.Button;
import me.mat1337.easy.gui.graphics.element.button.dropdown.DropDown;
import me.mat1337.easy.gui.graphics.element.button.toggle.ToggleButton;
import me.mat1337.easy.gui.graphics.element.checkbox.CheckBox;
import me.mat1337.easy.gui.graphics.element.input.InputField;
import me.mat1337.easy.gui.graphics.element.label.Label;
import me.mat1337.easy.gui.graphics.element.panel.Panel;
import me.mat1337.easy.gui.graphics.element.progressbar.ProgressBar;
import me.mat1337.easy.gui.graphics.element.slider.Slider;
import me.mat1337.easy.gui.graphics.theme.Theme;
import me.mat1337.easy.gui.graphics.theme.impl.DarkTheme;
import me.mat1337.easy.gui.input.Mouse;
import me.mat1337.easy.gui.util.Constraint;
import me.mat1337.easy.gui.util.action.ActionHandler;
import me.mat1337.easy.gui.util.action.ActionType;
import me.mat1337.easy.gui.util.logger.Logger;
import me.mat1337.easy.gui.util.manager.Manager;
import me.mat1337.easy.gui.util.vec.Vec2;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class EasyGui extends Canvas {

    public static final Logger LOGGER = new Logger("main");

    public static int EASY_ID = 0;

    private int initialWidth;
    private int initialHeight;

    // Display declaration
    private Display display;
    private Mouse mouse;
    private Theme theme;
    private Panel rootPanel;

    @Setter
    private Font font;

    private Manager<ActionHandler> handlers;

    // in milliseconds
    private long creationTime;

    public EasyGui(String title, int width, int height, Theme theme) {
        this.creationTime = System.currentTimeMillis();
        this.setMinimumSize(null);
        this.setMaximumSize(null);

        this.initialWidth = width;
        this.initialHeight = height;
        this.mouse = new Mouse(this);
        this.theme = theme;
        this.font = loadTTFFont("", 15);
        this.handlers = new Manager<>();
        this.rootPanel = new Panel(new Constraint(new Vec2(0, 0)), 0, 0);
        this.rootPanel.setGui(this);

        this.display = new Display(title, width, height);
        this.display.pack(this);

        this.setRadius(15);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> actionPerformed(null, ActionType.WINDOW_CLOSE)));

        EASY_ID++;
    }

    public EasyGui(int width, int height, Theme theme) {
        this("Easy-Gui Application", width, height, theme);
    }

    public EasyGui(String title, int width, int height) {
        this(title, width, height, new DarkTheme());
    }

    public EasyGui(int width, int height) {
        this("Easy-Gui Application", width, height, new DarkTheme());
    }

    /**
     * Handles all of the actions
     * for the elements in the gui
     *
     * @param element that triggered the action
     * @param type    the type of action that was triggered
     */

    public void actionPerformed(Element element, ActionType type) {
        handlers.forEach(actionHandler -> actionHandler.actionPerformed(element, type));
    }

    /**
     * Used for updating all the
     * gui elements
     *
     * @param renderer used for drawing on the screen
     * @param pos      the default starting position of all elements
     * @param width    max width for every element
     * @param height   max height of every element
     */

    public void updateGui(Renderer renderer, Vec2 pos, int width, int height) {
        this.rootPanel.setPos(pos);
        this.rootPanel.width = width;
        this.rootPanel.height = height;

        rootPanel.onUpdate(renderer);
    }

    /**
     * Get's triggered when the
     * mouse scrolls the mouse wheel
     *
     * @param pos           the position of the mouse
     * @param wheelRotation the rotation direction
     * @param scrollAmount  the amount that mouse has scrolled
     */

    public void onMouseScroll(Vec2 pos, double wheelRotation, int scrollAmount) {
        Vec2 scaledPos = display.scale(pos);
        rootPanel.getManager().filter(element -> element.isHovered).forEach(element -> {
            actionPerformed(element, ActionType.ELEMENT_SCROLL);
            element.onMouseScroll(scaledPos, wheelRotation, scrollAmount);
        });
    }

    /**
     * Get's triggered when the
     * mouse has dragged over an
     * element
     *
     * @param pos position of the mouse
     */

    public void onMouseDrag(Vec2 pos) {
        Vec2 scaledPos = display.scale(pos);
        rootPanel.getManager().filter(element -> element.isHovered).forEach(element -> {
            actionPerformed(element, ActionType.ELEMENT_DRAG);
            element.onDragged(scaledPos);
        });
    }

    /**
     * Get's triggered when mouse has been
     * moved on the screen
     *
     * @param pos position that it moved to
     */

    public void onMouseMove(Vec2 pos) {
        Vec2 scaledPos = display.scale(pos);

        AtomicBoolean repaintFlag = new AtomicBoolean(false);
        rootPanel.getManager().forEach(element -> {
            boolean state = element.isHovered(scaledPos);
            if (state && !element.isHovered) {
                element.onMouseEnter(scaledPos);
                actionPerformed(element, ActionType.MOUSE_ENTER);
                repaintFlag.set(true);
            } else if (!state && element.isHovered) {
                element.onMouseLeave(scaledPos);
                actionPerformed(element, ActionType.MOUSE_LEAVE);
                repaintFlag.set(true);
            }
            element.isHovered = state;
            element.onMouseMoved(scaledPos);
        });
        if (repaintFlag.get()) {
            display.repaint();
        }
    }

    /**
     * Called when the click is
     * first initiated
     *
     * @param pos    mouse position
     * @param button mouse button
     */

    public void onMouseClick(Vec2 pos, int button) {
        Vec2 scaledPos = display.scale(pos);
        rootPanel.getManager().filter(element -> element.isHovered).forEach(element -> element.onClick(scaledPos, button));

        AtomicBoolean dropDownClicked = new AtomicBoolean(false);
        rootPanel.getManager().forEach(element -> {
            if (element instanceof DropDown) {
                DropDown dropDown = (DropDown) element;
                if (dropDown.isHovered(pos)) {
                    dropDownClicked.set(true);
                }
            }
        });

        if (!dropDownClicked.get()) {
            rootPanel.getManager().filter(element -> element instanceof DropDown).forEach(element -> ((DropDown) element).setExpanded(false));
        }
    }

    /**
     * Called whenever a mouse has released
     * its click
     *
     * @param pos    mouse position
     * @param button mouse button
     */

    public void onMouseReleased(Vec2 pos, int button) {
        Vec2 scaledPos = display.scale(pos);

        rootPanel.getManager().filter(element -> element.isHovered).forEach(element -> {
            actionPerformed(element, button == MouseEvent.BUTTON1 ? ActionType.LEFT_CLICKED : ActionType.RIGHT_CLICKED);
            element.onClicked(scaledPos, button);
        });
    }

    /**
     * Get's triggered whenever application is focused
     * and a key has been pressed
     *
     * @param keyChar the character from the key press
     * @param keyCode the key code from the key press
     */

    public void onKeyPress(char keyChar, int keyCode) {
        rootPanel.getManager().forEach(element -> element.onKeyPress(keyChar, keyCode));
    }

    /**
     * Get's triggered whenever application is focused
     * and a key has been released
     *
     * @param keyChar the character from the key release
     * @param keyCode the key code from the key release
     */

    public void onKeyRelease(char keyChar, int keyCode) {
    }

    /**
     * Set the roundness of the window's corners
     * <p>
     * > default = 0
     *
     * @param radius the corner radius
     */

    public void setRadius(float radius) {
        LOGGER.info("Corner radius updated to: %s", radius);
        display.setCornerRadius(radius);
    }

    /**
     * The border that get's rendered
     * around the application in the
     * base color
     * <p>
     * > default = 2
     *
     * @param radius the border radius
     */

    public void setBorderRadius(int radius) {
        LOGGER.info("Border radius updated to: $s", radius);
        display.setBorderRadius(radius);
    }

    /**
     * Make the window resizable or not
     * <p>
     * > default = true
     *
     * @param resizable the value
     */

    public void setResizable(boolean resizable) {
        if (resizable) {
            LOGGER.info("Application is now resizable");
        } else {
            LOGGER.info("Application is not longer resizable");
        }
        display.setResizable(resizable);
    }

    /**
     * Set's the window's title to the
     * one that you provide
     *
     * @param title the actual title want it as
     */

    public void setTitle(String title) {
        display.setTitle(title);
    }

    /**
     * Sets the size of the window
     * the the given arguments
     *
     * @param size of the window
     */

    @Override
    public void setSize(Dimension size) {
        setSize(size.width, size.height);
    }

    /**
     * Sets the size of the window
     * the the given arguments
     *
     * @param width  of the window
     * @param height of the window
     */

    @Override
    public void setSize(int width, int height) {
        display.setSize(width, height);
    }

    /**
     * Sets the location of the display
     *
     * @param point the location on the screen
     */

    @Override
    public void setLocation(Point point) {
        display.setLocation(point);
    }

    /**
     * Sets the location of the display
     *
     * @param x the location on the screen (X)
     * @param y the location on the screen (Y)
     */

    @Override
    public void setLocation(int x, int y) {
        setLocation(new Point(x, y));
    }

    /**
     * Load's a ttf font from a file
     * and return's it as an font object
     *
     * @param path location of the font
     * @param size the size that you want it to be
     * @return the font object
     */

    public Font loadTTFFont(String path, int size) {
        Font font;
        try {
            InputStream var3 = EasyGui.class.getResourceAsStream(path);
            font = Font.createFont(0, var3);
            font = font.deriveFont(Font.PLAIN, (float) size);
        } catch (Exception var31) {
            EasyGui.LOGGER.warn("Failed to load font, using the fallback font");
            font = new Font("Arial", Font.PLAIN, size);
        }
        return font;
    }

    /**
     * Register's a action handler
     * so it can receive actions
     *
     * @param handler the handler that you want to register
     */

    public void registerActionHandler(ActionHandler handler) {
        handlers.push(handler);
    }

    /**
     * Show or hide the window
     * based on the visible boolean
     *
     * @param visible true will show the window and false will hide the window
     */

    @Override
    public void setVisible(boolean visible) {
        display.setVisible(visible);
    }

    /**
     * Add's a label to the gui
     * and applies given constraints
     * on the element
     *
     * @param text       for the label to display
     * @param constraint used for position the element on the screen
     * @return the element itself for later usage
     */

    public Label label(String text, Constraint constraint) {
        return rootPanel.label(text, constraint);
    }

    /**
     * Add's a button to the gui
     * and applies given constraints
     * on the element
     *
     * @param label      the label of the button that get's rendered
     * @param width      width of the element
     * @param height     height of the element
     * @param constraint used for position the element on the screen
     * @return the element itself for later usage
     */

    public Button button(String label, int width, int height, Constraint constraint) {
        return rootPanel.button(label, width, height, constraint);
    }

    /**
     * Add's a toggle button to the gui
     * and applies given constraints
     * on the element
     *
     * @param label      the label of the button that get's rendered
     * @param width      width of the element
     * @param height     height of the element
     * @param constraint used for position the element on the screen
     * @return the element itself for later usage
     */

    public ToggleButton toggleButton(String label, int width, int height, Constraint constraint) {
        return rootPanel.toggleButton(label, width, height, constraint);
    }

    /**
     * Add's a progressbar to the gui
     * and applies given constraints
     * on the element
     *
     * @param width      width of the element
     * @param height     height of the element
     * @param min        min value for the progressbar
     * @param max        max value for the progressbar
     * @param current    current value of the progressbar
     * @param constraint used for position the element on the screen
     * @return the element itself for later usage
     */

    public ProgressBar progressBar(int width, int height, float min, float max, float current, Constraint constraint) {
        return rootPanel.progressBar(width, height, min, max, current, constraint);
    }

    /**
     * Add's a slider to the gui
     * and applies given constraints
     * on the element
     *
     * @param width      width of the element
     * @param height     height of the element
     * @param min        min value for the progressbar
     * @param max        max value for the progressbar
     * @param current    current value of the progressbar
     * @param constraint used for position the element on the screen
     * @return the element itself for later usage
     */

    public Slider slider(int width, int height, float min, float max, float current, Constraint constraint) {
        return rootPanel.slider(width, height, min, max, current, constraint);
    }

    /**
     * Add's a checkbox to the gui
     * and applies given constraints
     * on the element
     *
     * @param width      of the element
     * @param height     of the element
     * @param constraint used for position the element on the screen
     * @return the element itself for later usage
     */

    public CheckBox checkbox(int width, int height, Constraint constraint) {
        return rootPanel.checkbox(width, height, constraint);
    }

    /**
     * Add's a dropdown to the gui
     * and applies given constraints
     * on the element
     *
     * @param width      of the element
     * @param height     of the element
     * @param constraint used for position the element on the screen
     * @return the element itself for later usage
     */

    public DropDown dropDown(int width, int height, Constraint constraint) {
        return rootPanel.dropDown(width, height, constraint);
    }

    public InputField inputField(int width, int height, Constraint constraint) {
        return rootPanel.inputField(width, height, constraint);
    }

}
