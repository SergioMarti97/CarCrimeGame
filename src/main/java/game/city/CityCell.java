package game.city;

import base.vectors.points2d.Vec2di;

public class CityCell {

    private int id;

    private Vec2di pos;

    ///////////////////////////

    private boolean isRoad = false;

    private boolean isBuilding = false;

    private int height = 0;

    private int imgIndex = 2;

    ///////////////////////////

    public CityCell(int id, int x, int y) {
        this.id = id;
        this.pos = new Vec2di(x, y);
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
    }

    public void addHeight(int increase) {
        this.height += increase;
    }

    public int getImgIndex() {
        return imgIndex;
    }

    public void setImgIndex(int imgIndex) {
        this.imgIndex = imgIndex;
    }

    ///////////////////////////

}
