package game.city;

import base.graphics.image.Image;
import engine3d.PipeLine;
import engine3d.mesh.MeshFactory;
import engine3d.mesh.Model;
import engine3d.transforms.Translation;
import engine3d.vectors.Vec4df;

import java.util.HashMap;

public class CityRender {

    private HashMap<Integer, Model> assets;

    private PipeLine pipe;

    private Model building;

    public CityRender(PipeLine pipe) {
        this.pipe = pipe;
        building = MeshFactory.getModel("/assets/buildings/building.obj"); // /assets/buildings/building.obj
    }

    public CityRender(PipeLine pipe, HashMap<Integer, Model> assets) {
        this.assets = assets;
        this.pipe = pipe;
    }

    /*public void renderCell(CityCell cell) {
        Translation trans = new Translation(cell.getPos().getX(), cell.getPos().getY(), 0.0f);
        pipe.setTransform(trans.getMat());
        Model model = assets.get(cell.getId());
        pipe.renderModel(model);
    }*/

    public void renderCell(CityCell cell, Image[] textures) {
        Translation trans = new Translation(cell.getPos().getX(), cell.getPos().getY(), 0.0f);

        if (cell.isRoad()) {
            trans.update();
            pipe.setTransform(trans.getMat());
            pipe.renderMesh(MeshFactory.getFlat(), textures[cell.getImgIndex()]);
        } else if (cell.isBuilding()) {
            trans.getDelta().addToX(+1);
            trans.getDelta().addToY(+1);
            trans.update();
            pipe.setTransform(trans.getMat());
            pipe.renderModel(building);
        } else {
            trans.update();
            pipe.setTransform(trans.getMat());
            pipe.renderMesh(MeshFactory.getFlat(), textures[cell.getImgIndex()]);
        }

    }

    public void renderCity(City city, Image[] textures) {
        Vec4df viewWorldTopLeft = pipe.getCameraObj().getIntersectionPoint(new Vec4df(
           -1.0f / pipe.getProjectionMatrix().getM()[0][0],
           -1.0f / pipe.getProjectionMatrix().getM()[1][1],
                1.0f,
                0.0f
        ));
        Vec4df viewWorldBottomRight = pipe.getCameraObj().getIntersectionPoint(new Vec4df(
                1.0f / pipe.getProjectionMatrix().getM()[0][0],
                1.0f / pipe.getProjectionMatrix().getM()[1][1],
                1.0f,
                0.0f
        ));

        int startX = Math.max(0, (int)viewWorldTopLeft.getX() - 1);
        int endX = Math.min(city.getDimensions().getX(), (int)viewWorldBottomRight.getX() + 1);
        int startY = Math.max(0, (int)viewWorldTopLeft.getY() - 1);
        int endY = Math.min(city.getDimensions().getY(), (int)viewWorldBottomRight.getY() + 1);

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                renderCell(city.getCell(x, y), textures);
            }
        }
    }

    ////////////////////

    public HashMap<Integer, Model> getAssets() {
        return assets;
    }

    public void setAssets(HashMap<Integer, Model> assets) {
        this.assets = assets;
    }

    public PipeLine getPipe() {
        return pipe;
    }

    public void setPipe(PipeLine pipe) {
        this.pipe = pipe;
    }
}
