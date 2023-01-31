package base.graphics.image;

public class ImageTile extends Image {

    private final int tileWidth;

    private final int tileHeight;

    private final int numTilesX;

    private final int numTilesY;

    public ImageTile(String path, int tileWidth, int tileHeight) {
        super(path);
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        numTilesX = width / tileWidth;
        numTilesY = height / tileHeight;
    }

    public ImageTile(Image image, int tileWidth, int tileHeight) {
        super();
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.setW(image.getW());
        this.setH(image.getH());
        this.setP(image.getP());
        numTilesX = width / tileWidth;
        numTilesY = height / tileHeight;
    }

    public Image getTileImage(int tileX, int tileY) {
        int[] p = new int[tileWidth * tileHeight];
        for ( int y = 0; y < tileHeight; y++ ) {
            for ( int x = 0; x < tileWidth; x++ ) {
                try {
                    p[x + y * tileWidth] = this.getP()[(x + tileX * tileWidth) + (y + tileY * tileHeight) * this.getW()];
                } catch ( ArrayIndexOutOfBoundsException e ) {
                    p[x + y * tileWidth] = 0xffffffff;
                }
            }
        }
        return new Image(p, tileWidth, tileHeight);
    }

    public Image getTileImage(int index) {
        int x;
        int y = 0;

        while ( index >= numTilesX ) {
            index -= numTilesX;
            y++;
        }

        x = index;

        return getTileImage(x, y);
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

}
