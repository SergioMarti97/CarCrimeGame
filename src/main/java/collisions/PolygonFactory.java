package collisions;

import base.vectors.points2d.Vec2df;

import java.util.ArrayList;

public class PolygonFactory {

    public static Polygon buildPentagon(float x, float y) {
        ArrayList<Vec2df> model = new ArrayList<>();
        double theta = Math.PI * 2.0f / 5.0f;
        for (int i = 0; i < 5; i++) {
            double pX = Math.cos(theta * i); // 30.0f *
            double pY = Math.sin(theta * i); // 30.0f *
            model.add(new Vec2df((float)pX, (float)pY));
        }
        return new Polygon(new Vec2df(x, y), model);
    }

    public static Polygon buildPentagon(Vec2df pos) {
        return buildPentagon(pos.getX(), pos.getY());
    }

    public static Polygon buildTriangle(float x, float y) {
        ArrayList<Vec2df> model = new ArrayList<>();
        double theta = Math.PI * 2.0f / 3.0f;
        for (int i = 0; i < 3; i++) {
            double pX = Math.cos(theta * i); // 20.0f *
            double pY = Math.sin(theta * i); // 20.0f *
            model.add(new Vec2df((float)pX, (float)pY));
        }
        return new Polygon(new Vec2df(x, y), model);
    }

    public static Polygon buildTriangle(Vec2df pos) {
        return buildTriangle(pos.getX(), pos.getY());
    }

    public static Polygon buildCenteredSquare(float x, float y) {
        ArrayList<Vec2df> model = new ArrayList<>();
        model.add(new Vec2df(-0.5f, -0.5f));
        model.add(new Vec2df(0.5f, -0.5f));
        model.add(new Vec2df(0.5f, 0.5f));
        model.add(new Vec2df(-0.5f, 0.5f));
        return new Polygon(new Vec2df(x, y), model);
    }

    public static Polygon buildSquare(float x, float y) {
        ArrayList<Vec2df> model = new ArrayList<>();
        model.add(new Vec2df(0.0f, 0.0f));
        model.add(new Vec2df(1.0f, 0.0f));
        model.add(new Vec2df(1.0f, 1.0f));
        model.add(new Vec2df(0.0f, 1.0f));
        return new Polygon(new Vec2df(x, y), model);
    }

}
