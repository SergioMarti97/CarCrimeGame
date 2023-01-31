package shapes;

import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * "https://www.tutorialspoint.com/javafx/javafx_animations.htm"
 */
public class TestShapes extends Application {

    @Override
    public void start(Stage stage) {
        // Create the hexagon
        Polygon hexagon = new Polygon();
        hexagon.getPoints().addAll(200.0, 50.0,
                400.0, 50.0,
                450.0, 150.0,
                400.0, 250.0,
                200.0, 250.0,
                150.0, 150.0);
        hexagon.setFill(Color.BLUE);

        //Creating a rotate transition
        RotateTransition rotateTransition = new RotateTransition();

        //Setting the duration for the transition
        rotateTransition.setDuration(Duration.millis(10000));

        //Setting the node for the transition
        rotateTransition.setNode(hexagon);

        //Setting the angle of the rotation
        rotateTransition.setByAngle(360);

        //Setting the cycle count for the transition
        rotateTransition.setCycleCount(50);

        //Setting auto reverse value to false
        rotateTransition.setAutoReverse(false);

        //Playing the animation
        rotateTransition.play();

        Group root = new Group(hexagon);
        Scene scene = new Scene(root, 1280, 725);

        root.prefWidth(scene.getWidth());
        root.prefHeight(scene.getHeight());

        stage.setScene(scene);
        stage.setTitle("Test Shapes");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
