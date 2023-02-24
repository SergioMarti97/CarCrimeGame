package collisions;

import base.graphics.HexColors;
import base.graphics.Renderer;
import base.vectors.points2d.Vec2df;
import base.vectors.points2d.Vec2di;

import java.util.ArrayList;

public class PolygonDrawUtils {

    public static void drawPolygon(Renderer r,
                                   Polygon p,
                                   float offsetX,
                                   float offsetY,
                                   float scaleX,
                                   float scaleY,
                                   int color,
                                   int colorOverlap) {
        //Vec2di offset = new Vec2di(100, 100);
        //Vec2di scale = new Vec2di(50, 50);

        int c = p.isOverlap() ? colorOverlap : color; //HexColors.RED : HexColors.WHITE;

        // transform
        ArrayList<Vec2df> points = new ArrayList<>();
        for (Vec2df point : p.getPoints()) {
            points.add(new Vec2df(
                    scaleX * point.getX() + offsetX,
                    scaleY * point.getY() + offsetY
            ));
        }

        // draw
        r.drawPolygon(points, c);
        r.drawLine(
                (int)(scaleX * p.getPos().getX() + offsetX),
                (int)(scaleY * p.getPos().getY() + offsetY),
                (int)points.get(0).getX(),
                (int)points.get(0).getY(),
                c);
    }

    public static void drawPolygon(Renderer r,
                                   Polygon p,
                                   Vec2df offset,
                                   Vec2df scale,
                                   int color,
                                   int colorOverlap) {
        drawPolygon(r, p, offset.getX(), offset.getY(), scale.getX(), scale.getY(), color, colorOverlap);
    }

}
