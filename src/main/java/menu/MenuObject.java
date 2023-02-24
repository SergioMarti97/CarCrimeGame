package menu;

import base.graphics.HexColors;
import base.graphics.Renderer;
import base.graphics.image.ImageTile;
import base.vectors.points2d.Vec2di;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuObject {

    private int id = -1;

    private String name;

    private boolean isEnabled = true;

    private int totalRows = 0;

    private int topVisibleRow = 0;

    private final Vec2di cellTable = new Vec2di(1, 0);

    private final Vec2di cellSize = new Vec2di();

    private final Vec2di cellPadding = new Vec2di(2, 0);

    private final Vec2di patchSize = new Vec2di(16, 24);

    private final Vec2di sizeInPatches = new Vec2di();

    // Data

    private final HashMap<String, Integer> itemPointer = new HashMap<>();

    private final ArrayList<MenuObject> items = new ArrayList<>();

    // Cursor

    private final Vec2di cellCursor = new Vec2di();

    private int cursorItem = 0;

    private final Vec2di cursorPos = new Vec2di();

    public MenuObject() {
        name = "root";
    }

    public MenuObject(String name) {
        this.name = name;
    }

    public MenuObject(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void build() {
        /*
         * Recursively build all children, so they can determine their size, use
         * that size to indicate cell sizes if this object contains more than
         * one item
         */
        for (MenuObject item : items) {
            if (item.hasChildren()) {
                item.build();
            }
            // Longest child name determines cell width
            cellSize.setX(Math.max(item.getSize().getX(), cellSize.getX()));
            cellSize.setY(Math.max(item.getSize().getY(), cellSize.getY()));
        }
        // Calculate how many rows this item has to hold
        totalRows = (items.size() / cellTable.getX()) + (((items.size() % cellTable.getX()) > 0) ? 1 : 0);

        sizeInPatches.setX(cellTable.getX() * cellSize.getX() + (cellTable.getX() - 1) * cellPadding.getX() + 2);
        sizeInPatches.setY(cellTable.getY() * cellSize.getY() + (cellTable.getY() - 1) * cellPadding.getY() + 2);
    }

    private void drawPanel(Renderer r, ImageTile img, Vec2di screenOffset, boolean drawTiles) {
        int x, y;
        for (x = 0; x < sizeInPatches.getX(); x++) {
            for (y = 0; y < sizeInPatches.getY(); y++) {
                // Determine position in screen space
                int screenLocX = x * patchSize.getX() + screenOffset.getX();
                int screenLocY = y * patchSize.getY() + screenOffset.getY();

                // Calculate which path is needed
                int sourcePatchX = 0;
                int sourcePatchY = 0;
                if (x > 0) {
                    sourcePatchX = 1;
                }
                if (x == sizeInPatches.getX() - 1) {
                    sourcePatchX = 2;
                }
                if (y > 0) {
                    sourcePatchY = 1;
                }
                if (y == sizeInPatches.getY() - 1) {
                    sourcePatchY = 2;
                }

                r.drawImage(img.getTileImage(sourcePatchX, sourcePatchY), screenLocX, screenLocY);
                if (drawTiles) {
                    r.drawRect(screenLocX, screenLocY, patchSize.getX(), patchSize.getY(), HexColors.MAGENTA);
                }
            }
        }
    }

    private void drawMenuContent(Renderer r, ImageTile img, Vec2di screenOffset) {
        int cellX = 0;
        int cellY = 0;
        int patchPosX = 1;
        int patchPosY = 1;

        // Work out visible items
        int topLeftItem = topVisibleRow * cellTable.getX();
        int bottomRightItem = cellTable.getY() * cellTable.getX() + topLeftItem;

        // Clamp to size of the child item vector
        bottomRightItem = Math.min(items.size(), bottomRightItem);
        int visibleItems = bottomRightItem - topLeftItem;

        // Draw scroll markers (if is required)
        if (topVisibleRow > 0) {
            patchPosX = sizeInPatches.getX() - 2;
            patchPosY = 0;
            int screenLocX = patchPosX * patchSize.getX() + screenOffset.getX();
            int screenLocY = patchPosY * patchSize.getY() + screenOffset.getX();
            r.drawImage(img.getTileImage(3, 0), screenLocX, screenLocY);
        }

        if ((totalRows - topVisibleRow) > cellTable.getY()) {
            patchPosX = sizeInPatches.getX() - 2;
            patchPosY = sizeInPatches.getY() - 1;
            int screenLocX = patchPosX * patchSize.getX() + screenOffset.getX();
            int screenLocY = patchPosY * patchSize.getY() + screenOffset.getY();
            r.drawImage(img.getTileImage(3, 2), screenLocX, screenLocY);
        }

        // Draw visible items
        for (int i = 0; i < visibleItems; i++) {
            // Cell location
            cellX = i % cellTable.getX();
            cellY = i / cellTable.getX();

            // Patch location (including border offset and padding)
            patchPosX = cellX * (cellSize.getX() + cellPadding.getX()) + 1;
            patchPosY = cellY * (cellSize.getY() + cellPadding.getY()) + 1;

            // Actual screen location in pixels
            int screenLocX = patchPosX * patchSize.getX() + screenOffset.getX();
            int screenLocY = patchPosY * patchSize.getY() + screenOffset.getY();

            r.drawText(items.get(topLeftItem + i).getName(), screenLocX, screenLocY, items.get(topLeftItem + i).isEnabled() ? HexColors.WHITE : HexColors.GREY);

            if (items.get(topLeftItem + i).hasChildren()) {
                // Display indicator that panel has a sub panel
                patchPosX = cellX * (cellSize.getX() + cellPadding.getX()) + 1 + cellSize.getX();
                patchPosY = cellY * (cellSize.getY() + cellPadding.getY()) + 1;
                screenLocX = patchPosX * patchSize.getX() + screenOffset.getX();
                screenLocY = patchPosY * patchSize.getY() + screenOffset.getY();
                r.drawImage(img.getTileImage(3, 1), screenLocX, screenLocY);
            }
        }
    }

    private void calCursorPosition(Vec2di screenOffset) {
        cursorPos.setX((cellCursor.getX() * (cellSize.getX() + cellPadding.getX())) * patchSize.getX() + screenOffset.getX() - patchSize.getX());
        cursorPos.setY(((cellCursor.getY() - topVisibleRow) * (cellSize.getY() + cellPadding.getY())) * patchSize.getY() + screenOffset.getY() + patchSize.getY());
    }

    public void drawSelf(Renderer r, ImageTile img, Vec2di screenOffset, boolean drawTileBorders) {
        drawPanel(r, img, screenOffset, drawTileBorders);
        drawMenuContent(r, img, screenOffset);
        calCursorPosition(screenOffset);
    }

    // Actions

    public void onUp() {
        cellCursor.addToY(-1);
        if (cellCursor.getY() < 0) {
            cellCursor.setY(0);
        }

        if (cellCursor.getY() < topVisibleRow) {
            topVisibleRow--;
            if (topVisibleRow < 0) {
                topVisibleRow = 0;
            }
        }

        clampCursor();
    }

    public void onDown() {
        cellCursor.addToY(1);
        if (cellCursor.getY() == totalRows) {
            cellCursor.setY(totalRows - 1);
        }

        if (cellCursor.getY() > (topVisibleRow + cellTable.getY()- 1)) {
            topVisibleRow++;
            if (topVisibleRow > (totalRows - cellTable.getY())) {
                topVisibleRow = totalRows - cellTable.getY();
            }
        }

        clampCursor();
    }

    public void onLeft() {
        cellCursor.addToX(-1);
        if (cellCursor.getX() < 0) {
            cellCursor.setX(0);
        }
        clampCursor();
    }

    public void onRight() {
        cellCursor.addToX(1);
        if (cellCursor.getX() == cellTable.getX()) {
            cellCursor.setX(cellTable.getX() - 1);
        }
        clampCursor();
    }

    public MenuObject onConfirm() {
        if (items.get(cursorItem).hasChildren()) {
            return items.get(cursorItem);
        } else {
            return this;
        }
    }

    private void clampCursor() {
        // find item in children
        cursorItem = cellCursor.getY() * cellTable.getX() + cellCursor.getX();

        // Clamp cursor
        if (cursorItem >= items.size()) {
            cellCursor.setY((items.size() / cellTable.getX()));
            cellCursor.setX((items.size() % cellTable.getX()) - 1);
            cursorItem = items.size() - 1;
        }
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean hasChildren() {
        return !items.isEmpty();
    }

    public Vec2di getSize() {
        return new Vec2di(name.length(), 1);
    }

    public Vec2di getCursorPos() {
        return cursorPos;
    }

    public Vec2di getCellTable() {
        return cellTable;
    }

    public MenuObject getSelectedItem() {
        return items.get(cursorItem);
    }

    public MenuObject get(String name) {
        return items.get(itemPointer.get(name));
    }

    public MenuObject add(String optionName) {
        if ( !itemPointer.containsKey(optionName) ) {
            itemPointer.put(optionName, items.size());
            items.add(new MenuObject(optionName));
        }
        return get(optionName);
    }

    public MenuObject add(MenuObject mo) {
        if (!itemPointer.containsKey(mo.getName())) {
            itemPointer.put(mo.getName(), items.size());
            items.add(mo);
        }
        return get(mo.getName());
    }

    public ArrayList<MenuObject> getItems() {
        return items;
    }

    public MenuObject setId(int id) {
        this.id = id;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuObject setTable(int numColumns, int numRows) {
        cellTable.setX(numColumns);
        cellTable.setY(numRows);
        return this;
    }

    public MenuObject setEnabled(boolean enabled) {
        isEnabled = enabled;
        return this;
    }

    @Override
    public String toString() {
        return name + " children: " + items.size();
    }

}
