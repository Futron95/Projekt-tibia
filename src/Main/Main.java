package Main;

import Actions.Action;
import Actions.HealingAction;
import Actions.PotionAction;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Scanner;

public class Main
{
    public static Dimension screenSize;
    public static Robot robot;
    public static Random r;
    private static Rectangle screenRect;
    public static volatile BufferedImage capture;
    public static volatile boolean focused=false;
    public static volatile boolean canWalk=false;           //volatile sprawia ze wszystkie watki widza zmiane wartosci
    private static int hpPercent=100;
    private static int manaPercent=100;
    private static Action exura;
    private static Action healthPotion;
    private static Action manaPotion;

    public static void main(String[] args) throws Exception
    {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenRect = new Rectangle(Main.screenSize);
        System.out.println("Wymiary ekranu: szerokosc "+screenSize.getWidth()+", wysokość "+screenSize.getHeight());
        robot = new Robot();
        capture = robot.createScreenCapture(screenRect);
        r = new Random();
        exura = new HealingAction(KeyEvent.VK_F1);
        healthPotion = new PotionAction(KeyEvent.VK_F4);
        manaPotion = new PotionAction(KeyEvent.VK_F2);

        Walker.fillVectorsList();

        Thread focusChecking = new Thread(() ->
        {
            int i, color;
            while (true) {
                capture = robot.createScreenCapture(screenRect);
                for (i = 24; i < 28; i++) {
                    color = capture.getRGB(i, 7);
                    if (color != 0xFF000000) {                      //warunek potwierdzenia aktywnosci okna to sprawdzenie czy pixele 24,25,26,27 na 7 sa czarne
                        focused = false;
                        break;
                    }
                }
                if (i==28)
                    focused = true;
            }
        });
        focusChecking.start();

        Thread barsChecking = new Thread(() ->
        {
            while(true)
            {
               if(!focused) {
                    continue;
                }
               hpPercent = Checker.getHpPercent();
               if (hpPercent<40) {
                   Action.perform(healthPotion);
                   hpPercent = Checker.getHpPercent();
               }
               if (hpPercent<80)
                   Action.perform(exura);
               manaPercent = Checker.getManaPercent();
               if (manaPercent<20)
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
            boolean monsterPresent, attackFailed;
           while(true)
           {
               monsterPresent = Attacker.isMonsterPresent();
               attackFailed = Attacker.isAttackFailed();
               if(focused && monsterPresent && attackFailed) {
                   Attacker.attack();
               }
               try {
                   Thread.sleep(100);
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
               if(focused && canWalk && (!Attacker.isMonsterPresent() || Attacker.attackFailed) && !Walker.isWalking())
                  Walker.walk();
           }
        });
        walking.start();

        String command;
        Scanner sc = new Scanner(System.in);
        System.out.println("Wpisz komende go, zeby wlaczyc chodzenie, lub x zeby je wylaczyc: ");
        while (true)
        {
            command = sc.next();
            System.out.println();
            if (command.equals("go")) {
                canWalk = true;
                System.out.println("Chodzenie wlaczone, wpisz x aby wylaczyc: ");
            }
            if (command.equals("x")) {
                canWalk = false;
                System.out.println("Chodzenie wylaczone, wpisz go aby wlaczyc: ");
            }
        }
    }

    public static void mouseClick(int x, int y)
    {
        robot.mouseMove(x,y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
}
