package Main;

import Actions.Action;
import Actions.HealingAction;
import Actions.PotionAction;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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
    private static ArrayList<Action> actionList;

    public static void main(String[] args) throws Exception
    {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenRect = new Rectangle(Main.screenSize);
        System.out.println("Wymiary ekranu: szerokosc "+screenSize.getWidth()+", wysokość "+screenSize.getHeight());
        robot = new Robot();
        capture = robot.createScreenCapture(screenRect);
        r = new Random();
        actionList = new ArrayList<>();
        actionList.add(new HealingAction("Exura", KeyEvent.VK_F1, 80, 100));
        actionList.add(new PotionAction("Health potion", KeyEvent.VK_F4, 40, 100));
        actionList.add(new PotionAction("Mana Potion", KeyEvent.VK_F2, 100, 30));
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

        Thread barsUpdate = new Thread(()->
        {
            while(true)
            {
                if(!focused) {
                    continue;
                }
                Checker.updateHpPercent();
                Checker.updateManaPercent();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        barsUpdate.start();

        Thread performingActions = new Thread(() ->
        {
            while(true)
            {
                if(!focused) {
                    continue;
                }
                for (Action action:actionList) {
                    if (action.activated)
                        Action.perform(action);
                }
            }
        });
        performingActions.start();

        Thread hunting = new Thread(()->
        {
            boolean monsterPresent, attackFailed;
            while(true) {
                monsterPresent = Attacker.isMonsterPresent();
                attackFailed = Attacker.isAttackFailed();       //zwraca true jezeli postac nie atakuje, lub atak sie nie udal (8 sekund bez zmiany koloru paska hp atakowanego potwora)
                if(focused && monsterPresent && attackFailed)
                    Attacker.attack();
                if(focused && canWalk && (Attacker.attackFailed || !monsterPresent) && !Walker.isWalking())
                    Walker.walk();
            }
        });
        hunting.start();

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
