package me.mat1337.easy.gui.graphics;

import lombok.Getter;
import lombok.Setter;
import me.mat1337.easy.gui.EasyGui;
import me.mat1337.easy.gui.input.Keyboard;
import me.mat1337.easy.gui.util.action.ActionType;
import me.mat1337.easy.gui.util.vec.Vec2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;

public class Display {

    private JFrame frame;
    private Vec2 mousePos;
    private GraphicsPanel panel;
    private EasyGui gui;

    /**
     * Toolbar
     */

    private int toolBarFixedHeight;

    /**
     * Used to check the button id's
     * (mouse drag has broken mouse button id's so i use the click instead)
     */
    private boolean locked;

    /**
     * The amount of pixels from each side
     * that can get grabbed to size the window
     */
    @Getter
    @Setter
    private int resizeRadius;

    /**
     * Size of the border around the
     * application with the base color
     */

    @Getter
    @Setter
    private int borderRadius;

    /**
     * Initial width of the display
     */

    @Getter
    private int width;

    /**
     * Initial height of the display
     */

    @Getter
    private int height;

    /**
     * ToolBar button properties
     */

    private Vec2 closePos;
    private Vec2 maximizePos;
    private Vec2 minimizePos;

    private int closeBtnWidth;
    private int maximizeBtnWidth;
    private int minimizeBtnWidth;
    private int btnHeight;

    private boolean closeBtnHovered;
    private boolean maximizeBtnHovered;
    private boolean minimizeBtnHovered;

    public Display(String title, int width, int height) {
        this.width = width;
        this.height = height;
        this.mousePos = new Vec2();
        this.resizeRadius = 4;
        this.borderRadius = 1;
        this.frame = new JFrame(title);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.setUndecorated(true);
        this.frame.setBackground(new Color(0, 0, 0, 0));
        this.frame.setContentPane(this.panel = new GraphicsPanel(0));

        this.frame.addWindowStateListener(e -> {
            if (e.getNewState() == 1) {
                gui.actionPerformed(null, ActionType.WINDOW_MINIMIZE);
            } else if (e.getNewState() == 0) {
                gui.actionPerformed(null, ActionType.WINDOW_UN_MINIMIZE);
            }
        });
    }

    public void repaint() {
        frame.repaint();
    }

    /**
     * Uses the pre scaled drawing system
     *
     * @param renderer takes care of rendering
     */

    public void updateDisplay(Renderer renderer, int width, int height) {
        Vec2 pos = new Vec2(1, toolBarFixedHeight);
        gui.updateGui(renderer, pos, (width - 2), (int) (height - pos.y - 1));
    }

    /**
     * Pack's the display so it fits the gui
     *
     * @param component the EasyGui object
     */

    public void pack(EasyGui component) {
        this.gui = component;

        this.frame.getContentPane().add(component);

        this.frame.addMouseListener(new CustomMouseAdapter());
        this.frame.addMouseMotionListener(new CustomMouseMotionAdapter());

        this.frame.addKeyListener(new Keyboard(component));
        this.frame.addMouseListener(component.getMouse());
        this.frame.addMouseMotionListener(component.getMouse());
        this.frame.addMouseWheelListener(component.getMouse());

        this.frame.pack();
        this.frame.setSize(width, height);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);

