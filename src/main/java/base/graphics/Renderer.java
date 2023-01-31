package base.graphics;

import base.graphics.font.Font;
import base.graphics.image.Image;
import base.graphics.image.ImageRequest;
import base.graphics.image.ImageTile;
import base.vectors.points2d.Vec2df;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Renderer {

    protected final int width;

    protected final int height;

    protected final int[] p;
    
    protected final float invW;
    
    protected final float invH;
    
    protected int[] zb;
    
    protected int zDepth = 0;
    
    protected int[] lm;
    
    protected int[] lb;
    
    protected boolean processing = false;
    
    protected int ambientColor = 0xffffffff;

    protected final PixelBuffer<IntBuffer> pixelBuffer;
    
    protected ArrayList<ImageRequest> imageRequests;
    
    protected Font font;

    public Renderer(int width, int height) {
        this.width = width;
        this.height = height;

        invW = 1.0f / width;
        invH = 1.0f / height;
        
        // Allocate memory
        IntBuffer buffer = IntBuffer.allocate(width * height);
        p = buffer.array();

        zb = new int[p.length];
        lm = new int[p.length];
        lb = new int[p.length];

        imageRequests = new ArrayList<>();

        // Font
        font = new Font("fonts/consolas24.png");

        // Pixel Buffer
        PixelFormat<IntBuffer> format = PixelFormat.getIntArgbPreInstance();
        pixelBuffer = new PixelBuffer<>(width, height, buffer, format);
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

    public void process() {
        processing = true;

        pixelBuffer.updateBuffer(b -> null);

        if ( imageRequests.size() > 0 ) {
            imageRequests.sort(Comparator.comparingInt(ImageRequest::getzDepth));

            for (ImageRequest ir : imageRequests) {
                setZDepth(ir.getzDepth());
                drawImage(ir.getImage(), ir.getOffX(), ir.getOffY());
            }

            for (int i = 0; i < p.length; i++) {
                float r = ((lm[i] >> 16) & 0xff) / 255.0f;
                float g = ((lm[i] >> 8) & 0xff) / 255.0f;
                float b = (lm[i] & 0xff) / 255.0f;
                p[i] = ((int) (((p[i] >> 16) & 0xff) * r) << 16 | (int) (((p[i] >> 8) & 0xff) * g) << 8 | (int) ((p[i] & 0xff) * b));
            }
        }
        imageRequests.clear();

        processing = false;
    }

    // Drawing methods

    public void clear(int color) {
        Arrays.fill(p, color);
        Arrays.fill(zb, 0);
        Arrays.fill(lm, ambientColor);
        Arrays.fill(lb, 0);
    }

    public void clear() {
        clear(ambientColor);
    }

    public void setPixel(int x, int y, int color) {
        int alpha = ((color >> 24) & 0xff);
        if ( (x < 0 || x >= width || y < 0 || y >= height) || alpha == 0 ) { // value == 0xffff00ff
            return;
        }
        int index = x + y * width;
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
        }
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
        if ( offX >= width ) {
            return;
        }
        if ( offY >= height ) {
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
        if ( newWidth + offX >= width ) {
            newWidth -= (newWidth + offX - width);
        }
        if ( newHeight + offY >= height ) {
            newHeight -= (newHeight + offY - height);
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
        if ( offX >= width ) {
            return;
        }
        if ( offY >= height ) {
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
        if ( newWidth + offX >= width ) {
            newWidth -= (newWidth + offX - width);
        }
        if ( newHeight + offY >= height ) {
            newHeight -= (newHeight + offY - height);
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
                    int index = i * width + j;
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
                    int index = i * width + j;
                    if (index < p.length) {
                        setPixel(j, i, color);
                    }
                }
            }
        }
    }

    public void drawPolygon(ArrayList<Vec2df> points, int color) {
        for ( int i = 0; i < points.size() - 1; i++ ) {
            drawLine(
                    (int)points.get(i).getX(), (int)points.get(i).getY(),
                    (int)points.get(i + 1).getX(), (int)points.get(i + 1).getY(),
                    color);
        }
        drawLine(
                (int)points.get(points.size() - 1).getX(), (int)points.get(points.size() - 1).getY(),
                (int)points.get(0).getX(), (int)points.get(0).getY(),
                color);
    }

    public void drawImage(Image image, int offX, int offY) {
        if ( image == null ) {
            return;
        }

        if ( image.isAlpha() && !processing) {
            imageRequests.add(new ImageRequest(image, zDepth, offX, offY));
            return;
        }

        // Don't render code
        if ( offX < -width ) {
            return;
        }
        if ( offY < -height ) {
            return;
        }
        if ( offX >= width ) {
            return;
        }
        if ( offY >= height ) {
            return;
        }

        int newX = 0;
        int newY = 0;
        int newWidth = image.getW();
        int newHeight = image.getH();

        // Clipping Code
        if ( offX < 0 ) {
            newX -= offX;
        }
        if ( offY < 0 ) {
            newY -= offY;
        }
        if ( newWidth + offX >= width ) {
            newWidth -= (newWidth + offX - width);
        }
        if ( newHeight + offY >= height ) {
            newHeight -= (newHeight + offY - height);
        }

        for (int y = newY; y < newHeight; y++) {
            for (int x = newX; x < newWidth; x++) {
                //p[(x + offX) + width * (y + offY)] = image.getPixel(x, y);
                setPixel(x + offX, y + offY, image.getPixel(x, y));
            }
        }
    }

    public void drawImage(Image image, int offX, int offY, int colorToChange, int newColor) {
        if ( image == null ) {
            return;
        }

        if ( image.isAlpha() && !processing) {
            imageRequests.add(new ImageRequest(image, zDepth, offX, offY));
            return;
        }

        // Don't render code
        if ( offX < -width ) {
            return;
        }
        if ( offY < -height ) {
            return;
        }
        if ( offX >= width ) {
            return;
        }
        if ( offY >= height ) {
            return;
        }

        int newX = 0;
        int newY = 0;
        int newWidth = image.getW();
        int newHeight = image.getH();

        // Clipping Code
        if ( offX < 0 ) {
            newX -= offX;
        }
        if ( offY < 0 ) {
            newY -= offY;
        }
        if ( newWidth + offX >= width ) {
            newWidth -= (newWidth + offX - width);
        }
        if ( newHeight + offY >= height ) {
            newHeight -= (newHeight + offY - height);
        }

        for ( int y = newY; y < newHeight; y++ ) {
            for ( int x = newX; x < newWidth; x++ ) {
                int pixelValue = image.getP()[x + y * image.getW()];
                if ( pixelValue == colorToChange ) {
                    setPixel(x + offX, y + offY, newColor);
                } else {
                    setPixel(x + offX, y + offY, pixelValue);
                }
            }
        }
    }

    public void drawImageTile(ImageTile image, int offX, int offY, int tileX, int tileY) {
        if ( image == null ) {
            return;
        }

        if ( image.isAlpha() && !processing) {
            imageRequests.add(new ImageRequest(image.getTileImage(tileX, tileY), zDepth, offX, offY));
            return;
        }

        // Don't render code
        if ( offX < -image.getTileWidth() ) {
            return;
        }
        if ( offY < -image.getTileHeight() ) {
            return;
        }
        if ( offX >= image.getTileWidth() ) {
            return;
        }
        if ( offY >= image.getTileHeight() ) {
            return;
        }

        int newX = 0;
        int newY = 0;
        int newWidth = image.getTileWidth();
        int newHeight = image.getTileHeight();

        // Clipping Code
        if ( offX < 0 ) {
            newX -= offX;
        }
        if ( offY < 0 ) {
            newY -= offY;
        }
        if ( newWidth + offX >= width ) {
            newWidth -= (newWidth + offX - width);
        }
        if ( newHeight + offY >= height ) {
            newHeight -= (newHeight + offY - height);
        }

        for ( int y = newY; y < newHeight; y++ ) {
            for ( int x = newX; x < newWidth; x++ ) {
                setPixel(x + offX, y + offY,
                        image.getP()[(x + tileX * image.getTileWidth()) + (y + tileY * image.getTileHeight()) * image.getW()]);
            }
        }
    }

    public void drawCharacter(Image characterImage, int offX, int offY, int color) {
        if ( characterImage == null ) {
            return;
        }
        // Don't render code
        if ( offX < -width ) {
            return;
        }
        if ( offY < -height ) {
            return;
        }
        if ( offX >= width ) {
            return;
        }
        if ( offY >= height ) {
            return;
        }

        int newX = 0;
        int newY = 0;
        int newWidth = characterImage.getW();
        int newHeight = characterImage.getH();

        // Clipping Code
        if ( offX < 0 ) {
            newX -= offX;
        }
        if ( offY < 0 ) {
            newY -= offY;
        }
        if ( newWidth + offX >= width ) {
            newWidth -= (newWidth + offX - width);
        }
        if ( newHeight + offY >= height ) {
            newHeight -= (newHeight + offY - height);
        }

        for ( int y = newY; y < newHeight; y++ ) {
            for (int x = newX; x < newWidth; x++) {
                if (characterImage.getP()[x + y * characterImage.getW()] == 0xffffffff) {
                    setPixel(x + offX, y + offY, color);
                }
            }
        }
    }

    public void drawText(String text, int offX, int offY, int color, Font font) {
        int offset = 0;
        for ( int i = 0; i < text.length(); i++ ) {
            int unicode = text.codePointAt(i);
            drawCharacter(font.getCharacterImage(unicode), offset + offX, offY, color);
            offset += font.getWidths()[unicode];
        }
    }

    public void drawText(String text, int offX, int offY, int color) {
        drawText(text, offX, offY, color, font);
    }

    public void drawWarpedDecal(Image img, Vec2df[] pos, boolean drawTriangles) {
        float[] w = new float[4];
        Arrays.fill(w, 1.0f);

        Vec2df[] uv = new Vec2df[] {
                new Vec2df(0.0f, 0.0f),
                new Vec2df(0.0f, 1.0f),
                new Vec2df(1.0f, 1.0f),
                new Vec2df(1.0f, 0.0f)
        };

        Vec2df[] posDi = new Vec2df[] {
                new Vec2df(),
                new Vec2df(),
                new Vec2df(),
                new Vec2df()
        };

        Vec2df center = new Vec2df();

        float rd = (pos[2].getX() - pos[0].getX()) * (pos[3].getY() - pos[1].getY()) - (pos[3].getX() - pos[1].getX()) * (pos[2].getY() - pos[0].getY());

        if (rd != 0) {
            rd = 1.0f / rd;

            float rn = ((pos[3].getX() - pos[1].getX()) * (pos[0].getY() - pos[1].getY()) - (pos[3].getY() - pos[1].getY()) * (pos[0].getX() - pos[1].getX())) * rd;
            float sn = ((pos[2].getX() - pos[0].getX()) * (pos[0].getY() - pos[1].getY()) - (pos[2].getY() - pos[0].getY()) * (pos[0].getX() - pos[1].getX())) * rd;

            if ( !(rn < 0.f || rn > 1.f || sn < 0.f || sn > 1.f) ) {
                //center = pos[0] + rn * (pos[2] - pos[0]);
                center.setX(pos[0].getX() + rn * (pos[2].getX() - pos[0].getX()));
                center.setY(pos[0].getY() + rn * (pos[2].getY() - pos[0].getY()));
            }

            float[] d = new float[4];
            for (int i = 0; i < 4; i++)	{
                //d[i] = (pos[i] - center).mag();
                // Para cada valor de d, se calcula la magnitud del vector resultante de pos[i] - centro
                float vecX = pos[i].getX() - center.getX();
                float vecY = pos[i].getY() - center.getY();
                d[i] = (float)Math.sqrt(vecX * vecX + vecY * vecY);
            }

            for (int i = 0; i < 4; i++) {
                //float up = d[i] + d[(i + 2) & 3];
                //float down = d[(i + 2) & 3];
                //down = (down == 0)? 1.0f : down;
                float q = (d[i] == 0.0f) ? 1.0f : (d[i] + d[(i + 2) & 3]) / (d[(i + 2) & 3]);

                //di.uv[i] *= q;
                uv[i].multiply(q);

                //di.w[i] *= q;
                w[i] *= q;

                posDi[i].setX(pos[i].getX());
                posDi[i].setY(pos[i].getY());
            }
            //di.mode = nDecalMode;
            //vLayers[nTargetLayer].vecDecalInstance.push_back(di);

            drawTexturedParallelogram(posDi, uv, w, img, drawTriangles);
        }
    }

    private void drawTexturedParallelogram(Vec2df[] pos, Vec2df[] uv, float[] w, Image img, boolean drawTriangles) {
        drawTexturedTriangle(
                (int)pos[0].getX(), (int)pos[0].getY(), uv[0].getX(), uv[0].getY(), w[0],
                (int)pos[1].getX(), (int)pos[1].getY(), uv[1].getX(), uv[1].getY(), w[1],
                (int)pos[2].getX(), (int)pos[2].getY(), uv[2].getX(), uv[2].getY(), w[2],
                img);
        drawTexturedTriangle(
                (int)pos[0].getX(), (int)pos[0].getY(), uv[0].getX(), uv[0].getY(), w[0],
                (int)pos[2].getX(), (int)pos[2].getY(), uv[2].getX(), uv[2].getY(), w[2],
                (int)pos[3].getX(), (int)pos[3].getY(), uv[3].getX(), uv[3].getY(), w[3],
                img);
        if (drawTriangles) {
            drawTriangle(
                    (int)pos[0].getX(), (int)pos[0].getY(),
                    (int)pos[1].getX(), (int)pos[1].getY(),
                    (int)pos[2].getX(), (int)pos[2].getY(),
                    HexColors.WHITE);
            drawTriangle(
                    (int)pos[0].getX(), (int)pos[0].getY(),
                    (int)pos[2].getX(), (int)pos[2].getY(),
                    (int)pos[3].getX(), (int)pos[3].getY(),
                    HexColors.WHITE);
        }
    }

    private void setPixelForTexturedTriangle(int y, int x,
                                             float tex_u, float tex_v, float tex_w,
                                             Image texture) {
        int color;// = texture.getSample(tex_u, tex_v);
        tex_w = (tex_w == 0.0f)? 1.0f : tex_w;

        color = texture.getSample((tex_u / tex_w), (tex_v / tex_w));
        //color = calculateColor(color, brightness);

        try {
            /*if ( tex_w > depthBuffer[y * getW() + x] ) {
                setPixel(x, y, color);
                depthBuffer[y * getW() + x] = tex_w;
            }*/
            setPixel(x, y, color);
        } catch ( ArrayIndexOutOfBoundsException e ) {
            String errorMessage = "X: " + x + " Y: " + y + " outside of " + getWidth() + "x" + getHeight();
            System.out.println("Set pixel Error: " + errorMessage + e.getMessage());
        }

    }

    /**
     * This method draws a textured triangle. Code copied from Javidx9 video
     * Code-It-Yourself! 3D Graphics Engine Part #4 - Texturing & depth buffers.
     * https://www.youtube.com/watch?v=nBzCS-Y0FcY&t=1722s
     * @param x1 x value for point one of triangle
     * @param y1 y value for point one of triangle
     * @param u1 x value for texture point one of triangle
     * @param v1 y value for texture point one of triangle
     * @param w1 z value for texture point one of triangle
     * @param x2 x value for point two of triangle
     * @param y2 y value for point two of triangle
     * @param u2 x value for texture point two of triangle
     * @param v2 y value for texture point two of triangle
     * @param w2 z value for texture point two of triangle
     * @param x3 x value for point three of triangle
     * @param y3 y value for point three of triangle
     * @param u3 x value for texture point three triangle
     * @param v3 y value for texture point three triangle
     * @param w3 z value for texture point three triangle
     * @param texture the image texture
     */
    public void drawTexturedTriangle(
            int x1, int y1, float u1, float v1, float w1,
            int x2, int y2, float u2, float v2, float w2,
            int x3, int y3, float u3, float v3, float w3,
            Image texture) {
        if (y2 < y1) {
            int tempInteger = y1;
            y1 = y2;
            y2 = tempInteger;

            tempInteger = x1;
            x1 = x2;
            x2 = tempInteger;

            float tempFloat = u1;
            u1 = u2;
            u2 = tempFloat;

            tempFloat = v1;
            v1 = v2;
            v2 = tempFloat;

            tempFloat = w1;
            w1 = w2;
            w2 = tempFloat;
        }

        if (y3 < y1) {
            int tempInteger = y1;
            y1 = y3;
            y3 = tempInteger;

            tempInteger = x1;
            x1 = x3;
            x3 = tempInteger;

            float tempFloat = u1;
            u1 = u3;
            u3 = tempFloat;

            tempFloat = v1;
            v1 = v3;
            v3 = tempFloat;

            tempFloat = w1;
            w1 = w3;
            w3 = tempFloat;
        }

        if (y3 < y2) {
            int tempInteger = y2;
            y2 = y3;
            y3 = tempInteger;

            tempInteger = x2;
            x2 = x3;
            x3 = tempInteger;

            float tempFloat = u2;
            u2 = u3;
            u3 = tempFloat;

            tempFloat = v2;
            v2 = v3;
            v3 = tempFloat;

            tempFloat = w2;
            w2 = w3;
            w3 = tempFloat;
        }

        int dy1 = y2 - y1;
        int dx1 = x2 - x1;
        float dv1 = v2 - v1;
        float du1 = u2 - u1;
        float dw1 = w2 - w1;

        int dy2 = y3 - y1;
        int dx2 = x3 - x1;
        float dv2 = v3 - v1;
        float du2 = u3 - u1;
        float dw2 = w3 - w1;

        float tex_u, tex_v, tex_w;

        float dax_step = 0, dbx_step = 0, du1_step = 0, dv1_step = 0, du2_step = 0, dv2_step = 0, dw1_step = 0, dw2_step = 0;

        if ( dy1 != 0 ) {
            dax_step = dx1 / (float)Math.abs(dy1);
        }
        if ( dy2 != 0 ) {
            dbx_step = dx2 / (float)Math.abs(dy2);
        }

        if ( dy1 != 0 ) {
            du1_step = du1 / (float)Math.abs(dy1);
        }
        if ( dy1 != 0 ) {
            dv1_step = dv1 / (float)Math.abs(dy1);
        }
        if ( dy1 != 0 ) {
            dw1_step = dw1 / (float)Math.abs(dy1);
        }

        if ( dy2 != 0 ) {
            du2_step = du2 / (float)Math.abs(dy2);
        }
        if ( dy2 != 0 ) {
            dv2_step = dv2 / (float)Math.abs(dy2);
        }
        if ( dy2 != 0 ) {
            dw2_step = dw2 / (float)Math.abs(dy2);
        }

        if ( dy1 != 0 ) {
            for ( int i = y1; i <= y2; i++ ) {
                int ax = (int)(x1 + (float)(i - y1) * dax_step);
                int bx = (int)(x1 + (float)(i - y1) * dbx_step);

                float tex_su = u1 + (float)(i - y1) * du1_step;
                float tex_sv = v1 + (float)(i - y1) * dv1_step;
                float tex_sw = w1 + (float)(i - y1) * dw1_step;

                float tex_eu = u1 + (float)(i - y1) * du2_step;
                float tex_ev = v1 + (float)(i - y1) * dv2_step;
                float tex_ew = w1 + (float)(i - y1) * dw2_step;

                if ( ax > bx ) {
                    int tempInteger = ax;
                    ax = bx;
                    bx = tempInteger;

                    float tempFloat = tex_su;
                    tex_su = tex_eu;
                    tex_eu = tempFloat;

                    tempFloat = tex_sv;
                    tex_sv = tex_ev;
                    tex_ev = tempFloat;

                    tempFloat = tex_sw;
                    tex_sw = tex_ew;
                    tex_ew = tempFloat;
                }

                tex_u = tex_su;
                tex_v = tex_sv;
                tex_w = tex_sw;

                float tstep = 1.0f / ((float)(bx - ax));
                float t = 0.0f;

                for (int j = ax; j < bx; j++) {
                    tex_u = (1.0f - t) * tex_su + t * tex_eu;
                    tex_v = (1.0f - t) * tex_sv + t * tex_ev;
                    tex_w = (1.0f - t) * tex_sw + t * tex_ew;

                    setPixelForTexturedTriangle(i, j, tex_u, tex_v, tex_w, texture);

                    t += tstep;
                }
            }
        }

        dy1 = y3 - y2;
        dx1 = x3 - x2;
        dv1 = v3 - v2;
        du1 = u3 - u2;
        dw1 = w3 - w2;

        if ( dy1 != 0 ) {
            dax_step = dx1 / (float)Math.abs(dy1);
        }
        if ( dy2 != 0 ) {
            dbx_step = dx2 / (float)Math.abs(dy2);
        }

        du1_step = 0;
        dv1_step = 0;
        if ( dy1 != 0 ) {
            du1_step = du1 / (float)Math.abs(dy1);
        }
        if ( dy1 != 0 ) {
            dv1_step = dv1 / (float)Math.abs(dy1);
        }
        if ( dy1 != 0 ) {
            dw1_step = dw1 / (float)Math.abs(dy1);
        }

        if ( dy1 != 0 ) {
            for (int i = y2; i <= y3; i++) {
                int ax = (int)(x2 + (float)(i - y2) * dax_step);
                int bx = (int)(x1 + (float)(i - y1) * dbx_step);

                float tex_su = u2 + (float)(i - y2) * du1_step;
                float tex_sv = v2 + (float)(i - y2) * dv1_step;
                float tex_sw = w2 + (float)(i - y2) * dw1_step;

                float tex_eu = u1 + (float)(i - y1) * du2_step;
                float tex_ev = v1 + (float)(i - y1) * dv2_step;
                float tex_ew = w1 + (float)(i - y1) * dw2_step;

                if (ax > bx) {
                    int tempInteger = ax;
                    ax = bx;
                    bx = tempInteger;

                    float tempFloat = tex_su;
                    tex_su = tex_eu;
                    tex_eu = tempFloat;

                    tempFloat = tex_sv;
                    tex_sv = tex_ev;
                    tex_ev = tempFloat;

                    tempFloat = tex_sw;
                    tex_sw = tex_ew;
                    tex_ew = tempFloat;
                }

                tex_u = tex_su;
                tex_v = tex_sv;
                tex_w = tex_sw;

                float tstep = 1.0f / ((float)(bx - ax));
                float t = 0.0f;

                for (int j = ax; j < bx; j++) {
                    tex_u = (1.0f - t) * tex_su + t * tex_eu;
                    tex_v = (1.0f - t) * tex_sv + t * tex_ev;
                    tex_w = (1.0f - t) * tex_sw + t * tex_ew;

                    setPixelForTexturedTriangle(i, j, tex_u, tex_v, tex_w, texture);

                    t += tstep;
                }
            }
        }
    }
    
    // Getters

    public int[] getP() {
        return p;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getZDepth() {
        return zDepth;
    }

    public int getAmbientColor() {
        return ambientColor;
    }

    public Font getFont() {
        return font;
    }

    private void setZDepth(int zDepth) {
        this.zDepth = zDepth;
    }

    public void setAmbientColor(int ambientColor) {
        this.ambientColor = ambientColor;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public PixelBuffer<IntBuffer> getPixelBuffer() {
        return pixelBuffer;
    }

}
