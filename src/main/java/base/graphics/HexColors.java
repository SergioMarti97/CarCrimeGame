package base.graphics;

/**
 * This class is an attempt to have all the codes
 * of colors that I use the most.
 *
 * @class: HexColors.
 * @autor: Sergio Martí Torregrosa
 * @date: 2020-07-06
 */
public class HexColors {

    public static int rnd(int max, int min) {
        return (int)(Math.random() * (max - min + 1) + min);
    }

    public static int rndHexColor() {
        int r = rnd(0xff, 0x00);
        int g = rnd(0xff, 0x00);
        int b = rnd(0xff, 0x00);
        return (255 << 24 | r << 16 | g << 8 | b);
    }

    public static final int WHITE = 0xffffffff;

    public static final int BLACK = 0xff000000;

    public static final int ALPHA = 0x00000000;

    public static final int GREY = 0xff333333;

    public static  final int RED = 0xffff0000;

    public static  final int GREEN = 0xff00ff00;

    public static  final int BLUE = 0xff0000ff;

    public static  final int YELLOW = 0xffffff00;

    public static  final int MAGENTA = 0xffff00ff;

    public static  final int CYAN = 0xff00ffff;

    public static  final int WINE = 0xffc82a54;

    public static  final int FANCY_RED = 0xffef280f;

    public static  final int ORANGE = 0xffe36b2d;

    public static  final int TANGERINE = 0xffe36b2c;

    public static  final int LEMON = 0xffe7d40a;

    public static  final int MINT = 0xff6dc36d;

    public static  final int DARK_MINT = 0xff02ac66;

    public static  final int FANCY_BLUE = 0xff23bac4;

    public static  final int LIGHT_BLUE = 0xff109dfa;

    public static  final int DARK_BLUE = 0xff024a86;

    public static  final int ROYAL_BLUE = 0xff002366;

}
