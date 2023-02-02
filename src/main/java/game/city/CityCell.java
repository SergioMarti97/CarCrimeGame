package game.city;

import base.vectors.points2d.Vec2df;
import base.vectors.points2d.Vec2di;
import collisions.Polygon;
import collisions.PolygonFactory;

public class CityCell {

    private int id;

    private Vec2di pos;

    ///////////////////////////

    private boolean isRoad = false;

    private boolean isBuilding = false;

    private int height = 0;

    private int imgIndex = 2;

    ///////////////////////////

    private boolean isSolid = false;

    private Polygon boundingBox;

    ///////////////////////////

    public CityCell(int id, int x, int y) {
        this.id = id;
        this.pos = new Vec2di(x, y);

        boundingBox = PolygonFactory.buildCenteredSquare(x + 0.5f, y + 0.5f);
        boundingBox.update();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vec2di getPos() {
        return pos;
    }

    public void setPos(Vec2di pos) {
        this.pos = pos;
    }

    ///////////////////////////

    public boolean isRoad() {
        return isRoad;
    }

    public void setRoad(boolean road) {
        isRoad = road;
    }

    public boolean isBuilding() {
        return isBuilding;
    }

    public void setBuilding(boolean building) {
        isBuilding = building;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        isSolid = height != 0;
    }

    public void addHeight(int increase) {
        this.height += increase;
        isSolid = height != 0;
    }

    public int getImgIndex() {
        return imgIndex;
    }

    public void setImgIndex(int imgIndex) {
        this.imgIndex = imgIndex;
    }

    ///////////////////////////

    public boolean isSolid() {
        return isSolid;
    }

    public void setSolid(boolean solid) {
        isSolid = solid;
    }

    public Polygon getBoundingBox() {
        return boundingBox;
    }

    ///////////////////////////

}
