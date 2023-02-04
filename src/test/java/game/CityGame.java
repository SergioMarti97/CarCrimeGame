package game;

import base.AbstractGame;
import base.GameApplication;
import base.graphics.HexColors;
import base.graphics.image.Image;
import base.graphics.image.ImageTile;
import base.vectors.points2d.Vec2di;
import collisions.ConvexPolygonCollisions;
import collisions.Polygon;
import engine3d.PipeLine;
import engine3d.RenderFlags;
import engine3d.matrix.Mat4x4;
import engine3d.matrix.MatrixMath;
import engine3d.mesh.MeshFactory;
import engine3d.mesh.Model;
import engine3d.vectors.Vec4df;
import game.car.Car;
import game.city.City;
import game.city.CityCell;
import game.city.CityIO;
import game.city.CityRender;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;
import java.util.HashSet;

public class CityGame implements AbstractGame {

    private PipeLine pipe;

    private Car car;

    private City city;

    private CityRender cityRender;

    private Image[] cityTextures;

    ////

    private Vec2di mouseWorld;

    private Vec2di oldMouseWorld;

    private HashSet<CityCell> selectedCells;

    ////

    private Model carModel;

    ////

    private final ArrayList<Polygon> boundingBoxes = new ArrayList<>();

    ////

    private final String path = "C:\\Users\\Sergio\\IdeaProjects\\JAVAFX\\javafx-CarCrimeGame\\src\\main\\resources\\city.json";

    // only "saveCityTask" modifies those variable

    private boolean isSavingCity = false;

    private long saveCityElapsedTime = 0;

    private final Runnable saveCityTask = new Runnable() {
        @Override
        public void run() {
            System.out.println("Saving City...");
            isSavingCity = true;
            long t1 = System.nanoTime();
            CityIO.saveCity(path, city);
            long t2 = System.nanoTime();
            saveCityElapsedTime = t2 - t1;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("City saved!");
            isSavingCity = false;
        }
    };

    ////

    @Override
    public void initialize(GameApplication gc) {
        Image imageCar = new Image("/assets/vehicles/car_top1.png");
        carModel = MeshFactory.getModel("/assets/vehicles/citroen/model_car.obj");
        car = new Car(imageCar);
        // city = new City(64, 32);
        city = CityIO.loadCity(path);
        // At initialize, fill the bounding boxes array with the data from the loaded city
        for (CityCell cityCell : city.getCells()) {
            if (cityCell.isBuilding()) {
                cityCell.setSolid(cityCell.isBuilding());
                boundingBoxes.add(cityCell.getBoundingBox());
            }
        }

        cityTextures = getTextures(new ImageTile("/assets/City_Roads1_mip1.png", 96, 96));

        pipe = new PipeLine(gc.getRenderer());
        pipe.getRenderer3D().setRenderFlag(RenderFlags.RENDER_FULL_TEXTURED);
        cityRender = new CityRender(pipe);

        // World Mouse
        mouseWorld = new Vec2di();
        oldMouseWorld = new Vec2di();

        // Selected cells
        selectedCells = new HashSet<>();
    }

    private Image[] getTextures(ImageTile resources) {
        Image[] roads = new Image[20];
        for ( int x = 0; x < resources.getW() / resources.getTileWidth(); x++ ) {
            for ( int y = 0; y < resources.getH() / resources.getTileHeight(); y++ ) {
                roads[y * 4 + x] = resources.getTileImage(x, y);
            }
        }
        return roads;
    }

    private void updateCar(GameApplication gc, float dt) {
        float carAngle = car.getRotation();

        if ( gc.getInput().isKeyHeld(KeyCode.D) ) {
            carAngle += 4.0f * dt;
        }
        if ( gc.getInput().isKeyHeld(KeyCode.A) ) {
            carAngle -= 4.0f * dt;
        }

        car.rotate(carAngle);

        if ( gc.getInput().isKeyHeld(KeyCode.W) ) {
            car.getPosition().addToX(car.getVelocity().getX() * car.getSpeed() * dt);
            car.getPosition().addToY(car.getVelocity().getY() * car.getSpeed() * dt);
        }
        if ( gc.getInput().isKeyHeld(KeyCode.S)) {
            car.getPosition().addToX(-car.getVelocity().getX() * car.getSpeed() * dt);
            car.getPosition().addToY(-car.getVelocity().getY() * car.getSpeed() * dt);
        }

        car.updateBoundingBox();
    }

    private void updateCamera(GameApplication gc, float dt) {
        // Panning
        pipe.getCameraObj().getOrigin().setX(car.getPosition().getX());
        pipe.getCameraObj().getOrigin().setY(car.getPosition().getY());
        // Zoom
        Vec4df forward = MatrixMath.vectorMul(pipe.getCameraObj().getLookDirection(), - gc.getInput().getScroll() * 0.5f * dt);
        pipe.getCameraObj().setOrigin(MatrixMath.vectorAdd(pipe.getCameraObj().getOrigin(), forward));
        // Update the mat view
        pipe.updateMatView();
    }

