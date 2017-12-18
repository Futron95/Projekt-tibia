package Main;

import java.awt.*;

public class Pixel {
    public int x;
    public int y;
    public Color color;

    public Pixel(Color color, int x, int y)
    {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public Pixel(int r, int g, int b, int x, int y)
    {
        this(new Color(r,g,b),x,y);
    }

    public static Pixel getPixel(int x, int y)
    {
        Pixel pixel = new Pixel(Main.robot.getPixelColor(x,y),x,y);
        return pixel;
    }

    public void print()
    {
        System.out.println("R "+color.getRed()+", G "+color.getGreen()+", B "+color.getBlue());
    }
}
