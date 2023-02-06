package menu;

import base.graphics.HexColors;
import base.graphics.Renderer;
import base.graphics.image.ImageTile;
import base.vectors.points2d.Vec2di;

import java.util.Stack;

public class MenuManager {

    // todo: implement MenuRenderer / stores the graphics tile

    private final Stack<MenuObject> panels;

    private Vec2di offsetPanels = new Vec2di(10, 10);

    private boolean drawTileBorders = false;

    public MenuManager() {
        panels = new Stack<>();
    }

    public void open(MenuObject mo) {
        clear();
        panels.add(mo);
    }

    public void clear() {
        panels.clear();
    }

    // User Input methods

    public void onUp() {
        if (!panels.isEmpty()) {
            panels.peek().onUp();
        }
    }

    public void onDown() {
        if (!panels.isEmpty()) {
            panels.peek().onDown();
        }
    }

    public void onLeft() {
        if (!panels.isEmpty()) {
            panels.peek().onLeft();
        }
    }

    public void onRight() {
        if (!panels.isEmpty()) {
            panels.peek().onRight();
        }
    }

    public void onBack() {
        if (!panels.isEmpty()) {
            panels.pop();
        }
    }

    public MenuObject onConfirm() {
        if (panels.isEmpty()) {
            return null;
        }

        MenuObject next = panels.peek().onConfirm();

        if (next.equals(panels.peek())) {
            if (panels.peek().getSelectedItem().isEnabled()) {
                return panels.peek().getSelectedItem();
            }
        } else {
            if (next.isEnabled()) {
                return panels.push(next);
            }
        }

        return null;
    }

    private void drawCursorImage(Renderer r, ImageTile img, Vec2di screenPos) {
        r.drawImage(img.getTileImage(4, 0), screenPos.getX(), screenPos.getY());
        r.drawImage(img.getTileImage(5, 0), screenPos.getX() + img.getTileWidth(), screenPos.getY());
        r.drawImage(img.getTileImage(4, 1), screenPos.getX(), screenPos.getY() + img.getTileHeight());
        r.drawImage(img.getTileImage(5, 1), screenPos.getX() + img.getTileWidth(), screenPos.getY() + img.getTileHeight());
        if (drawTileBorders) {
            r.drawRect(screenPos.getX(), screenPos.getY(), img.getTileWidth() * 2, img.getTileHeight() * 2, HexColors.YELLOW);
        }
    }

    public void draw(Renderer r, ImageTile img, Vec2di screenOffset) {
        if (panels.isEmpty()) {
            return;
        }

        for (MenuObject p : panels) {
            p.drawSelf(r, img, screenOffset, drawTileBorders);
            screenOffset.add(offsetPanels);
        }

        // Draw cursor
        drawCursorImage(r, img, panels.get(panels.size() - 1).getCursorPos());
    }

    // Getters and Setters

    public Vec2di getOffsetPanels() {
        return offsetPanels;
    }

    public boolean isDrawingTileBorders() {
        return drawTileBorders;
    }

    public void setOffsetPanels(Vec2di offsetPanels) {
        this.offsetPanels = offsetPanels;
    }

    public void setDrawTileBorders(boolean drawTileBorders) {
        this.drawTileBorders = drawTileBorders;
    }

}
