package Main;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Mateusz on 24.12.2017.
 */
public class Attacker {

    private static long lastDamageTime=0;       //czas systemowy ostatniej zmiany koloru paska hp obecnie atakowanego potwora
    private static int attackedHpColor=0;       //kolor hp atakowanego potwora, potrzebny przy okreslaniu czy spada mu poziom zycia
    private static int battleListPosition;      //wspolrzedna x poczatku paska hp potworow na liscie battle
    public static volatile boolean attackFailed = false;
    private static Robot robot;

    static {
        try {
            battleListPosition = (int)Main.screenSize.getWidth()-326;
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static boolean isAttackFailed()  //sprawdza czy atak sie nie udal, czyli czy zaatakowany potwor nie traci hp, co oznacza ze stoi w niedostepnym miejscu i trzeba zmienic cel lub miejsce
    {
        int positionY = attackedY();
        if (positionY==0)
            return true;
        int ahc = Main.capture.getRGB(battleListPosition+1, positionY);
        long time = System.currentTimeMillis();
        if (ahc != attackedHpColor)
        {
            attackedHpColor = ahc;
            lastDamageTime = time;
            return false;
        }
        if (time - lastDamageTime > 8000) {
            System.out.println("Attack failed!");
            attackFailed = true;
            return true;
        }
        return false;
    }

    public static void attack()
    {
        robot.keyPress(KeyEvent.VK_F3);
        robot.keyRelease(KeyEvent.VK_F3);
        attackedHpColor = Main.capture.getRGB(battleListPosition+1, attackedY());
        lastDamageTime = System.currentTimeMillis();
        //attackFailed = false;
    }

    public static boolean isMonsterPresent()    //sprawdza czy na battle liscie jest przynajmniej 1 potwor
    {
        if (Pixel.checkPixel(battleListPosition,57, 0xFF000000))    //jezeli sprawdzany pixel jest czarny to jest tam pasek hp potwora
            return true;
        else
            return false;
    }

    private static int attackedY()      //metoda zwracajaca wspolrzedna y atakowanego potwora w tabeli battle
    {
        int x = (int)Main.screenSize.getWidth()-326;
        int y = 57;
        while (Pixel.checkPixel(x,y,0xFF000000))
        {
            if (Pixel.checkPixel(x-3,y,0xFFFF0000))
                return y;
            y+=22;
        }
        return 0;
    }

    public static boolean isAttacking()
    {
        if(attackedY()>0)
            return true;
        return false;
    }
}
