package base.renderer;

import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;

import java.nio.IntBuffer;
import java.util.Arrays;

/**
 * This class contains some
 * methods to draw in a matrix
 * of pixels
 * This class contains the drawing methods
 * Works with an array of pixels
 *
 * Are the methods are copied
 * from the olcPixelGameEngine.h
 * javidx9
 *
 * DRAWING ROUTINES:
 *  - draw
 *  - draw line
 *  - draw circle
 *  - draw fill circle
 *  - draw rect
 *  - draw fill rect
 *  - draw triangle
 *  - draw fill triangle
 *  - draw sprite
 *  - draw partial sprite
 *  - draw string
 *  - get text size
 *  - draw string properly
 *  - get text size properly
 *
 *  - clear
 *  - clear buffer
 *  - get font sprite
 *
 *  Decals
 *  - set decal mode
 *  - draw decal
 *  - draw partial decal
 *  - draw explicit decal
 *  - draw warped decal
 *  - draw partial warped decal
 *  - draw rotate decal
 *  - draw partial rotate decal
 *  - draw string decal
 *  - draw string properly decal
 *  - fill rect decal
 *  - gradient fill rect decal
 *  - draw polygon decal
 *  - draw line decal
 *  - draw rotate string decal
 *  - draw rotate string properly decal
 */
public class RendererOriginal {

    /**
     * Array of pixels
     */
    protected final int[] p;

    /**
     * Canvas width
     */
    protected int pW;

    /**
     * Canvas height
     */
    protected int pH;

    /**
     * Canvas inverse width (1.0F / pW)
     */
    protected final float invW;

    /**
     * Canvas inverse height (1.0F / pH)
     */
    protected final float invH;

    /**
     * The Z-buffer, needed for Alpha blending
     */
    protected int[] zb;

    /**
     * The depth in z axis of screen
     */
    protected int zDepth = 0;

    /**
     *
     */
    protected int[] lm;

    /**
     *
     */
    protected int[] lb;

    /**
     * Processing flag for the image processing
     */
    protected boolean processing = false;

    /**
     * The background color
     */
    protected int ambientColor = 0xffffffff;

    protected PixelBuffer<IntBuffer> pixelBuffer;

    /*public Renderer(int[] p, int pW, int pH) {
        this.p = p;
        this.pW = pW;
        this.pH = pH;

        invW = 1.0f / pW;
        invH = 1.0f / pH;

        zb = new int[p.length];
        lm = new int[p.length];
        lb = new int[p.length];
    }*/

    /**
     * Constructor
     * @param pW image width
     * @param pH image height
     */
    public RendererOriginal(int pW, int pH) {
        this.pW = pW;
        this.pH = pH;

        IntBuffer buffer = IntBuffer.allocate(pW * pH);
        p = buffer.array();
        PixelFormat<IntBuffer> format = PixelFormat.getIntArgbPreInstance();
        pixelBuffer = new PixelBuffer<>(pW, pH, buffer, format);

        invW = 1.0f / pW;
        invH = 1.0f / pH;

        zb = new int[p.length];
        lm = new int[p.length];
        lb = new int[p.length];
    }

    public void process() {
        pixelBuffer.updateBuffer(b -> null);
    }

    /**
     * This method interpolates color between to of them
     * @see "https://stackoverflow.com/questions/17544157/generate-n-colors-between-two-colors"
     */
    public static int interpolateColor(int x, int y, double blending) {
        double inverseBlending = 1 - blending;

        int xR = x >> 16 & 255;
        int xG = x >> 8 & 255;
        int xB = x & 255;

        int yR = y >> 16 & 255;
        int yG = y >> 8 & 255;
        int yB = y & 255;

        double r = xR * blending + yR * inverseBlending;
        double g = xG * blending + yG * inverseBlending;
        double b = xB * blending + yB * inverseBlending;

        return 255 << 24 | (int)r << 16 | (int)g << 8 | (int)b;
    }

    /**
     * This method clears all the screen with
     * the color passed as parameter
     *
     * Before it works well only with the method
     * "Arrays.fill(p, color)"
     *
     * @param color the color for clear all the screen
     */
    public void clear(int color) {
        Arrays.fill(p, color);
        Arrays.fill(zb, 0);
        Arrays.fill(lm, ambientColor);
        Arrays.fill(lb, 0);
    }

    /**
     * This method clears all the screen with
     * black
     */
    public void clear() {
        clear(0xff000000);
    }

