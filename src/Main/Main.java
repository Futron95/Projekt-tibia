package Main;

import Actions.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Main
{
    public static Dimension screenSize;
    public static Robot robot;
    public static Random r;
    private static int hpPercent=100;
    private static int manaPercent=100;
    private static Action exura;
    private static Action healthPotion;
    private static Action manaPotion;

    public static void main(String[] args) throws Exception
    {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("Wymiary ekranu: szerokosc "+screenSize.getWidth()+", wysokość "+screenSize.getHeight());
        robot = new Robot();
        r = new Random();
        exura = new HealingAction(KeyEvent.VK_F1);
        healthPotion = new PotionAction(KeyEvent.VK_F4);
        manaPotion = new PotionAction(KeyEvent.VK_F2);

        Thread checking = new Thread(() ->
        {
            while(true)
            {
               hpPercent = Checker.getHpPercent();
               manaPercent = Checker.getManaPercent();
               if (hpPercent<40)
                   Action.perform(healthPotion);
               if (hpPercent<80)
                   Action.perform(exura);
               if (manaPercent<30)
                   Action.perform(manaPotion);

                try {
                    Thread.sleep(r.nextInt(40)+80);           //Sprawdzanie co 80-120 milisekund
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        //checking.start();

        Thread attacking = new Thread(() ->
        {
           while(true)
           {
               if(Attacker.isMonsterPresent() && !Attacker.isAttacking()) {

                   robot.keyPress(KeyEvent.VK_F3);
                   robot.keyRelease(KeyEvent.VK_F3);
                   System.out.println("Atakuje!");
               }

               try {
                   Thread.sleep(r.nextInt(100));           //Sprawdzanie co 80-120 milisekund
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        });
        attacking.start();
    }
}
