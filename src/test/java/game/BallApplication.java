package game;

import base.GameApplication;

public class BallApplication extends GameApplication {

    @Override
    public void init() throws Exception {
        super.init();
        setAppName("Ball Application");
        setWidth(985);
        setHeight(645);
        setGame(new BallGame());
    }

    public static void main(String[] args) {
        launch(args);
    }

}
