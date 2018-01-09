package Main;

import Actions.Action;
import Actions.HealingAction;
import Actions.PotionAction;
import Actions.SupportAction;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import static java.awt.event.InputEvent.CTRL_MASK;
import static java.awt.event.KeyEvent.*;

public class Main extends Application
{
    public static Dimension screenSize;
    public static Robot robot;
    public static Random r;
    private static Rectangle screenRect;
    public static volatile BufferedImage capture;
    public static volatile boolean focused=false;
    public static volatile boolean canWalk=false;           //volatile sprawia ze wszystkie watki widza zmiane wartosci
    public static volatile boolean magicLevel = false;
    public static volatile boolean canHunt = false;
    public static volatile boolean canAntyKick = false;
    private static ArrayList<Action> actionList;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("gui.fxml"));
        primaryStage.setTitle("UltraBot v.1");
        primaryStage.setScene(new Scene(root, 400, 350));
        primaryStage.show();


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
                if(canHunt==true)
                {
                    monsterPresent = Attacker.isMonsterPresent();
                    attackFailed = Attacker.isAttackFailed();       //zwraca true jezeli postac nie atakuje, lub atak sie nie udal (8 sekund bez zmiany koloru paska hp atakowanego potwora)
                    if(focused && monsterPresent && attackFailed)
                        Attacker.attack();
                    if(focused && canWalk && (Attacker.attackFailed || !monsterPresent) && !Walker.isWalking())
                        Walker.walk();
                }
            }
        });
        hunting.start();

        Thread mLeveling = new Thread(()->
        {
            Action magicLeveling = new SupportAction("Exura", VK_F10, 20000, 25);
            while(true){
                if(magicLevel==true){
                    Action.perform(magicLeveling);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mLeveling.start();

        Thread antyKick = new Thread(()->
        {
            while (true)
            {
                if(canAntyKick=true)
                {
                    robot.keyPress(KeyEvent.VK_CONTROL);
                    robot.keyPress(KeyEvent.VK_UP);
                    robot.keyRelease(KeyEvent.VK_UP);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.keyPress(KeyEvent.VK_DOWN);
                    robot.keyRelease(KeyEvent.VK_DOWN);
                    robot.keyRelease(KeyEvent.VK_CONTROL);
                    try {
                        Thread.sleep(600000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        antyKick.start();

        /*String command;
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
        */
    }

    public static void main(String[] args) throws Exception
    {
        launch(args);

    }



    public static void mouseClick(int x, int y)
    {
        robot.mouseMove(x,y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
}
