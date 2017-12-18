package Actions;

import Main.*;

public abstract class Action {
    public static long potionTime;
    public static long healingTime;
    public static long supportTime;
    public static long attackTime;

    public int keyCode;
    public int coolDown;

    public static boolean perform(Action action)
    {
        long newAction = System.currentTimeMillis();
        if (newAction-action.getActionTime()>action.coolDown) {
            Main.robot.keyPress(action.keyCode);
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