    /**
     * This method sets only one pixel of the array to
     * the color specify in the parameter value
     *
     * Original way: p[y * pW + x] = color;
     *
     * @param x the x coordinate in screen
     * @param y the y coordinate in screen
     * @param color the color to set the pixel
     */
    public void setPixel(int x, int y, int color) {
        p[x + y * pW] = color;
        /*int alpha = ((color >> 24) & 0xff);
        if ( (x < 0 || x >= pW || y < 0 || y >= pH) || alpha == 0 ) { // value == 0xffff00ff
            return;
        }
        int index = x + y * pW;
        if ( zb[index] > zDepth ) {
            return;
        }
        zb[index] = zDepth;
        if ( alpha == 255 ) {
            p[index] = color;
        } else {
            int pixelColor = p[index];
            int newRed = ((pixelColor >> 16) & 0xff) - (int)((((pixelColor >> 16) & 0xff) - ((color >> 16) & 0xff)) * (alpha / 255.0f));
            int newGreen = ((pixelColor >> 8) & 0xff) - (int)((((pixelColor >> 8) & 0xff) - ((color >> 8) & 0xff)) * (alpha / 255.0f));
            int newBlue = (pixelColor & 0xff) - (int)(((pixelColor & 0xff) - (color & 0xff)) * (alpha / 255.0f));
            int pixel = (255 << 24 | newRed << 16 | newGreen << 8 | newBlue);
            p[index] = pixel;
        }*/
    }

    public void drawLine(int x1, int y1, int x2, int y2, int color) {
        int x, y, dx, dy, dx1, dy1, px, py, xe, ye;
        dx = x2 - x1; dy = y2 - y1;

        // straight lines idea by gurkanctn
        if (dx == 0) // Line is vertical
        {
            if (y2 < y1) {
                int temp = y1;
                y1 = y2;
                y2 = temp;
            }
            for (y = y1; y <= y2; y++) {
                setPixel(x1, y, color);
            }
            return;
        }

        if (dy == 0) // Line is horizontal
        {
            if (x2 < x1) {
                int temp = x1;
                x1 = x2;
                x2 = temp;
            }
            for (x = x1; x <= x2; x++) {
                setPixel(x, y1, color);
            }
            return;
        }

        // Line is Funk-aye
        dx1 = Math.abs(dx); dy1 = Math.abs(dy);
        px = 2 * dy1 - dx1;	py = 2 * dx1 - dy1;
        if (dy1 <= dx1)
        {
            if (dx >= 0)
            {
                x = x1; y = y1; xe = x2;
            }
            else
            {
                x = x2; y = y2; xe = x1;
            }

            setPixel(x, y, color);

            while (x < xe) {
                x = x + 1;
                if (px<0)
                    px = px + 2 * dy1;
                else
                {
                    if ((dx<0 && dy<0) || (dx>0 && dy>0)) y = y + 1; else y = y - 1;
                    px = px + 2 * (dy1 - dx1);
                }
                setPixel(x, y, color);
            }
        }
        else
        {
            if (dy >= 0)
            {
                x = x1; y = y1; ye = y2;
            }
            else
            {
                x = x2; y = y2; ye = y1;
            }

            setPixel(x, y, color);

            while (y<ye) {
                y = y + 1;
                if (py <= 0)
                    py = py + 2 * dx1;
                else
                {
                    if ((dx<0 && dy<0) || (dx>0 && dy>0)) x = x + 1; else x = x - 1;
                    py = py + 2 * (dx1 - dy1);
                }
                setPixel(x, y, color);
            }
        }
    }

    public void drawRectangle(int offX, int offY, int width, int height, int color) {
        // Don't render code
        if ( offX < -width ) {
            return;
        }
        if ( offY < -height ) {
            return;
        }
        if ( offX >= pW ) {
            return;
        }
        if ( offY >= pH ) {
            return;
        }

        int newX = 0;
        int newY = 0;
        int newWidth = width;
        int newHeight = height;

        // Clipping Code
        if ( offX < 0 ) {
            newX -= offX;
        }
        if ( offY < 0 ) {
            newY -= offY;
        }
        if ( newWidth + offX >= pW ) {
            newWidth -= (newWidth + offX - pW);
        }
        if ( newHeight + offY >= pH ) {
            newHeight -= (newHeight + offY - pH);
        }

        for ( int y = newY; y <= newHeight; y++ ) {
            setPixel(offX, y + offY, color);
            setPixel(offX + width, y + offY, color);
        }
        for ( int x = newX; x <= newWidth; x++ ) {
            setPixel(x + offX, offY, color);
            setPixel(x + offX, offY + height, color);
        }
    }

    public void drawFillRectangle(int offX, int offY, int width, int height, int color) {
        // Don't render code
        if ( offX < -width ) {
            return;
        }
        if ( offY < -height ) {
            return;
        }
        if ( offX >= pW ) {
            return;
        }
        if ( offY >= pH ) {
            return;
        }

        int newX = 0;
        int newY = 0;
        int newWidth = width;
        int newHeight = height;

        // Clipping Code
        if ( offX < 0 ) {
            newX -= offX;
        }
        if ( offY < 0 ) {
            newY -= offY;
        }
        if ( newWidth + offX >= pW ) {
            newWidth -= (newWidth + offX - pW);
        }
        if ( newHeight + offY >= pH ) {
            newHeight -= (newHeight + offY - pH);
        }

        for ( int y = newY; y < newHeight; y++ ) {
            for ( int x = newX; x < newWidth; x++ ) {
                setPixel(x + offX, y + offY, color);
            }
        }
    }

