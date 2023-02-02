package game.city;

import base.vectors.points2d.Vec2di;

public class City {

    private Vec2di dimensions;

    private CityCell[] cells;

    public City(int numCellsX, int numCellsY) {
        this.dimensions = new Vec2di(numCellsX, numCellsY);
        initialize();
    }

    private void initialize(Vec2di dimensions) {
        cells = new CityCell[dimensions.getX() * dimensions.getY()];

        for (int x = 0; x < dimensions.getX(); x++) {
            for (int y = 0; y < dimensions.getY(); y++) {
                cells[y * dimensions.getX() + x] = new CityCell(0, x, y);
            }
        }
    }

    public void initialize() {
        initialize(dimensions);
    }

    private boolean isRoad(int positionX, int positionY, int i, int j) {
        int finalX = positionX + i;
        int finalY = positionY + j;
        if ( finalX < dimensions.getX() && finalY < dimensions.getY() && finalX >= 0 && finalY >= 0 ) {
            return getCell(finalX, finalY).isRoad();
        } else {
            return false;
        }
    }

    private boolean isRoad(CityCell cityCell, int i, int j) {
        return isRoad(cityCell.getPos().getX(), cityCell.getPos().getY(), i, j);
    }

    private int getRoadIndex(CityCell cityCell) {
        if ( isRoad(cityCell, 0, +1) && isRoad(cityCell, 0, -1) &&
                !isRoad(cityCell, +1, 0) && !isRoad(cityCell, -1, 0) ) {
            return  0;
        }
        if ( !isRoad(cityCell, 0, +1) && !isRoad(cityCell, 0, -1) &&
                isRoad(cityCell, +1, 0) && isRoad(cityCell, -1, 0) ) {
            return 1;
        }

        if ( !isRoad(cityCell, 0, +1) && isRoad(cityCell, 0, -1) &&
                !isRoad(cityCell, +1, 0) && isRoad(cityCell, -1, 0) ) {
            return 6; // 4
        }
        if ( !isRoad(cityCell, 0, +1) && isRoad(cityCell, 0, -1) &&
                isRoad(cityCell, +1, 0) && isRoad(cityCell, -1, 0) ) {
            return 5;
        }
        if ( !isRoad(cityCell, 0, +1) && isRoad(cityCell, 0, -1) &&
                isRoad(cityCell, +1, 0) && !isRoad(cityCell, -1, 0) ) {
            return 4; // 6
        }

        if ( isRoad(cityCell, 0, +1) && isRoad(cityCell, 0, -1) &&
                !isRoad(cityCell, +1, 0) && isRoad(cityCell, -1, 0) ) {
            return 10; // 8
        }
        if ( isRoad(cityCell, 0, +1) && isRoad(cityCell, 0, -1) &&
                isRoad(cityCell, +1, 0) && isRoad(cityCell, -1, 0) ) {
            return 9;
        }
        if ( isRoad(cityCell, 0, +1) && isRoad(cityCell, 0, -1) &&
                isRoad(cityCell, +1, 0) && !isRoad(cityCell, -1, 0) ) {
            return 8; // 10
        }

        if ( isRoad(cityCell, 0, +1) && !isRoad(cityCell, 0, -1) &&
                !isRoad(cityCell, +1, 0) && isRoad(cityCell, -1, 0) ) {
            return 14; // 12
        }
        if ( isRoad(cityCell, 0, +1) && !isRoad(cityCell, 0, -1) &&
                isRoad(cityCell, +1, 0) && isRoad(cityCell, -1, 0) ) {
            return 13;
        }
        if ( isRoad(cityCell, 0, +1) && !isRoad(cityCell, 0, -1) &&
                isRoad(cityCell, +1, 0) && !isRoad(cityCell, -1, 0) ) {
            return 12; // 14
        }

        return 0;
    }

    public void updateRoads() {
        for (var cell : cells) {
            if (cell.isRoad()) {
                cell.setImgIndex(getRoadIndex(cell));
            } else {
                cell.setImgIndex(2);
            }
        }
    }


    ////////////////////

    public Vec2di getDimensions() {
        return dimensions;
    }

    public void setDimensions(Vec2di dimensions) {
        CityCell[] old = cells;
        initialize(dimensions);
        for (int x = 0; x < this.dimensions.getX(); x++) {
            for (int y = 0; y < this.dimensions.getY(); y++) {
                if (getCell(x, y) != null) {
                    cells[y * dimensions.getX() + x] = old[y * this.dimensions.getX() + x];
                }
            }
        }
        this.dimensions = dimensions;
    }

    public void clearCity() {
        cells = new CityCell[dimensions.getX() * dimensions.getY()];
    }

    public CityCell[] getCells() {
        return cells;
    }

    public CityCell getCell(int x, int y) {
        if (x >= 0 && x < dimensions.getX() && y >= 0 && y < dimensions.getY()) {
            return cells[y * dimensions.getX() + x];
        } else {
            return null;
        }

    }

    public boolean setCell(int x, int y, CityCell cell) {
        if (x >= 0 && x < dimensions.getX() && y >= 0 && y < dimensions.getY()) {
            cells[y * dimensions.getX() + x] = cell;
            return true;
        } else {
            return false;
        }
    }

}
