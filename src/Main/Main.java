package Main;

import Actions.Action;
import Actions.HealingAction;
import Actions.PotionAction;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Scanner;

public class Main
{
    public static Dimension screenSize;
    public static Robot robot;
    public static Random r;
    public static volatile boolean canWalk=false;           //volatile sprawia ze zmieniajac wartosc w jednym watku, inne watki widza ja
    private static int hpPercent=100;
    private static int manaPercent=100;
    private static Action exura;
    private static Action healthPotion;
    private static Action manaPotion;

    public static boolean isFocused()
    {
        int i, color;
        for (i = 24; i < 29; i++) {
            color = robot.getPixelColor(i, 7).getRGB();
            if (color != 0xFF000000) {                      //warunek potwierdzenia aktywnosci okna to sprawdzenie czy pixele 24,25,26,27,28 na 7 sa czarne
                return false;
            }
        }
        return true;
    }

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

        Thread barsChecking = new Thread(() ->
        {
            while(true)
            {
               if(!isFocused()) {
                    continue;
                }
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
        barsChecking.start();

        Thread attacking = new Thread(() ->
        {
           while(true)
           {
               if(!isFocused())
                   continue;
               if(Attacker.isMonsterPresent() && !Attacker.isAttacking()) {
                   robot.keyPress(KeyEvent.VK_F3);
                   robot.keyRelease(KeyEvent.VK_F3);
               }

               try {
                   Thread.sleep(r.nextInt(100));
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        });
        attacking.start();

        Thread walking = new Thread(()->
        {
           while(true)
           {
               if(isFocused() && canWalk && !Attacker.isAttacking() && !Walker.isWalking())
                    Walker.walk();
           }
        });
        walking.start();

        String command;
        Scanner sc = new Scanner(System.in);
        while (true)
        {
            command = sc.next();
            System.out.println();
            if (command.equals("go"))
                canWalk = true;
            if (command.equals("x"))
                canWalk = false;
        }
    }

    public static void mouseClick(int x, int y)
    {
        robot.mouseMove(x,y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
}
