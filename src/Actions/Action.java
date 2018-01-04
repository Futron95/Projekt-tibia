package Actions;

import Main.Checker;

import java.awt.*;

public abstract class Action {
    public static long potionTime;
    public static long healingTime;
    public static long supportTime;
    public static long attackTime;
    public String name;
    public int keyCode;
    public int coolDown;
    public boolean activated;
    private int maxHpToTrigger;
    private int maxManaToTrigger;
    private static Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static boolean perform(Action action)
    {
        long time = System.currentTimeMillis();
        if (time-action.getActionTime()>action.coolDown && Checker.hpPercent<= action.maxHpToTrigger && Checker.manaPercent<=action.maxManaToTrigger) {
            robot.keyPress(action.keyCode);
            try {
                Thread.sleep(22);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.keyRelease(action.keyCode);
            action.setActionTime();
            return true;
        }
        return false;
    }

    public Action(String name, int keyCode, int maxHpToTrigger, int maxManaToTrigger, int coolDown)
    {
        this.name = name;
        this.keyCode = keyCode;
        this.coolDown = coolDown;
        this.maxHpToTrigger = maxHpToTrigger;
        this.maxManaToTrigger = maxManaToTrigger;
        this.activated = true;
    }

    public abstract long getActionTime();
    public abstract void setActionTime();
}
