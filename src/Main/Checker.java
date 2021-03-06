package Main;

import java.awt.*;
import Actions.Action;

public class Checker {

    private static int barsColor = 0xFFDB4F4F;  //kolor serduszka używany przy określeniu czy paski many i hp są tak gdzie powinny
    private static boolean bars = true;   //wskazuje czy paski zdrowia i many sa w odpowiednim miejscu (na podstawie konkretnego piksela z serduszka kolo paska hp)
    public static volatile int hpPercent=100;
    public static volatile int manaPercent=100;

    public static boolean checkBars()       //metoda statyczna sprawdzająca czy paski hp i many są w przewidywanym miejscu
    {
        boolean newBars = Main.capture.getRGB((int)Main.screenSize.getWidth()-160, 33) == barsColor;    //sprawdzanie czy paski hp i many są w odpowiednim miejscu
        if (bars != newBars) {
            if (newBars)
                System.out.println("Paski hp i many na odpowiednim miejscu.");
            else
                System.out.println("Paski hp i many w złym miejscu!");
            bars = newBars;
        }
        return newBars;
    }

    static void updateHpPercent()
    {
        int red;
        if (!checkBars())
            hpPercent = 100;
        double x = Main.screenSize.getWidth()-141.2;
        for (int i = 1;i<=10;i++)
        {
            red = (Main.capture.getRGB((int)x,34) & 0x00FF0000) >> 16;
            if (red<150)
                hpPercent = (i-1)*10;
            x+=8.7;
        }
        hpPercent = 100;
    }

    static void updateManaPercent()
    {
        int blue;
        if (!checkBars())
            manaPercent = 100;
        double x = Main.screenSize.getWidth()-141.2;
        for (int i = 1;i<=10;i++)
        {
            blue = Main.capture.getRGB((int)x,47) & 0x000000FF;
            if (blue<150)
                manaPercent = (i-1)*10;
            x+=8.7;
        }
        manaPercent = 100;
    }
}
