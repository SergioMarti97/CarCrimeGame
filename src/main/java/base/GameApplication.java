package base;

import base.clock.GameClock;
import base.input.Input;
import base.graphics.Renderer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * This class should be implemented
 */
public class GameApplication extends Application {

    // Settings of the application

    private String appName = "default";

    private int width = 800;

    private int height = 600;

    // Classes which manages internally the game

    private AbstractGame game;

    private Renderer renderer;

    private Input input;

    private GameClock clock;

    // Scene layout

    private WritableImage image;

    private Canvas canvas;

    @Override
    public void start(Stage stage) {
        // Renderer
        renderer = new Renderer(width, height);
        // Build Writable image
        image = new WritableImage(renderer.getPixelBuffer());

        // ----------------------------- //
        // - Set the application scene - //
        // ----------------------------- //

        // Set the image view
        ImageView imgView = new ImageView();
        imgView.setPreserveRatio(true);
        imgView.setImage(image);
        imgView.setSmooth(true);

        // Set the canvas
        canvas = new Canvas();

        // Choose between canvas or image view...
        StackPane pane = new StackPane(canvas);
        pane.setFocusTraversable(true);

        // bing width and height properties to the graphics display
        imgView.fitWidthProperty().bind(pane.widthProperty());
        imgView.fitHeightProperty().bind(pane.heightProperty());
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());

        // Input
        input = new Input(pane);

        // Game Clock
        clock = new GameClock(this::update, this::render);

        // Set the scene
        Scene scene = new Scene(pane, renderer.getWidth(), renderer.getHeight());
        stage.setScene(scene);
        stage.setTitle(appName);

        // Show the scene
        if (game != null) {
            game.initialize(this);
            clock.start();
            stage.show();
        }
    }

    // Update and Render methods

    protected void update(float elapsedTime) {
        game.update(this, elapsedTime);
        input.update();
        renderer.process();
    }

    protected void render() {
        canvas.getGraphicsContext2D().drawImage(image, 0, 0);
        canvas.getGraphicsContext2D().setImageSmoothing(true);

        game.render(this);

    }

    // Getters and Setters

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public AbstractGame getGame() {
        return game;
    }

    public void setGame(AbstractGame game) {
        this.game = game;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public Input getInput() {
        return input;
    }

    public GameClock getClock() {
        return clock;
    }

    public GraphicsContext getGraphicsContext() {
        return canvas.getGraphicsContext2D();
    }
}
