package base.renderer;

import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;

import java.nio.IntBuffer;
import java.util.Arrays;

public class Renderer {

    private final int width;

    private final int height;

    private final int[] p;

    private final PixelBuffer<IntBuffer> pixelBuffer;

    public Renderer(int width, int height) {
        this.width = width;
        this.height = height;

        // Allocate memory
        IntBuffer buffer = IntBuffer.allocate(width * height);
        p = buffer.array();

        // Pixel Buffer
        PixelFormat<IntBuffer> format = PixelFormat.getIntArgbPreInstance();
        pixelBuffer = new PixelBuffer<>(width, height, buffer, format);
    }

    public void process() {
        pixelBuffer.updateBuffer(b -> null);
    }

    // Drawing methods

    public void clear(int color) {
        Arrays.fill(p, color);
    }

    public void setPixel(int x, int y, int color) {
        p[x + y * width] = color;
    }

    public void drawCircle(int x, int y, int radius, int color) {
        int x0 = 0;
        int y0 = radius;
        int d = 3 - 2 * radius;
        if ( radius == 0) {
            return;
        }

        while (y0 >= x0) // only formulate 1/8 of circle
        {
            setPixel(x + x0, y - y0, color);
            setPixel(x + y0, y - x0, color);
            setPixel(x + y0, y + x0, color);
            setPixel(x + x0, y + y0, color);
            setPixel(x - x0, y + y0, color);
            setPixel(x - y0, y + x0, color);
            setPixel(x - y0, y - x0, color);
            setPixel(x - x0, y - y0, color);
            if (d < 0) d += 4 * x0++ + 6;
            else d += 4 * (x0++ - y0--) + 10;
        }
    }

    public void drawFillRect(int x, int y, int w, int h, int color) {
        int x2 = x + w;
        int y2 = y + h;

        if (x < 0) x = 0;
        if (x >= width) x = width;
        if (y < 0) y = 0;
        if (y >= height) y = height;

        if (x2 < 0) x2 = 0;
        if (x2 >= width) x2 = width;
        if (y2 < 0) y2 = 0;
        if (y2 >= height) y2 = height;

        for (int i = x; i < x2; i++) {
            for (int j = y; j < y2; j++) {
                setPixel(i, j, color);
            }
        }
    }

    // Getters

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public PixelBuffer<IntBuffer> getPixelBuffer() {
        return pixelBuffer;
    }

}
