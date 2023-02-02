package game;

import base.AbstractGame;
import base.GameApplication;
import base.graphics.HexColors;
import base.graphics.image.Image;
import base.graphics.image.ImageTile;
import base.vectors.points2d.Vec2di;
import engine3d.Perspective;
import engine3d.PipeLine;
import engine3d.RenderFlags;
import engine3d.matrix.MatrixMath;
import engine3d.mesh.Mesh;
import engine3d.mesh.MeshFactory;
import engine3d.mesh.Model;
import engine3d.transforms.Rotation;
import engine3d.transforms.Scale;
import engine3d.transforms.Transform;
import engine3d.transforms.Translation;
import engine3d.vectors.Vec4df;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import java.util.Random;

public class BallGame implements AbstractGame {

    private int x;

    private int y;

    private Vec2di[] points;

    private Vec2di selectedPoint = null;

    private Image texture;

    private PipeLine pipeLine;

    private Mesh obj;

    private Rotation rot;

    private Translation trans;

    private Scale scale;

    private Translation[] translations;

    private int numOfCubes;

    private Model model;

    @Override
    public void initialize(GameApplication gc) {
        pipeLine = new PipeLine(gc.getRenderer().getP(), gc.getWidth(), gc.getHeight());
        pipeLine.setPerspective(Perspective.NORMAL);
        pipeLine.getRenderer3D().setRenderFlag(RenderFlags.RENDER_FULL_TEXTURED);

        points = new Vec2di[] {
                new Vec2di(100, 100),
                new Vec2di(150, 100),
                new Vec2di(100, 150)
        };

        ImageTile imageTile = new ImageTile("/assets/City_Roads1_mip1.png", 96, 96);
        texture = imageTile.getTileImage(3, 1);

        obj = MeshFactory.getUnitCube();
        rot = new Rotation();
        trans = new Translation(-0.5f, -0.5f, -0.5f);
        scale = new Scale(0.2f, 0.2f, 0.2f);

        numOfCubes = 10;
        Random rnd = new Random();
        translations = new Translation[numOfCubes];
        for (int i = 0; i < numOfCubes; i++) {
            float x = 1.5f * (rnd.nextFloat() - rnd.nextFloat());
            float y = 1.5f * (rnd.nextFloat() - rnd.nextFloat());
            translations[i] = new Translation(x, y, 0);
        }

        model = MeshFactory.getModel("/assets/buildings/building.obj");
    }

    private void updateCameraPanning(GameApplication gc, float dt) {
        final float vel = 1.75f;

        if ( gc.getInput().isKeyHeld(KeyCode.UP) ) {
            //Vec4df forward = MatrixMath.vectorMul(pipeLine.getCameraObj().getLookDirection(), vel * dt);
            //pipeLine.getCameraObj().setOrigin(MatrixMath.vectorAdd(pipeLine.getCameraObj().getOrigin(), forward));
            pipeLine.getCameraObj().getOrigin().addToY(vel * dt);
        }
        if ( gc.getInput().isKeyHeld(KeyCode.DOWN) ) {
            //Vec4df forward = MatrixMath.vectorMul(pipeLine.getCameraObj().getLookDirection(), -vel * dt);
            //pipeLine.getCameraObj().setOrigin(MatrixMath.vectorAdd(pipeLine.getCameraObj().getOrigin(), forward));
            pipeLine.getCameraObj().getOrigin().addToY(-vel * dt);
        }

        if ( gc.getInput().isKeyHeld(KeyCode.LEFT) ) {
            Vec4df forward = MatrixMath.vectorMul(pipeLine.getCameraObj().getLookDirection(), -vel * dt);
            Vec4df cross = new Vec4df(-forward.getZ(), forward.getY(), forward.getX());
            pipeLine.getCameraObj().setOrigin(MatrixMath.vectorAdd(pipeLine.getCameraObj().getOrigin(), cross));
        }
        if ( gc.getInput().isKeyHeld(KeyCode.RIGHT) ) {
            Vec4df forward = MatrixMath.vectorMul(pipeLine.getCameraObj().getLookDirection(), vel * dt);
            Vec4df cross = new Vec4df(-forward.getZ(), forward.getY(), forward.getX());
            pipeLine.getCameraObj().setOrigin(MatrixMath.vectorAdd(pipeLine.getCameraObj().getOrigin(), cross));
        }

        if ( gc.getInput().isKeyHeld(KeyCode.Z) ) {
            pipeLine.getCameraObj().getOrigin().addToY(2.0f * dt);
        }
        if ( gc.getInput().isKeyHeld(KeyCode.X) ) {
            pipeLine.getCameraObj().getOrigin().addToY(-2.0f * dt);
        }
    }

    private int distanceSquareBetweenPoints(int x1, int y1, int x2, int y2) {
        return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
    }

    @Override
    public void update(GameApplication gc, float elapsedTime) {
        updateCameraPanning(gc, elapsedTime);
        pipeLine.updateMatView();

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

        if (gc.getInput().isKeyDown(KeyCode.SPACE)) {
            System.out.println("SPACE");
        }

        if (selectedPoint != null) {
            selectedPoint.setX(x);
            selectedPoint.setY(y);
        }

        rot.getDelta().addToX(elapsedTime);
        rot.getDelta().addToZ(elapsedTime * 1.5f);
        rot.update();
        trans.update();
        scale.update();
        for (Translation t : translations) {
            t.update();
        }
    }

    @Override
    public void render(GameApplication gc) {
        gc.getRenderer().clear(HexColors.FANCY_BLUE);

        for (int i = 0; i < numOfCubes; i++) {
            Transform t = new Transform();
            pipeLine.setTransform(t.combine(trans).combine(scale).combine(rot).combine(translations[i]).getMat());
            pipeLine.renderMesh(MeshFactory.getUnitCube(), texture);
            pipeLine.renderModel(model);
        }
        pipeLine.getRenderer3D().clearDepthBuffer();

        gc.getRenderer().drawTexturedTriangle(
                points[0].getX(), points[0].getY(), 0, 0, 0,
                points[1].getX(), points[1].getY(), 1, 0, 0,
                points[2].getX(), points[2].getY(), 0, 1, 0,
                texture
        );

        int radius = 30;
        gc.getRenderer().drawCircle(x, y, radius, HexColors.FANCY_RED);
    }

}