    public void drawRect(int x, int y, int w, int h, int color) {
        drawLine(x, y, x + w, y, color);
        drawLine(x + w, y, x + w, y + h, color);
        drawLine(x + w, y + h, x, y + h, color);
        drawLine(x, y + h, x, y, color);
    }

    public void drawFillRect(int x, int y, int w, int h, int color) {
        int x2 = x + w;
        int y2 = y + h;

        if (x < 0) x = 0;
        if (x >= pW) x = pW;
        if (y < 0) y = 0;
        if (y >= pH) y = pH;

        if (x2 < 0) x2 = 0;
        if (x2 >= pW) x2 = pW;
        if (y2 < 0) y2 = 0;
        if (y2 >= pH) y2 = pH;

        for (int i = x; i < x2; i++) {
            for (int j = y; j < y2; j++) {
                setPixel(i, j, color);
            }
        }
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

    private void drawLineForFillCircle(int sx, int ex, int ny, int color) {
        for (int i = sx; i <= ex; i++) {
            setPixel(i, ny, color);
        }
    }

    public void drawFillCircle(int x, int y, int radius, int color) {
        int x0 = 0;
        int y0 = radius;
        int d = 3 - 2 * radius;
        if ( radius == 0 ) {
            return;
        }

        while (y0 >= x0) {
            // Modified to draw scan-lines instead of edges
            drawLineForFillCircle(x - x0, x + x0, y - y0, color);
            drawLineForFillCircle(x - y0, x + y0, y - x0, color);
            drawLineForFillCircle(x - x0, x + x0, y + y0, color);
            drawLineForFillCircle(x - y0, x + y0, y + x0, color);
            if (d < 0) d += 4 * x0++ + 6;
            else d += 4 * (x0++ - y0--) + 10;
        }
    }

    public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int color) {
        drawLine(x1, y1, x2, y2, color);
        drawLine(x2, y2, x3, y3, color);
        drawLine(x3, y3, x1, y1, color);
    }

    public void drawFillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int color) {
        if (y2 < y1) {
            int tempInteger = y1;
            y1 = y2;
            y2 = tempInteger;

            tempInteger = x1;
            x1 = x2;
            x2 = tempInteger;
        }

        if (y3 < y1) {
            int tempInteger = y1;
            y1 = y3;
            y3 = tempInteger;

            tempInteger = x1;
            x1 = x3;
            x3 = tempInteger;
        }

        if (y3 < y2) {
            int tempInteger = y2;
            y2 = y3;
            y3 = tempInteger;

            tempInteger = x2;
            x2 = x3;
            x3 = tempInteger;
        }

        int dy1 = y2 - y1;
        int dx1 = x2 - x1;

        int dy2 = y3 - y1;
        int dx2 = x3 - x1;

        float dax_step = 0, dbx_step = 0;

        if ( dy1 != 0 ) {
            dax_step = dx1 / (float)Math.abs(dy1);
        }
        if ( dy2 != 0 ) {
            dbx_step = dx2 / (float)Math.abs(dy2);
        }

        if ( dy1 != 0 ) {
            for ( int i = y1; i <= y2; i++ )
            {
                int ax = (int)(x1 + (float)(i - y1) * dax_step);
                int bx = (int)(x1 + (float)(i - y1) * dbx_step);

                if ( ax > bx ) {
                    int tempInteger = ax;
                    ax = bx;
                    bx = tempInteger;
                }

                for (int j = ax; j < bx; j++) {
                    int index = i * pW + j;
                    if (index < p.length) {
                        setPixel(j, i, color);
                    }
                }
            }
        }

        dy1 = y3 - y2;
        dx1 = x3 - x2;

        if ( dy1 != 0 ) {
            dax_step = dx1 / (float)Math.abs(dy1);
        }
        if ( dy2 != 0 ) {
            dbx_step = dx2 / (float)Math.abs(dy2);
        }

        if ( dy1 != 0 )
        {
            for (int i = y2; i <= y3; i++)
            {
                int ax = (int)(x2 + (float)(i - y2) * dax_step);
                int bx = (int)(x1 + (float)(i - y1) * dbx_step);

                if (ax > bx) {
                    int tempInteger = ax;
                    ax = bx;
                    bx = tempInteger;
                }

                for (int j = ax; j < bx; j++) {
                    int index = i * pW + j;
                    if (index < p.length) {
                        setPixel(j, i, color);
                    }
                }
            }
        }
    }

    // Getters

    public int[] getP() {
        return p;
    }

    public int getW() {
        return pW;
    }

    public int getH() {
        return pH;
    }

    public int getZDepth() {
        return zDepth;
    }

    public int getAmbientColor() {
        return ambientColor;
    }

    public PixelBuffer<IntBuffer> getPixelBuffer() {
        return pixelBuffer;
    }

}
