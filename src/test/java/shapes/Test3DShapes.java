package shapes;

import base.clock.GameClock;
import base.clock.Render;
import base.clock.Updater;
import base.input.Input;
import javafx.animation.AnimationTimer;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * 3D Shapes: "https://www.tutorialspoint.com/javafx/javafx_3d_shapes.htm"
 * Timer: "https://stackoverflow.com/questions/28287398/what-is-the-preferred-way-of-getting-the-frame-rate-of-a-javafx-application"
 */
public class Test3DShapes extends Application {

    @Override
    public void start(Stage stage) {
        // -----------------------------------------
        // 3D SHAPES
        // -----------------------------------------

        //Drawing a Box
        Box box1 = new Box();

        //Setting the properties of the Box
        box1.setWidth(100.0);
        box1.setHeight(100.0);
        box1.setDepth(100.0);

        //Setting the position of the box
        box1.setTranslateX(200);
        box1.setTranslateY(150);
        box1.setTranslateZ(0);

        //Setting the drawing mode of the box
        box1.setDrawMode(DrawMode.LINE);

        //Drawing a Box
        Box box2 = new Box();

        //Setting the properties of the Box
        box2.setWidth(100.0);
        box2.setHeight(100.0);
        box2.setDepth(100.0);

        //Setting the position of the box
        box2.setTranslateX(450); //450
        box2.setTranslateY(150);//150
        box2.setTranslateZ(300);

        //Setting the drawing mode of the box
        box2.setDrawMode(DrawMode.FILL);

        // -----------------------------------------
        // TEXTURE OF THE 3D BOX
        // -----------------------------------------

        //Setting the material of the box
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.DARKSLATEBLUE);

        //Setting the diffuse color material to box
        box2.setMaterial(material);

        // -----------------------------------------
        // ROTATION TRANSITION OF THE BOX
        // -----------------------------------------

        //Setting the rotation animation to the box
        RotateTransition rotateTransition = new RotateTransition();

        //Setting the duration for the transition
        rotateTransition.setDuration(Duration.millis(2000));

        //Setting the axis of the rotation
        rotateTransition.setAxis(Rotate.Y_AXIS);

        //Setting the angle of the rotation
        rotateTransition.setByAngle(360);

        //Setting the cycle count for the transition
        rotateTransition.setCycleCount(1);

        //Setting auto reverse value to false
        rotateTransition.setAutoReverse(false);

        //Setting the node for the transition
        rotateTransition.setNode(box2);

        // -----------------------------------------
        // SETTING UP THE SCENE
        // -----------------------------------------

        //Creating a Group object
        Group root = new Group(box1, box2);
        root.setFocusTraversable(true);

        //Creating a scene object
        Scene scene = new Scene(root, 1280, 725, true, SceneAntialiasing.BALANCED);

        //Setting camera
        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setTranslateZ(0);
        scene.setCamera(camera);

        // -----------------------------------------
        // CREATE THE INPUT
        // -----------------------------------------

        Input input = new Input(root);

        // -----------------------------------------
        // CREATE THE GAME CLOCK
        // -----------------------------------------

        GameClock timer = new GameClock(new Updater() {
            @Override
            public void update(float elapsedTime) {
                final float vel = 100;

                if (input.isKeyHeld(KeyCode.A)) {
                    camera.setRotationAxis(new Point3D(0, 1, 0));
                    camera.setRotate(camera.getRotate() + vel * elapsedTime);
                }
                if (input.isKeyHeld(KeyCode.D)) {
                    camera.setRotationAxis(new Point3D(0, 1, 0));
                    camera.setRotate(camera.getRotate() - vel * elapsedTime);
                }
                if (input.isKeyHeld(KeyCode.W)) {
                    camera.setRotationAxis(new Point3D(0, 0, 1));
                    camera.setRotate(camera.getRotate() + vel * elapsedTime);
                }
                if (input.isKeyHeld(KeyCode.S)) {
                    camera.setRotationAxis(new Point3D(0, 0, 1));
                    camera.setRotate(camera.getRotate() - vel * elapsedTime);
                }

                if (input.isKeyHeld(KeyCode.LEFT)) {
                    camera.setTranslateX(camera.getTranslateX() + vel * elapsedTime);
                }
                if (input.isKeyHeld(KeyCode.RIGHT)) {
                    camera.setTranslateX(camera.getTranslateX() - vel * elapsedTime);
                }
                if (input.isKeyHeld(KeyCode.UP)) {
                    camera.setTranslateY(camera.getTranslateY() + vel * elapsedTime);
                }
                if (input.isKeyHeld(KeyCode.DOWN)) {
                    camera.setTranslateY(camera.getTranslateY() - vel * elapsedTime);
                }

                input.update();
            }
        }, new Render() {
            @Override
            public void render() {

            }
        });
        timer.start();

        // -----------------------------------------
        // ADD EVENT HANDLERS TO THE SCENE
        // -----------------------------------------

        /*scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.A) {
                    TranslateTransition translation = new TranslateTransition();
                    //translation.setDuration(Duration.millis(10));
                    translation.setByX(10);
                    translation.setAutoReverse(false);
                    translation.setNode(camera);
                    translation.play();
                    //camera.setTranslateX(camera.getTranslateX() + 10);
                    System.out.println("Pressed key: \"A\"");
                }
                if (keyEvent.getCode() == KeyCode.D) {
                    camera.setTranslateX(camera.getTranslateX() - 10);
                    System.out.println("Pressed key: \"D\"");
                }
                if (keyEvent.getCode() == KeyCode.R) {
                    rotateTransition.play();
                    System.out.println("Pressed key: \"R\"");
                }
                if (keyEvent.getCode() == KeyCode.T) {
                    rotateTransition.stop();
                    System.out.println("Pressed key: \"T\"");
                }
            }
        });*/

        // -----------------------------------------
        // RUN THE APPLICATION
        // -----------------------------------------

        //Setting title to the Stage
        stage.setTitle("Drawing a Box");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