        this.toolBarFixedHeight = 40;
    }

    /**
     * Used to render stuff in the toolbar
     *
     * @param renderer takes care of rendering
     */

    public void drawToolBar(Renderer renderer, int width, int height) {
        renderer.drawString(frame.getTitle(), 20, (toolBarFixedHeight + renderer.getFontHeight()) / 2.0 - 3, Color.WHITE);
        closeBtnWidth = (int) renderer.getStringWidth("X");
        maximizeBtnWidth = (int) renderer.getStringWidth("🗖");
        minimizeBtnWidth = (int) renderer.getStringWidth("-");
        btnHeight = (int) renderer.getFontHeight();
        closePos = new Vec2(width - closeBtnWidth - 20, (float) ((toolBarFixedHeight + renderer.getFontHeight()) / 2.0 - 3));
        renderer.drawString("X", closePos, closeBtnHovered ? Color.GRAY : Color.WHITE);
        renderer.drawString("🗖", maximizePos = closePos.clone().sub(maximizeBtnWidth + 20, 0), maximizeBtnHovered ? Color.GRAY : Color.WHITE);
        renderer.drawString("-", minimizePos = maximizePos.clone().sub(minimizeBtnWidth + 20, 0), minimizeBtnHovered ? Color.GRAY : Color.WHITE);
    }

    public Vec2 scale(Vec2 pos) {
        return pos.clone().div(panel.xScale, panel.yScale);
    }

    public void setCornerRadius(float radius) {
        panel.setRadius(radius);
    }

    public void setResizable(boolean resizable) {
        frame.setResizable(resizable);
    }

    public void setTitle(String title) {
        frame.setTitle(title);
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public String getTitle() {
        return frame.getTitle();
    }

    public void setSize(int width, int height) {
        frame.setSize(width, height);
    }

    public void setLocation(Point point) {
        frame.setLocation(point);
    }

    class CustomMouseAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            mousePos = new Vec2(e.getPoint());
            locked = e.getButton() != MouseEvent.BUTTON1;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (locked) {
                locked = false;
            }
            Vec2 mPos = new Vec2(e.getPoint()).div(panel.getXScale(), panel.getYScale());
            if (mPos.x >= closePos.x && mPos.y >= (closePos.y - btnHeight) && mPos.x <= (closePos.x + closeBtnWidth) && mPos.y <= closePos.y) {
                System.exit(0);
            } else if (mPos.x >= minimizePos.x && mPos.y >= (minimizePos.y - btnHeight) && mPos.x <= (minimizePos.x + minimizeBtnWidth) && mPos.y <= minimizePos.y) {
                frame.setState(Frame.ICONIFIED);
            }
        }

    }

    class CustomMouseMotionAdapter extends MouseMotionAdapter {

        boolean top, bottom, left, right;
        boolean topLeft, topRight, bottomLeft, bottomRight;

        Dimension clampSize(Dimension dimension) {
            Dimension clamped = dimension;
            Dimension minSize = gui.getMinimumSize();
            Dimension maxSize = gui.getMaximumSize();

            if (minSize != null) {
                clamped = new Vec2(clamped).max(minSize.width, minSize.height).toDimension();
            }
            if (maxSize != null) {
                clamped = new Vec2(clamped).min(maxSize.width, maxSize.height).toDimension();
            }
            return clamped;
        }

        Rectangle clampSize(Rectangle bounds, Rectangle resizedRect) {
            Dimension minSize = gui.getMinimumSize();
            Dimension maxSize = gui.getMaximumSize();

            boolean wFlag = resizedRect.width <= minSize.width || resizedRect.width >= maxSize.width;
            boolean hFlag = resizedRect.height <= minSize.height || resizedRect.height >= maxSize.height;

            return new Rectangle(wFlag ? bounds.x : resizedRect.x, hFlag ? bounds.y : resizedRect.y, Math.max(minSize.width, Math.min(maxSize.width, resizedRect.width)), Math.max(minSize.height, Math.min(maxSize.height, resizedRect.height)));
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (locked) {
                return;
            }

            Vec2 mouse = new Vec2(e.getPoint());
            Vec2 location = new Vec2(frame.getLocation());
            Vec2 size = new Vec2(frame.getSize());
            Vec2 diff = new Vec2(e.getPoint()).sub(size);
            Rectangle rect = frame.getBounds();

            if (right) {
                frame.setSize(clampSize(size.clone().add(diff).setY(size.y).toDimension()));
            } else if (left) {
                frame.setBounds(clampSize(rect, new Rectangle((int) (rect.x + mouse.x), rect.y, (int) (rect.width - mouse.x), rect.height)));
            } else if (bottom) {
                frame.setSize(clampSize(size.clone().add(diff).setX(size.x).toDimension()));
            } else if (top) {
                frame.setBounds(clampSize(rect, new Rectangle(rect.x, (int) (rect.y + mouse.y), rect.width, (int) (rect.height - mouse.y))));
            } else if (bottomRight) {
                frame.setSize(clampSize(size.clone().add(diff).toDimension()));
            } else if (topRight) {
                frame.setBounds(clampSize(rect, new Rectangle(rect.x, (int) (rect.y + mouse.y), (int) (rect.width + diff.x), (int) (rect.height - mouse.y))));
            } else if (topLeft) {
                frame.setBounds(clampSize(rect, new Rectangle((int) (rect.x + mouse.x), (int) (rect.y + mouse.y), (int) (rect.width - mouse.x), (int) (rect.height - mouse.y))));
            } else if (bottomLeft) {
                frame.setBounds(clampSize(rect, new Rectangle((int) (rect.x + mouse.x), rect.y, (int) (rect.width - mouse.x), (int) (rect.height + diff.y))));
            } else {
                Vec2 screenPos = panel.screen2world(mousePos);
                if (mousePos.x >= 0 && mousePos.x <= panel.getWidth() && screenPos.y >= 0 && screenPos.y <= toolBarFixedHeight) {
                    diff = mouse.clone().sub(mousePos);
                    Vec2 newLocation = location.clone().add(diff);
                    frame.setLocation(newLocation.toPoint());
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Vec2 mouse = new Vec2(e.getPoint());
            Vec2 endPos = new Vec2(frame.getSize());
            float reduction = panel.radius / 2.0F;

            Vec2 pos = panel.screen2world(new Vec2(e.getPoint()));

            boolean cache = pos.x >= closePos.x && pos.y >= (closePos.y - btnHeight) && pos.x <= (closePos.x + closeBtnWidth) && pos.y <= closePos.y;
            if (cache != closeBtnHovered) {
                closeBtnHovered = cache;
                frame.repaint();
            }

            cache = pos.x >= minimizePos.x && pos.y >= (minimizePos.y - btnHeight) && pos.x <= (minimizePos.x + minimizeBtnWidth) && pos.y <= minimizePos.y;
            if (cache != minimizeBtnHovered) {
                minimizeBtnHovered = cache;
                frame.repaint();
            }

            top = bottom = left = right = false;
            topLeft = topRight = bottomLeft = bottomRight = false;

            if (frame.isResizable()) {
                if (mouse.x >= 0 && mouse.y >= 0 && mouse.x <= (resizeRadius + reduction) && mouse.y <= (resizeRadius + reduction)) {
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                    topLeft = true;
                } else if (mouse.x >= (endPos.x - (resizeRadius + reduction)) && mouse.y >= 0 && mouse.x <= endPos.x && mouse.y <= (resizeRadius + reduction)) {
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                    topRight = true;
                } else if (mouse.x >= 0 && mouse.y >= (endPos.y - (resizeRadius + reduction)) && mouse.x <= (resizeRadius + reduction) && mouse.y <= endPos.y) {
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                    bottomLeft = true;
                } else if (mouse.x >= (endPos.x - (resizeRadius + reduction)) && mouse.y >= (endPos.y - (resizeRadius + reduction)) && mouse.x <= endPos.x && mouse.y <= endPos.y) {
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                    bottomRight = true;
                } else if (mouse.y > 0 && mouse.y <= resizeRadius || mouse.y >= endPos.y - resizeRadius) {
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                    if (mouse.y >= endPos.y - resizeRadius) {
                        bottom = true;
                    } else {
                        top = true;
                    }
                } else if (mouse.x >= endPos.x - resizeRadius || mouse.x <= resizeRadius) {
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                    if (mouse.x <= resizeRadius) {
                        left = true;
                    } else {
                        right = true;
                    }
                } else {
                    if (closeBtnHovered || minimizeBtnHovered) {
                        frame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else {
                        frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                }
            } else {
                if (closeBtnHovered || minimizeBtnHovered) {
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
        }

    }

    class GraphicsPanel extends JPanel {

        @Getter
        @Setter
        float radius;

        @Getter
        float xScale;

        @Getter
        float yScale;

        GraphicsPanel(float radius) {
            this.radius = radius;

            setOpaque(false);
            setLayout(new GridBagLayout());
        }

        /**
         * Uses the non scaled drawing system
         *
         * @param g the graphics
         */

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g.create();

            RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHints(hints);

            xScale = getWidth() / (float) gui.getInitialWidth();
            yScale = getHeight() / (float) gui.getInitialHeight();

            Vec2 mousePos = gui.getMouse().getPos().clone().div(xScale, yScale);
            Renderer renderer = new Renderer(g2d, mousePos, gui.getInitialWidth(), gui.getInitialHeight());

            g2d.setFont(gui.getFont());

            g2d.setColor(gui.getTheme().background);
            g2d.fill(new RoundRectangle2D.Float(borderRadius, borderRadius, getWidth() - borderRadius * 2, getHeight() - borderRadius * 2, radius, radius));

            renderer.pushGraphics();

            renderer.clip(new RoundRectangle2D.Float(borderRadius - 1, borderRadius - 1, getWidth() - borderRadius * 2 + 1, getHeight() - borderRadius * 2, radius, radius));
            renderer.drawRect(new Vec2(0, 0), getWidth(), (int) (toolBarFixedHeight * yScale), gui.getTheme().base);

            renderer.scale(xScale, yScale);
            drawToolBar(renderer, (int) (getWidth() / xScale), (int) (getHeight() / yScale));

            renderer.popGraphics();

            g2d.setColor(gui.getTheme().border);
            g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));

            renderer.pushGraphics();

            renderer.scale(xScale, yScale);
            updateDisplay(renderer, (int) (getWidth() / xScale), (int) (getHeight() / yScale));

            renderer.popGraphics();

            g2d.dispose();
        }

        public Vec2 screen2world(Vec2 pos) {
            return pos.clone().div(xScale, yScale);
        }

        public Vec2 world2screen(Vec2 pos) {
            return pos.clone().mul(xScale, yScale);
        }

        public float world2ScreenX(float coordinate) {
            return coordinate * xScale;
        }

        public float world2ScreenY(float coordinate) {
            return coordinate * yScale;
        }

        public float screen2WorldX(float coordinate) {
            return coordinate / xScale;
        }

        public float screen2WorldY(float coordinate) {
            return coordinate / yScale;
        }

    }

}
