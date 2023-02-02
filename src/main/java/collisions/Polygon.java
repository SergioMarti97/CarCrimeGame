package collisions;

import base.GameApplication;
import base.graphics.HexColors;
import base.vectors.points2d.Vec2df;
import base.vectors.points2d.Vec2di;

import java.util.ArrayList;

public class Polygon {

    private final ArrayList<Vec2df> points = new ArrayList<>();

    private ArrayList<Vec2df> model = new ArrayList<>();

    private final Vec2df pos;

    private float angle = 0;

    private boolean overlap = false;

    public Polygon(float x, float y, ArrayList<Vec2df> model) {
        this.pos = new Vec2df(x, y);
        this.model = model;
        for (Vec2df p : model) {
            points.add(new Vec2df(p));
        }
    }

    public Polygon(Vec2df pos, ArrayList<Vec2df> model) {
        this.pos = pos;
        this.model = model;
        for (Vec2df p : model) {
            points.add(new Vec2df(p));
        }
    }

    public void update() {
        // todo: implement quick math (tables with pre-calculated values for sin() and cos() methods)
        for (int i = 0; i < model.size(); i++) {
            double x = (model.get(i).getX() * Math.cos(angle) - (model.get(i).getY() * Math.sin(angle))) + pos.getX();
            double y = (model.get(i).getX() * Math.sin(angle) + (model.get(i).getY() * Math.cos(angle))) + pos.getY();
            points.get(i).set((float) x, (float) y);
        }
        overlap = false;
    }

    public void render(GameApplication gc) {
        Vec2di offset = new Vec2di(100, 100);
        Vec2di scale = new Vec2di(50, 50);

        int color = overlap ? HexColors.RED : HexColors.WHITE;
        ArrayList<Vec2df> points = new ArrayList<>();
        for (Vec2df p : this.points) {
            points.add(new Vec2df(
                    scale.getX() * p.getX() + offset.getX(),
                    scale.getY() * p.getY() + offset.getY()
            ));
        }
        gc.getRenderer().drawPolygon(points, color);
        gc.getRenderer().drawLine(
                (int)(scale.getX() * pos.getX() + offset.getX()),
                (int)(scale.getY() * pos.getY() + offset.getY()),
                (int)points.get(0).getX(),
                (int)points.get(0).getY(),
                color);
    }

    ////////////////////////////

    public ArrayList<Vec2df> getPoints() {
        return points;
    }

    public ArrayList<Vec2df> getModel() {
        return model;
    }

    public Vec2df getPos() {
        return pos;
    }

    public float getAngle() {
        return angle;
    }

    public boolean isOverlap() {
        return overlap;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setOverlap(boolean overlap) {
        this.overlap = overlap;
    }

    ////////////////////////////

}
