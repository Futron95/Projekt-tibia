package Main;


public class Pixel {
    public int x;
    public int y;
    public int color;
    public static final int GROUND_COLOR = 0xFF996633;

    public static boolean checkPixel(int x, int y, int rgb)
    {
        int checkedPixel = Main.capture.getRGB(x,y);
        if (checkedPixel == rgb)
            return true;
        else
            return false;
    }

    public Pixel(int color, int x, int y)
    {
        this.color = color;
        this.x = x;
        this.y = y;
    }

}
