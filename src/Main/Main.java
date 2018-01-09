package Main;

import Actions.Action;
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

import static java.awt.event.KeyEvent.*;

public class Main extends Application
{
    public static Dimension screenSize;
    public static Robot robot;
    public static Random r;

    private static Rectangle screenRect;
    public static volatile BufferedImage capture;
    public static volatile boolean running = true;
    public static volatile boolean focused=false;
    public static volatile boolean canWalk=false;           //volatile sprawia ze wszystkie watki widza zmiane wartosci
    public static volatile boolean magicLevel = false;
    public static volatile boolean canHunt = false;
    public static volatile boolean canAntyKick = false;
    public static ArrayList<Action> actionList;

    @Override
    public void start(Stage primaryStage) throws Exception{

        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenRect = new Rectangle(Main.screenSize);
        System.out.println("Wymiary ekranu: szerokosc "+screenSize.getWidth()+", wysokość "+screenSize.getHeight());
        robot = new Robot();
        capture = robot.createScreenCapture(screenRect);
        r = new Random();
        actionList = new ArrayList<>();
        //actionList.add(new Action("Exura", "F1", 80, 100, Action.ActionType.heal, 1000, true));
        actionList.add(new Action("Health potion", "F4", 40, 100, Action.ActionType.potion, 1000, true));
        actionList.add(new Action("Mana Potion", "F2", 100, 30, Action.ActionType.potion, 1000, true));
        Parent root = FXMLLoader.load(getClass().getResource("gui.fxml"));
        primaryStage.setTitle("UltraBot v.1");
        primaryStage.setScene(new Scene(root, 640, 480));
        primaryStage.show();
        Walker.fillVectorsList();

        Thread focusChecking = new Thread(() ->
        {
            int i, color;
            while (running) {
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
            while(running)
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
            while(running)
            {
                if(!focused) {
                    continue;
                }
                for (Action action:actionList) {
                    if (action.isActivated())
                        action.perform();
                }
            }
        });
        performingActions.start();

        Thread hunting = new Thread(()->
        {
            boolean monsterPresent, attackFailed;
            while(running) {
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
            Action magicLeveling = new Action("Exura", "F10", 100, 25, Action.ActionType.heal, 1000, true);
            while(running){
                if(magicLevel && focused){
                    magicLeveling.perform();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //mLeveling.start();

        Thread antyKick = new Thread(()->
        {
            while (running)
            {
                if(canAntyKick && focused)
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
        //antyKick.start();

        primaryStage.setOnCloseRequest(e ->
        {
            running = false;
        });

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
