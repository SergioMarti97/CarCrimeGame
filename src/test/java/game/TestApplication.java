package game;

import base.GameApplication;

public class TestApplication extends GameApplication {

    @Override
    public void init() throws Exception {
        super.init();
        setAppName("Test Application");
        setWidth(985); // 985
        setHeight(645); // 645
        setGame(new CityGame());
    }

    public static void main(String[] args) {
        launch(args);
    }

}
