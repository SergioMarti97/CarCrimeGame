package game;

import base.AbstractGame;
import base.GameApplication;
import base.graphics.HexColors;
import base.graphics.image.Image;
import base.graphics.image.ImageTile;
import base.vectors.points2d.Vec2df;
import base.vectors.points2d.Vec2di;
import collisions.ConvexPolygonCollisions;
import collisions.Polygon;
import collisions.PolygonDrawUtils;
import engine3d.PipeLine;
import engine3d.RenderFlags;
import engine3d.matrix.Mat4x4;
import engine3d.matrix.MatrixMath;
import engine3d.mesh.MeshFactory;
import engine3d.mesh.Model;
import engine3d.vectors.Vec4df;
import game.vehicle.Vehicle;
import game.city.City;
import game.city.CityCell;
import game.city.CityIO;
import game.city.CityRender;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import menu.MenuAction;
import menu.MenuIO;
import menu.MenuManager;
import menu.MenuObject;
import panAndZoom.PanAndZoomRenderer;
import panAndZoom.PanAndZoomUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CityGame extends AbstractGame {

    // Data

    private Vehicle car;

    private City city;

    // Renderer

    private PipeLine pipe;

    private CityRender cityRender;

    private PanAndZoomRenderer renderer2D;

    private Model carModel;

    private Image[] cityTextures;

    private boolean drawCarModel = true;

    private boolean isDebug = true;

    // User input management

    private Vec2di mouseWorld;

    private Vec2di oldMouseWorld;

    private HashSet<CityCell> selectedCells;

    // Collision detection

    private final ArrayList<Polygon> boundingBoxes = new ArrayList<>();

    // Menu

    private MenuObject mo;

    private MenuManager mm;

    private HashMap<Integer, MenuAction> menuActions;

    private ImageTile menuGraphics;

    // Save data

    private final String path = "C:\\Users\\Sergio\\IdeaProjects\\JAVAFX\\javafx-CarCrimeGame\\src\\main\\resources";

    private final String cityFileName = "city.json";

    private final String menuFileName = "menu.json";

    private boolean isSavingCity = false;

    private long saveCityElapsedTime = 0;

    private final Runnable saveCityTask = new Runnable() {
        @Override
        public void run() {
            System.out.println("Saving City...");
            isSavingCity = true;
            long t1 = System.nanoTime();
            CityIO.saveCity(path + File.separator + cityFileName, city);
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

    @Override
    public void initialize(GameApplication gc) {
        Image imageCar = new Image("/graphics/vehicle/car_top1.png");
        carModel = MeshFactory.getModel("/assets/vehicles/citroen/model_car.obj");

        car = new Vehicle(imageCar);
        city = CityIO.loadCity(path + File.separator + cityFileName);
        // At initialize, fill the bounding boxes array with the data from the loaded city
        for (CityCell cityCell : city.getCells()) {
            if (cityCell.isBuilding()) {
                cityCell.setSolid(cityCell.isBuilding());
                boundingBoxes.add(cityCell.getBoundingBox());
            }
        }

        cityTextures = getCityTextures(new ImageTile("/graphics/city/City_Roads1_mip1.png", 96, 96));

        pipe = new PipeLine(gc.getRenderer());
        pipe.getRenderer3D().setRenderFlag(RenderFlags.RENDER_FULL_TEXTURED);
        cityRender = new CityRender(pipe);

        renderer2D = new PanAndZoomRenderer(gc.getRenderer());
        renderer2D.getWorldScale().set(50, 50);
        renderer2D.getWorldOffset().set(-18, -11);

        // World Mouse
        mouseWorld = new Vec2di();
        oldMouseWorld = new Vec2di();

        // Selected cells
        selectedCells = new HashSet<>();

        // Initialize menu
        initializeMenu();
    }

    private Image[] getCityTextures(ImageTile resources) {
        Image[] roads = new Image[20];
        for ( int x = 0; x < resources.getW() / resources.getTileWidth(); x++ ) {
            for ( int y = 0; y < resources.getH() / resources.getTileHeight(); y++ ) {
                roads[y * 4 + x] = resources.getTileImage(x, y);
            }
        }
        return roads;
    }

    //////////////////////
    
    private void setBuilding(Iterable<CityCell> cells) {
        for (var cell : cells) {
            cell.setBuilding(true);
            cell.setSolid(cell.isBuilding());
            boundingBoxes.add(cell.getBoundingBox());
        }
    }

    private void unsetBuilding(Iterable<CityCell> cells) {
        for (var cell : cells) {
            cell.setBuilding(false);
            cell.setBuilding(cell.isBuilding());
            boundingBoxes.remove(cell.getBoundingBox());
        }
    }
    
    private void changeBuilding(Iterable<CityCell> cells) {
        for (var cell : cells) {
            cell.setBuilding(!cell.isBuilding());
            cell.setSolid(cell.isBuilding());
            if (cell.isSolid()) {
                boundingBoxes.add(cell.getBoundingBox());
            } else {
                boundingBoxes.remove(cell.getBoundingBox());
            }
        }
    }

    private void setRoads(Iterable<CityCell> cells) {
        for (var cell : cells) {
            cell.setRoad(true);
            city.updateRoads();
        }
    }

    private void unsetRoads(Iterable<CityCell> cells) {
        for (var cell : cells) {
            cell.setRoad(false);
            city.updateRoads();
        }
    }

    private void changeRoads(Iterable<CityCell> cells) {
        for (var cell : cells) {
            cell.setRoad(!cell.isRoad());
            city.updateRoads();
        }
    }

    //////////////////////

    private void initializeMenu() {
        menuGraphics = new ImageTile("/graphics/menu/RetroMenu2.png", 16, 24);
        mm = new MenuManager();

        mo = MenuIO.loadMenu(path + File.separator + menuFileName);

        mo.build();

        menuActions = new HashMap<>();
        menuActions.put(101, () -> new Thread(saveCityTask).start());
        menuActions.put(102, () -> { city.initialize(); boundingBoxes.clear(); });
        menuActions.put(103, () -> {
            if (!selectedCells.isEmpty()) {
                setBuilding(selectedCells);
            }
        });
        menuActions.put(104, () -> {
            if (!selectedCells.isEmpty()) {
                unsetBuilding(selectedCells);
            }
        });
        menuActions.put(105, () -> {
            if (!selectedCells.isEmpty()) {
                changeBuilding(selectedCells);
            }
        });
        menuActions.put(106, () -> {
            if (!selectedCells.isEmpty()) {
                setRoads(selectedCells);
            }
        });
        menuActions.put(107, () -> {
            if (!selectedCells.isEmpty()) {
                unsetRoads(selectedCells);
            }
        });
        menuActions.put(108, () -> {
            if (!selectedCells.isEmpty()) {
                changeRoads(selectedCells);
            }
        });
        menuActions.put(110, () -> mm.setDrawTileBorders(true));
        menuActions.put(111, () -> mm.setDrawTileBorders(false));
        menuActions.put(112, () -> drawCarModel = true);
        menuActions.put(113, () -> drawCarModel = false);
        menuActions.put(114, () -> isDebug = true);
        menuActions.put(115, () -> isDebug = false);
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

    public void checkCollisions() {
        // Check for overlaps
        for (var p : boundingBoxes) {
            p.setOverlap(false);
            p.setOverlap(p.isOverlap() || ConvexPolygonCollisions.shapeOverlapDIAGSStatic(car.getBoundingBox(), p));
            car.setPosToBuildingBox();
        }
    }

    private void updateCamera(GameApplication gc, float dt) {
        // Panning
        pipe.getCameraObj().getOrigin().setX(car.getPosition().getX());
        pipe.getCameraObj().getOrigin().setY(car.getPosition().getY());

        // Zoom
        Vec4df forward = MatrixMath.vectorMul(pipe.getCameraObj().getLookDirection(), - gc.getInput().getScroll() * 0.5f * dt);
        pipe.getCameraObj().setOrigin(MatrixMath.vectorAdd(pipe.getCameraObj().getOrigin(), forward));

        if (gc.getInput().isKeyHeld(KeyCode.PLUS)) {
            Vec4df f = MatrixMath.vectorMul(pipe.getCameraObj().getLookDirection(), - 1 * 0.5f * dt);
            pipe.getCameraObj().setOrigin(MatrixMath.vectorAdd(pipe.getCameraObj().getOrigin(), f));
        }
        if (gc.getInput().isKeyHeld(KeyCode.MINUS)) {
            Vec4df f = MatrixMath.vectorMul(pipe.getCameraObj().getLookDirection(), 1 * 0.5f * dt);
            pipe.getCameraObj().setOrigin(MatrixMath.vectorAdd(pipe.getCameraObj().getOrigin(), f));
        }

        // Update the mat view
        pipe.updateMatView();
    }

    private void updateMouseInput(GameApplication gc) {
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
    }

    private void updateMousePositionOn3DPlane(GameApplication gc) {
        Vec4df mouse3d = pipe.getCameraObj().getIntersectionPoint(new Vec4df(
                -2.0f * (((float) gc.getInput().getMouseX() / (float) gc.getWidth()) - 0.5f) / pipe.getProjectionMatrix().getM()[0][0],
                -2.0f * (((float) gc.getInput().getMouseY() / (float) gc.getHeight()) - 0.5f) / pipe.getProjectionMatrix().getM()[1][1],
                1.0f,
                0.0f
        ));

        updateMouseInput(gc);

        oldMouseWorld.setX(mouseWorld.getX());
        oldMouseWorld.setY(mouseWorld.getY());

        mouseWorld.setX((int) mouse3d.getX());
        mouseWorld.setY((int) mouse3d.getY());
    }

    private void updateMousePositionOn2DPlane(GameApplication gc) {
        renderer2D.handlePanAndZoom(gc, MouseButton.MIDDLE, 0.001f, true, true);

        Vec2df mouse2d = PanAndZoomUtils.screenToWorld(
                new Vec2df(
                        (float) gc.getInput().getMouseX(), // - renderer2D.getWidth(),
                        (float) gc.getInput().getMouseY() //- renderer2D.getHeight()
                ),
                renderer2D.getWorldOffset(),
                renderer2D.getWorldScale()
        );

        updateMouseInput(gc);

        oldMouseWorld.setX(mouseWorld.getX());
        oldMouseWorld.setY(mouseWorld.getY());

        mouseWorld.setX((int)(mouse2d.getX()));
        mouseWorld.setY((int)(mouse2d.getY()));
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

    private void updateMenu(GameApplication gc) {
        if (gc.getInput().isKeyUp(KeyCode.M)) {
            mm.open(mo);
        }
        if (gc.getInput().isKeyUp(KeyCode.UP)) {
            mm.onUp();
        }
        if (gc.getInput().isKeyUp(KeyCode.DOWN)) {
            mm.onDown();
        }
        if (gc.getInput().isKeyUp(KeyCode.LEFT)) {
            mm.onLeft();
        }
        if (gc.getInput().isKeyUp(KeyCode.RIGHT)) {
            mm.onRight();
        }
        if (gc.getInput().isKeyUp(KeyCode.Z)) {
            mm.onBack();
        }
        if (gc.getInput().isKeyUp(KeyCode.ESCAPE)) {
            mm.clear();
        }

        MenuObject command = null;

        if (gc.getInput().isKeyUp(KeyCode.SPACE)) {
            command = mm.onConfirm();
        }

        if (command != null) {

            if (menuActions.containsKey(command.getId())) {
                menuActions.get(command.getId()).doAction();
            }

            if (!command.hasChildren()) {
                mm.clear();
            }
        }
    }

    @Override
    public void update(GameApplication gc, float elapsedTime) {
        updateCar(gc, elapsedTime);
        checkCollisions();

        if (isDebug) {
            updateMousePositionOn2DPlane(gc);
        } else {
            updateCamera(gc, elapsedTime);
            updateMousePositionOn3DPlane(gc);
        }

        updateCity(gc);
        updateMenu(gc);
    }

    private void render2DWorld(GameApplication gc) {
        // Colors
        int colorDefault = HexColors.WHITE;
        int colorSolid = HexColors.YELLOW;
        int colorRoad = HexColors.BLACK;
        int colorCollision = HexColors.RED;
        int colorSelect = HexColors.GREEN;

        Vec2df cellSize = new Vec2df(1, 1);

        // Draw default cells
        for (var cell : city.getCells()) {
            Vec2df pos = new Vec2df(cell.getPos().getX(), cell.getPos().getY());
            //pos.multiply(-1);

            if (cell.isSolid()) {
                renderer2D.drawFillRect(pos, cellSize, colorSolid);
            } else if (cell.isRoad()) {
                renderer2D.drawFillRect(pos, cellSize, colorRoad);
            } else {
                renderer2D.drawRect(pos, cellSize, colorDefault);
            }

        }

        for (var cell : selectedCells) {
            Vec2df pos = new Vec2df(cell.getPos().getX(), cell.getPos().getY());
            //pos.multiply(-1);
            renderer2D.drawRect(pos, cellSize, colorSelect);
        }

        renderer2D.drawText("World mouse: " + mouseWorld, 10, 10, HexColors.GREEN);
        renderer2D.drawText("Num selected cells: " + selectedCells.size(), 10, 40, HexColors.GREEN);
        renderer2D.drawText("World offset: " + renderer2D.getWorldOffset(), 10, 70, HexColors.GREEN);
        renderer2D.drawText("World scale: " + renderer2D.getWorldScale(), 10, 100, HexColors.GREEN);
        renderer2D.drawText(String.format("Screen mouse: %.3fx %.3fy", gc.getInput().getMouseX(), gc.getInput().getMouseY()), 10, 130, HexColors.GREEN);

        // Draw bounding boxes
        /*PolygonDrawUtils.drawPolygon(
                gc.getRenderer(),
                car.getBoundingBox(),
                renderer2D.getWorldOffset(),
                renderer2D.getWorldScale(),
                colorDefault, colorCollision);
        for (Polygon polygon : boundingBoxes) {
            PolygonDrawUtils.drawPolygon(
                    gc.getRenderer(),
                    polygon,
                    renderer2D.getWorldOffset(),
                    renderer2D.getWorldScale(),
                    colorDefault, colorCollision);
        }*/

    }

    private void render3DWorld(GameApplication gc) {
        pipe.getRenderer3D().setRenderFlag(RenderFlags.RENDER_FULL_TEXTURED);
        cityRender.renderCity(city, cityTextures);

        if (drawCarModel) {
            car.render(carModel, pipe);
        } else {
            car.render(pipe);
        }

        pipe.getRenderer3D().setRenderFlag(RenderFlags.RENDER_WIRE);
        for (var cell : selectedCells) {
            Mat4x4 translation = MatrixMath.matrixMakeTranslation(cell.getPos().getX(), cell.getPos().getY(), 0.0f);
            pipe.setTransform(translation);
            pipe.renderMesh(MeshFactory.getFlat());
        }

        pipe.getRenderer3D().clearDepthBuffer();
    }

    @Override
    public void render(GameApplication gc) {
        gc.getRenderer().clear(HexColors.ROYAL_BLUE);

        if (!isDebug) {
            render3DWorld(gc);
        } else {
            render2DWorld(gc);
        }

        mm.draw(gc.getRenderer(), menuGraphics, new Vec2di(50, 50));

        if (isSavingCity) {
            gc.getRenderer().drawText(
                    String.format("City saved! (in %.6f seg)", saveCityElapsedTime / 1000000000.0f),
                    10, 40, HexColors.YELLOW);
        }
    }

    @Override
    public void stop(GameApplication gc) {
        super.stop(gc);
        MenuIO.saveMenu(path + File.separator + menuFileName, mo);
    }

}
