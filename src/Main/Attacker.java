package Main;

/**
 * Created by Mateusz on 24.12.2017.
 */
public class Attacker {
    public static boolean isMonsterPresent()
    {
        if (Pixel.checkPixel((int)Main.screenSize.getWidth()-326,57, 0xFF000000))
            return true;
        else
            return false;
    }

    public static boolean isAttacking()
    {
        int x = (int)Main.screenSize.getWidth()-326;
        int y = 57;
        while (Pixel.checkPixel(x,y,0xFF000000))
        {
            if (Pixel.checkPixel(x-3,y,0xFFFF0000))
                return true;
            y+=22;
        }
        return false;
    }
}
