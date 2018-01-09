package Actions;

import Main.Checker;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Action {
    private static long[] actionTimes;
    public ArrayList<Condition> conditions;
    private String name;
    private int keyCode;
    private String hotKey;
    private int coolDown;
    private boolean activated;
    private int maxHpToTrigger;
    private int maxManaToTrigger;
    private ActionType actionType;

    private static Robot robot;

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

    public Action(String name, String hotKey, int maxHpToTrigger, int maxManaToTrigger, ActionType actionType, int coolDown, boolean activated)
    {
        this.name = name;
        this.hotKey = hotKey;
        switch (hotKey) {
            case "F1": keyCode = KeyEvent.VK_F1; break;
            case "F2": keyCode = KeyEvent.VK_F2; break;
            case "F3": keyCode = KeyEvent.VK_F3; break;
            case "F4": keyCode = KeyEvent.VK_F4; break;
            case "F5": keyCode = KeyEvent.VK_F5; break;
            case "F6": keyCode = KeyEvent.VK_F6; break;
            case "F7": keyCode = KeyEvent.VK_F7; break;
            case "F8": keyCode = KeyEvent.VK_F8; break;
            case "F9": keyCode = KeyEvent.VK_F9; break;
            case "F10": keyCode = KeyEvent.VK_F10; break;
            case "F11": keyCode = KeyEvent.VK_F11; break;
            case "F12": keyCode = KeyEvent.VK_F12; break;
            default: keyCode = KeyEvent.VK_ESCAPE;
        }
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public String getHotKey() {
        return hotKey;
    }

    public void setHotKey(String hotKey) {
        this.hotKey = hotKey;
    }

    public int getCoolDown() {
        return coolDown;
    }

    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public int getMaxHpToTrigger() {
        return maxHpToTrigger;
    }

    public void setMaxHpToTrigger(int maxHpToTrigger) {
        this.maxHpToTrigger = maxHpToTrigger;
    }

    public int getMaxManaToTrigger() {
        return maxManaToTrigger;
    }

    public void setMaxManaToTrigger(int maxManaToTrigger) {
        this.maxManaToTrigger = maxManaToTrigger;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }
}
