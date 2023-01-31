package base.graphics.image;

import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;

import java.nio.IntBuffer;

public class Image {

    protected int width;

    protected int height;

    protected int[] pixels;

    protected boolean alpha = false;

    public Image() {
        width = 0;
        height = 0;
        pixels = new int[0];
    }

    public Image(int[] pixels, int width, int height) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
    }

    public Image(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
    }

    public Image(String path) {
        javafx.scene.image.Image img = new javafx.scene.image.Image(path);
        width = (int)img.getWidth();
        height = (int)img.getHeight();
        pixels = getPixels(img, 0, 0, width, height);
    }

    public int[] getPixels(javafx.scene.image.Image img, int x, int y, int w, int h) {
        int[] pixels = new int[w * h];

        PixelReader reader = img.getPixelReader();
        PixelFormat.Type type =  reader.getPixelFormat().getType();
        WritablePixelFormat<IntBuffer> format;

        if (type == PixelFormat.Type.INT_ARGB_PRE) {
            format = PixelFormat.getIntArgbPreInstance();
        } else {
            format = PixelFormat.getIntArgbInstance();
        }

        try {
            reader.getPixels(x, y, w, h, format, pixels, 0, w);
        } catch ( ArrayIndexOutOfBoundsException e ) {
            System.out.println(e.getMessage());
        }

        return pixels;
    }

    public int getPixel(int x, int y) throws ArrayIndexOutOfBoundsException {
        int index = x + width * y;
        assert pixels != null;
        if ( index < pixels.length ) {
            return pixels[x + width * y];
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public int getSample(float x, float y) {
        int color;
        int sampleX = Math.min((int)(x * (float)width), width > 0 ? width - 1 : width);
        int sampleY = Math.min((int)(y * (float)height), height > 0 ? height - 1 : height);
        try {
            color = getPixel(sampleX, sampleY);
        } catch ( ArrayIndexOutOfBoundsException e ) {
            color = 0x00000000;
            String errorMessage = "X: " + x + " Y: " + y + " outside of " + getW() + "x" + getH();
            System.out.println("Get sample Error: " + errorMessage + e.getMessage());
        }
        return color;
    }

    // Getters & Setters

    public int getW() {
        return width;
    }

    public int getH() {
        return height;
    }

    public int[] getP() {
        return pixels;
    }

    public boolean isAlpha() {
        return alpha;
    }

    public void setW(int w) {
        this.width = w;
    }

    public void setH(int h) {
        this.height = h;
    }

    public void setP(int[] p) {
        this.pixels = p;
    }

    public void set(int x, int y, int pixel) {
        pixels[x + width * y] = pixel;
    }

    public int get(int x, int y) {
        return pixels[x + width * y];
    }

    public void setAlpha(boolean alpha) {
        this.alpha = alpha;
    }

}