    private void updateMousePositionOnGroundPlane(GameApplication gc) {
        Vec4df mouse3d = pipe.getCameraObj().getIntersectionPoint(new Vec4df(
                -2.0f * (((float) gc.getInput().getMouseX() / (float) gc.getWidth()) - 0.5f) / pipe.getProjectionMatrix().getM()[0][0],
                -2.0f * (((float) gc.getInput().getMouseY() / (float) gc.getHeight()) - 0.5f) / pipe.getProjectionMatrix().getM()[1][1],
                1.0f,
                0.0f
        ));

        if (gc.getInput().isButtonHeld(MouseButton.PRIMARY) &&
                ((mouseWorld.getX() != oldMouseWorld.getX()) ||
                        (mouseWorld.getY() != oldMouseWorld.getY()))
        ) {
            CityCell c = city.getCell(mouseWorld.getX(), mouseWorld.getY());
            if (c != null) {
                selectedCells.add(c);
            }
        }

        if (gc.getInput().isButtonDown(MouseButton.PRIMARY)) {
            CityCell c = city.getCell(mouseWorld.getX(), mouseWorld.getY());
            if (c != null) {
                selectedCells.add(c);
            }
        }

        if (gc.getInput().isButtonUp(MouseButton.SECONDARY)) {
            selectedCells.clear();
        }

        oldMouseWorld.setX(mouseWorld.getX());
        oldMouseWorld.setY(mouseWorld.getY());

        mouseWorld.setX((int) mouse3d.getX());
        mouseWorld.setY((int) mouse3d.getY());
    }

    private void updateCity(GameApplication gc) {
        if (gc.getInput().isKeyDown(KeyCode.R)) {
            if (!selectedCells.isEmpty()) {
                for (var cell : selectedCells) {
                    cell.setRoad(!cell.isRoad());
                    city.updateRoads();
                }
            } else {
                CityCell cell = city.getCell(mouseWorld.getX(), mouseWorld.getY());
                cell.setRoad(!cell.isRoad());
                city.updateRoads();
            }
        }
        if (gc.getInput().isKeyDown(KeyCode.T)) {
            if (!selectedCells.isEmpty()) {
                for (var cell : selectedCells) {
                    cell.setBuilding(!cell.isBuilding());
                    cell.setSolid(cell.isBuilding());
                    if (cell.isSolid()) {
                        boundingBoxes.add(cell.getBoundingBox());
                    }
                }
            } else {
                CityCell cell = city.getCell(mouseWorld.getX(), mouseWorld.getY());
                cell.setBuilding(!cell.isBuilding());
                cell.setSolid(cell.isBuilding());
                if (cell.isSolid()) {
                    boundingBoxes.remove(cell.getBoundingBox());
                }
            }
        }

        // todo: code of control if the cell is water. Now this feature is not implemented in class CityCell
    }

    @Override
    public void update(GameApplication gc, float elapsedTime) {
        updateCar(gc, elapsedTime);
        updateCamera(gc, elapsedTime);
        updateMousePositionOnGroundPlane(gc);
        updateCity(gc);

        // Check for overlaps
        for (var p : boundingBoxes) {
            p.setOverlap(false);
            p.setOverlap(p.isOverlap() || ConvexPolygonCollisions.shapeOverlapDIAGSStatic(car.getBoundingBox(), p));
            car.setPosToBuildingBox();
        }

        // Save city
        if (gc.getInput().isKeyDown(KeyCode.SPACE)) {
            new Thread(saveCityTask).start();
        }

    }

    @Override
    public void render(GameApplication gc) {
        gc.getRenderer().clear(HexColors.ROYAL_BLUE);

        pipe.getRenderer3D().setRenderFlag(RenderFlags.RENDER_FULL_TEXTURED);
        cityRender.renderCity(city, cityTextures);
        car.render(carModel, pipe);
        //car.render(pipe);

        pipe.getRenderer3D().setRenderFlag(RenderFlags.RENDER_WIRE);
        for (var cell : selectedCells) {
            Mat4x4 translation = MatrixMath.matrixMakeTranslation(cell.getPos().getX(), cell.getPos().getY(), 0.0f);
            pipe.setTransform(translation);
            pipe.renderMesh(MeshFactory.getFlat());
        }

        pipe.getRenderer3D().clearDepthBuffer();

        if (isSavingCity) {
            gc.getRenderer().drawText(
                    String.format("City saved! (in %.6f seg)", saveCityElapsedTime / 1000000000.0f),
                    10, 10, HexColors.YELLOW);
        }

        /*car.getBoundingBox().render(gc);
        for (Polygon polygon : polygons) {
            polygon.render(gc);
        }*/
    }
}
