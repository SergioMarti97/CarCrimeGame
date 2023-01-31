package performance;

import base.graphics.HexColors;
import base.graphics.Renderer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TestPixelBuffer extends Application {

    private Renderer renderer;

    @Override
    public void start(Stage stage) {
        renderer = new Renderer(1280, 760);

        // drawing methods
        renderer.setPixel(2, 2, 0xffff0000);
        renderer.drawCircle(100, 150, 60, HexColors.LEMON);
        renderer.process();

        // Build Writable image
        WritableImage image = new WritableImage(renderer.getPixelBuffer());

        // Set the application scene
        ImageView imgView = new ImageView();
        imgView.setPreserveRatio(true);
        imgView.setImage(image);
        StackPane pane = new StackPane(imgView);
        pane.setFocusTraversable(true);
        imgView.fitWidthProperty().bind(pane.widthProperty());
        imgView.fitHeightProperty().bind(pane.heightProperty());

        // Show the scene
        Scene scene = new Scene(pane, renderer.getWidth(), renderer.getHeight());
        stage.setScene(scene);
        stage.setTitle("Test IntBuffer API");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
