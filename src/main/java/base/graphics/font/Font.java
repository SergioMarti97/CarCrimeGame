package base.graphics.font;

import base.graphics.image.Image;

public class Font {

    private Image fontImage;

    private int[] offsets;

    private int[] widths;

    public Font(String path) {
        fontImage = new Image(path);
        offsets = new int[256];
        widths = new int[256];
        int unicode = 0;
        for ( int i = 0; i < fontImage.getW(); i++ ) {
            if ( fontImage.getP()[i] == 0xff0000ff ) {
                offsets[unicode] = i;
            }
            if ( fontImage.getP()[i] == 0xffffff00 ) {
                widths[unicode] = i - offsets[unicode];
                unicode++;
            }
        }
    }

    public Image getCharacterImage(int unicode) {
        int[] characterImage = new int[widths[unicode] * fontImage.getH()];
        for ( int y = 0; y < fontImage.getH(); y++ ) {
            for ( int x = 0; x < widths[unicode]; x++ ) {
                characterImage[x + y * widths[unicode]] = fontImage.getP()[(x + offsets[unicode]) + y * fontImage.getW()];
            }
        }
        return new Image(characterImage, widths[unicode], fontImage.getH());
    }

    public Image getFontImage() {
        return fontImage;
    }

    public int[] getOffsets() {
        return offsets;
    }

    public int[] getWidths() {
        return widths;
    }

    public void setFontImage(Image fontImage) {
        this.fontImage = fontImage;
    }

    public void setOffsets(int[] offsets) {
        this.offsets = offsets;
    }

    public void setWidths(int[] widths) {
        this.widths = widths;
    }

}
