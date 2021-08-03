package me.mat1337.easy.gui.graphics.element.input;

import lombok.Getter;
import lombok.Setter;
import me.mat1337.easy.gui.graphics.Renderer;
import me.mat1337.easy.gui.graphics.element.Element;
import me.mat1337.easy.gui.input.Keyboard;
import me.mat1337.easy.gui.util.ChatAllowedCharacters;
import me.mat1337.easy.gui.util.ColorUtil;
import me.mat1337.easy.gui.util.Constraint;

import java.awt.*;

public class InputField extends Element {

    private static final Color SHADOW_COLOR = new Color(100, 100, 100);

    @Getter
    @Setter
    private String text;

    @Setter
    private String placeHolder;

    @Getter
    @Setter
    private boolean isSelected;

    @Setter
    private boolean isPassword;

    private int undoSpeed;

    public InputField(Constraint constraint, int width, int height) {
        super(constraint, width, height);
        this.text = "";
        this.placeHolder = "";
        this.isSelected = false;
        this.undoSpeed = 150;
    }

    @Override
    public void onKeyPress(char keyChar, int keyCode) {
        if (isSelected) {
            if (keyCode == Keyboard.KEY_BACK_SPACE) {
                undoText();
            } else {
                if (Keyboard.isKeyDown(Keyboard.KEY_CONTROL) && keyCode == Keyboard.KEY_V) {
                    /*String clipboard = SystemUtil.getClipBoard();
                    if (!clipboard.isEmpty()) {
                        text += clipboard;
                    }*/
                } else {
                    if (isValidKey(keyCode)) {
                        if (ChatAllowedCharacters.isAllowedCharacter(keyChar)) {
                            text += keyChar;
                        }
                    }
                }
            }
            gui.getDisplay().repaint();
        }
    }

    int i = 0;
    long lastTime = -1;

    @Override
    public void onUpdate(Renderer renderer) {
        super.onUpdate(renderer);

        this.isHovered = isHovered(renderer.getMousePos());

        if (Keyboard.isKeyDown(Keyboard.KEY_BACK_SPACE) && isSelected) {
            if (lastTime == -1) lastTime = System.currentTimeMillis();
            i++;
            if (i % ((System.currentTimeMillis() - lastTime >= 1000) ? undoSpeed / 10 : undoSpeed) == 0) {
                undoText();
            }
            gui.getDisplay().repaint();
        } else {
            i = 0;
            lastTime = -1;
        }

        renderer.drawRoundedRect(pos.x, pos.y, width, height, 10, ColorUtil.darken(gui.getTheme().background, 25));
        renderer.drawOutlineRoundedRect(pos.x, pos.y, width, height, 10, ColorUtil.darken(gui.getTheme().background, 15));

        if (!placeHolder.isEmpty() && text.isEmpty() && !isSelected) {
            renderer.drawCenteredStringWithShadow(placeHolder, pos.clone().add(width / 2f, height / 2f), SHADOW_COLOR);
        }

        renderer.pushGraphics();
        renderer.clip(pos, width, height);
        if (isSelected || !text.isEmpty()) {
            StringBuilder str = new StringBuilder(text);
            if (isPassword) {
                str = new StringBuilder();
                for (int i = 0; i < text.length(); i++) {
                    str.append("*");
                }
            }
            String label = str.toString();

            float lblWidth = renderer.getStringWidth(label) + 5;

            float xMove = 0;
            if (lblWidth >= width) {
                xMove = lblWidth - width + 5;
            }
            renderer.drawStringWithShadow(label, pos.clone().add(5 - xMove, height / 2f + 5), Color.WHITE);
        }
        renderer.popGraphics();
    }

    private void undoText() {
        if (text.length() - 1 >= 0) {
            text = text.substring(0, text.length() - 1);
        }
    }

    private boolean isValidKey(int keyCode) {
        if (keyCode == Keyboard.KEY_ENTER
                || keyCode == Keyboard.KEY_SHIFT
                || keyCode == Keyboard.KEY_CAPS_LOCK
                || keyCode == Keyboard.KEY_TAB
                || keyCode == Keyboard.KEY_RIGHT
                || keyCode == Keyboard.KEY_LEFT
                || keyCode == Keyboard.KEY_UP
                || keyCode == Keyboard.KEY_DOWN
                || keyCode == Keyboard.KEY_INSERT
                || keyCode == Keyboard.KEY_HOME
                || keyCode == Keyboard.KEY_PAGE_UP
                || keyCode == Keyboard.KEY_PAGE_DOWN
                || keyCode == Keyboard.KEY_DELETE
                || keyCode == Keyboard.KEY_END
                || keyCode == Keyboard.KEY_NUM_LOCK
                || keyCode == Keyboard.KEY_SCROLL_LOCK
                || keyCode == Keyboard.KEY_PAUSE
                || keyCode == Keyboard.KEY_PRINTSCREEN
                || keyCode == Keyboard.KEY_F1
                || keyCode == Keyboard.KEY_F2
                || keyCode == Keyboard.KEY_F3
                || keyCode == Keyboard.KEY_F4
                || keyCode == Keyboard.KEY_F5
                || keyCode == Keyboard.KEY_F6
                || keyCode == Keyboard.KEY_F7
                || keyCode == Keyboard.KEY_F8
                || keyCode == Keyboard.KEY_F9
                || keyCode == Keyboard.KEY_F10
                || keyCode == Keyboard.KEY_F11
                || keyCode == Keyboard.KEY_F12
                || keyCode == Keyboard.KEY_CONTEXT_MENU
                || keyCode == 12
                || keyCode == 17
                || keyCode == 18
                || keyCode == 524
        )
            return false;
        return true;
    }

}
