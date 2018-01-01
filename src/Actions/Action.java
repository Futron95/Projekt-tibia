package Actions;

import java.awt.*;

public abstract class Action {
    public static long potionTime;
    public static long healingTime;
    public static long supportTime;
    public static long attackTime;
    private static Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public int keyCode;
    public int coolDown;

    public static boolean perform(Action action)
    {
        long newAction = System.currentTimeMillis();
        if (newAction-action.getActionTime()>action.coolDown) {
            robot.keyPress(action.keyCode);
            robot.keyRelease(action.keyCode);
            return true;
        }
        return false;
    }

    public Action(int keyCode, int coolDown)
    {
        this.keyCode = keyCode;
        this.coolDown = coolDown;
    }

    public abstract long getActionTime();
    public abstract void setActionTime();
}
