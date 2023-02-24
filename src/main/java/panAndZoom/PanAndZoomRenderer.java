package panAndZoom;

import base.GameApplication;
import base.graphics.Renderer;
import base.vectors.points2d.Vec2d;
import base.vectors.points2d.Vec2df;
import base.vectors.points2d.Vec2di;
import javafx.scene.input.MouseButton;

import java.util.Arrays;

public class PanAndZoomRenderer extends Renderer {

    protected Vec2df startPan;

    protected Vec2df worldOffset;

    protected Vec2df worldScale;

    protected boolean isPanning = false;

    protected boolean isZooming = false;

    public PanAndZoomRenderer(int width, int height) {
        super(width, height);

        startPan = new Vec2df();
        worldOffset = new Vec2df();
        worldScale = new Vec2df();
    }

    public PanAndZoomRenderer(Renderer other) {
        super(other.getWidth(), other.getHeight());
        p = other.getP();

        startPan = new Vec2df();
        worldOffset = new Vec2df();
        worldScale = new Vec2df();
    }

    // Functions

    public void startPan(Vec2df pos) {
        isPanning = true;
        startPan.set(pos);
    }

    public void updatePan(Vec2df pos) {
        if (isPanning) {
            worldOffset.addToX(-((pos.getX() - startPan.getX()) / worldScale.getX()));
            worldOffset.addToY(-((pos.getY() - startPan.getY()) / worldScale.getY()));

            startPan.set(pos);
        }
    }

    public void endPan(Vec2df pos) {
        updatePan(pos);
        isPanning = false;
    }

    public void zoomAtScreenPos(float deltaZoom, Vec2df pos) {
        Vec2df offsetBeforeZoom = PanAndZoomUtils.screenToWorld(pos, worldOffset, worldScale);
        worldScale.multiply(deltaZoom);
        Vec2df offsetAfterZoom = PanAndZoomUtils.screenToWorld(pos, worldOffset, worldScale);
        worldOffset.addToX(offsetBeforeZoom.getX() - offsetAfterZoom.getX());
        worldOffset.addToY(offsetBeforeZoom.getY() - offsetAfterZoom.getY());
    }

    public void setZoom(float zoomX, float zoomY, Vec2df pos) {
        Vec2df offsetBeforeZoom = PanAndZoomUtils.screenToWorld(pos, worldOffset, worldScale);
        worldScale.set(zoomX, zoomY);
        Vec2df offsetAfterZoom = PanAndZoomUtils.screenToWorld(pos, worldOffset, worldScale);
        worldOffset.addToX(offsetBeforeZoom.getX() - offsetAfterZoom.getX());
        worldOffset.addToY(offsetBeforeZoom.getY() - offsetAfterZoom.getY());
    }

    public void setZoom(Vec2df zoom, Vec2df pos) {
        setZoom(zoom.getX(), zoom.getY(), pos);
    }

    public void setZoom(float zoom, Vec2df pos) {
        setZoom(zoom, zoom, pos);
    }

    public void handlePanAndZoom(GameApplication gc, MouseButton mouseButton, float zoomRate, boolean isPan, boolean isZoom) {

        Vec2df mousePos = new Vec2df(
                (float)gc.getInput().getMouseX(),
                (float)gc.getInput().getMouseY()
        );

        if (isPan) {
            if (gc.getInput().isButtonDown(mouseButton)) {
                startPan(mousePos);
            }
            if (gc.getInput().isButtonHeld(mouseButton)) {
                updatePan(mousePos);
            }
            if (gc.getInput().isButtonUp(mouseButton)) {
                endPan(mousePos);
            }
        }

        if (isZoom) {
            if (gc.getInput().getScroll() != 0) {
                zoomAtScreenPos(1.0f + (zoomRate * gc.getInput().getScroll()), mousePos);
            }
        }

    }

    public void drawRect(Vec2df pos, Vec2df size, int color) {
        Vec2df pos2 = PanAndZoomUtils.worldToScreen(pos, worldOffset, worldScale);
        Vec2df size2 = PanAndZoomUtils.scaleToScreen(size, worldScale);
        super.drawRect(
                (int) pos2.getX(),
                (int) pos2.getY(),
                (int) size2.getX(),
                (int) size2.getY(),
                color);
    }

    public void drawFillRect(Vec2df pos, Vec2df size, int color) {
        Vec2df pos2 = PanAndZoomUtils.worldToScreen(pos, worldOffset, worldScale);
        Vec2df size2 = PanAndZoomUtils.scaleToScreen(size, worldScale);
        super.drawFillRect(
                (int) pos2.getX(),
                (int) pos2.getY(),
                (int) size2.getX(),
                (int) size2.getY(),
                color);
    }

    // Getters & Setters

    public Vec2df getWorldOffset() {
        return worldOffset;
    }

    public Vec2df getWorldScale() {
        return worldScale;
    }
}
