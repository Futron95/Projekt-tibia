package Main;

import Actions.Action;
import Actions.HealingAction;
import Actions.PotionAction;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Main
{
    public static Dimension screenSize;
    public static Robot robot;
    public static Random r;
    public static boolean focused=false;
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

        Walker.fillVectorsList();

        Thread focusChecking = new Thread(()->      //watek sprawdzajacy czy okno tibii jest aktywne
        {
            int i, color;
            while(true) {
                for (i = 24; i < 29; i++) {
                    color = robot.getPixelColor(i, 7).getRGB();
                    if (color != 0xFF000000) {                      //warunek potwierdzenia aktywnosci okna to sprawdzenie czy pixele 24,25,26,27,28 na 7 sa czarne
                        focused = false;
                        break;
                    }
                }
                if (i == 29)
                    focused = true;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        focusChecking.start();

        Thread barsChecking = new Thread(() ->
        {
            while(focused)
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
        //barsChecking.start();

        Thread attacking = new Thread(() ->
        {
           while(focused)
           {
               if(Attacker.isMonsterPresent() && !Attacker.isAttacking()) {

                   robot.keyPress(KeyEvent.VK_F3);
                   robot.keyRelease(KeyEvent.VK_F3);
                   System.out.println("Atakuje!");
               }

               try {
                   Thread.sleep(r.nextInt(100));
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        });
        //attacking.start();

    }
}
