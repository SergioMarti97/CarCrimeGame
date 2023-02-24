package panAndZoom;

import base.vectors.points2d.Vec2df;

public class PanAndZoomUtils {

    public static float worldToScreen(float x, float offset, float scale) {
        return (x - offset) * scale;
    }

    public static float screenToWorld(float x, float offset, float scale) {
        return (x / scale) + offset;
    }

    public static float scaleToWorld(float x, float scale) {
        return x / scale;
    }

    public static float scaleToScreen(float x, float scale) {
        return x * scale;
    }

    // ------------------

    public static Vec2df worldToScreen(Vec2df pos, Vec2df offset, Vec2df scale) {
        return new Vec2df(
                worldToScreen(pos.getX(), offset.getX(), scale.getX()),
                worldToScreen(pos.getY(), offset.getY(), scale.getY())
        );
    }

    public static Vec2df screenToWorld(Vec2df pos, Vec2df offset, Vec2df scale) {
        return new Vec2df(
                screenToWorld(pos.getX(), offset.getX(), scale.getX()),
                screenToWorld(pos.getY(), offset.getY(), scale.getY())
        );
    }

    public static Vec2df scaleToWorld(Vec2df pos, Vec2df scale) {
        return new Vec2df(
                scaleToWorld(pos.getX(), scale.getX()),
                scaleToWorld(pos.getY(), scale.getY())
        );
    }

    public static Vec2df scaleToScreen(Vec2df pos, Vec2df scale) {
        return new Vec2df(
                scaleToScreen(pos.getX(), scale.getX()),
                scaleToScreen(pos.getY(), scale.getY())
        );
    }

}
