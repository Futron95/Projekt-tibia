package Actions;

import Main.Checker;

import java.awt.*;
import java.util.ArrayList;

public class Action {
    public static long[] actionTimes;
    public ArrayList<Condition> conditions;
    public String name;
    public int keyCode;
    public int coolDown;
    public boolean activated;
    private int maxHpToTrigger;
    private int maxManaToTrigger;
    private static Robot robot;
    public ActionType actionType;

    public enum ActionType {none, heal, attack, support, potion}

    static {
        actionTimes = new long[5];
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public boolean perform()
    {
        long time = System.currentTimeMillis();
        if (time-getActionTime()>coolDown) {
            for (Condition c:conditions) {
                if (!c.conditionMet())
                    return false;
            }
            robot.keyPress(keyCode);
            try {
                Thread.sleep(22);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.keyRelease(keyCode);
            setActionTime();
            return true;
        }
        return false;
    }

    public Action(String name, int keyCode, int maxHpToTrigger, int maxManaToTrigger, ActionType actionType, int coolDown, boolean activated)
    {
        this.name = name;
        this.keyCode = keyCode;
        this.maxHpToTrigger = maxHpToTrigger;
        conditions = new ArrayList<>();
        if (maxHpToTrigger<100)
            conditions.add(() -> {
                if(Checker.hpPercent<maxHpToTrigger)
                    return true;
                return false;
            });
        this.maxManaToTrigger = maxManaToTrigger;
        if (maxManaToTrigger<100)
            conditions.add(() -> {
                if (Checker.manaPercent<maxManaToTrigger)
                    return true;
                return false;
            });
        this.actionType = actionType;
        this.coolDown = coolDown;
        this.activated = activated;
    }

    public long getActionTime()
    {
        return actionTimes[actionType.ordinal()];
    }
    public void setActionTime()
    {
        actionTimes[actionType.ordinal()] = System.currentTimeMillis();
    }
}
