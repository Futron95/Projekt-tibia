import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Main
{
    public static Dimension screenSize;
    public static Robot robot;
    public static Random r;

    public static void main(String[] args) throws Exception
    {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("Wymiary ekranu: szerokosc "+screenSize.getWidth()+", wysokość "+screenSize.getHeight());
        robot = new Robot();
        r = new Random();
        Checker strongHealing = new Checker(true, new Pixel(150, 0, 0,(int)Main.screenSize.getWidth()-120, 33), KeyEvent.VK_F4);
        Checker healing = new Checker(true, new Pixel(150,0,0, (int)Main.screenSize.getWidth()-70, 33), KeyEvent.VK_F1);    //obiekt sluzacy do sprawdzania hp, jezeli pasek hp konczy sie wczesniej niz 70 pixeli od konca ekranu, wywolywana jest akcja
        Checker manaing = new Checker(true, new Pixel(0,0,150, (int)Main.screenSize.getWidth()-120, 46), KeyEvent.VK_F2);
        Thread checking = new Thread(() ->
        {
            while(true)
            {
                strongHealing.check();
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
}
