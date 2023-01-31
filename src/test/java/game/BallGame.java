package game;

import base.AbstractGame;
import base.GameApplication;
import base.graphics.HexColors;
import base.graphics.image.Image;
import base.graphics.image.ImageTile;
import base.vectors.points2d.Vec2di;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

public class BallGame implements AbstractGame {

    private int x;

    private int y;

    private Vec2di[] points;

    private Vec2di selectedPoint = null;

    private Image texture;

    @Override
    public void initialize(GameApplication gc) {
        points = new Vec2di[] {
                new Vec2di(100, 100),
                new Vec2di(150, 100),
                new Vec2di(100, 150)
        };

        ImageTile imageTile = new ImageTile("/city/City_Roads1_mip1.png", 96, 96);
        texture = imageTile.getTileImage(3, 1);
    }

    private int distanceSquareBetweenPoints(int x1, int y1, int x2, int y2) {
        return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
    }

    @Override
    public void update(GameApplication gc, float elapsedTime) {
        x = (int)gc.getInput().getMouseX();
        y = (int)gc.getInput().getMouseY();

        int minDistance = 30;
        if (gc.getInput().isButtonDown(MouseButton.PRIMARY)) {
            for (Vec2di v : points) {
                if (distanceSquareBetweenPoints(v.getX(), v.getY(), x, y) < minDistance * minDistance) {
                    selectedPoint = v;
                }
            }
        }
        if (gc.getInput().isButtonUp(MouseButton.PRIMARY)) {
            selectedPoint = null;
        }

        if (selectedPoint != null) {
            selectedPoint.setX(x);
            selectedPoint.setY(y);
        }
    }

    @Override
    public void render(GameApplication gc) {
        gc.getRenderer().clear(HexColors.WHITE);

        gc.getRenderer().drawTexturedTriangle(
                points[0].getX(), points[0].getY(), 0, 0, 0,
                points[1].getX(), points[1].getY(), 1, 0, 0,
                points[2].getX(), points[2].getY(), 0, 1, 0,
                texture
        );

        int radius = 30;
        gc.getRenderer().drawCircle(x, y, radius, HexColors.FANCY_BLUE);
    }

}
