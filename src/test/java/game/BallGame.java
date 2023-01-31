package game;

import base.AbstractGame;
import base.GameApplication;
import base.renderer.HexColors;
import javafx.scene.paint.Color;

public class BallGame implements AbstractGame {

    private int x;

    private int y;

    @Override
    public void initialize(GameApplication gc) {

    }

    @Override
    public void update(GameApplication gc, float elapsedTime) {
        x = (int)gc.getInput().getMouseX();
        y = (int)gc.getInput().getMouseY();
    }

    @Override
    public void render(GameApplication gc) {
        gc.getRenderer().clear(HexColors.BLACK);
        int radius = 30;

        gc.getGraphicsContext().setFill(Color.LEMONCHIFFON);
        gc.getGraphicsContext().fillOval(x - radius, y - radius, 2 * radius, 2 * radius);

        gc.getRenderer().drawFillRect(x - 10, y - 10, 100, 100, HexColors.LEMON);

        gc.getRenderer().drawCircle(x, y, radius, HexColors.FANCY_BLUE);
    }

}
