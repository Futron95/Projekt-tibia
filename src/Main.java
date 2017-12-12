import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Main
{
    public static Dimension screenSize;
    public static Rectangle screenRectangle;
    public static Robot robot;
    public static Random r;

    public static void start() throws Exception
    {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenRectangle = new Rectangle(screenSize);
        robot = new Robot();
        r = new Random();
        Checker healing = new Checker(true, new Pixel(150,0,0, 2490, 33), KeyEvent.VK_F1);
        Checker manaing = new Checker(true, new Pixel(0,0,150, 2440, 46), KeyEvent.VK_F2);
        Thread checking = new Thread(() ->
        {
            while(true)
            {
                healing.check();
                manaing.check();
                try {
                    Thread.sleep(r.nextInt(40)+80);           //Sprawdzanie co 80-120 milisekund
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        checking.start();
    }

    public static void main(String[] args) throws Exception {
        Main.start();
    }
}
