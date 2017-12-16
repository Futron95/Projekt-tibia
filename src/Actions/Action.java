package Actions;

public abstract class Action {
    public static long potionTime;
    public static long healingTime;
    public static long supportTime;
    public static long attackTime;

    public int keyCode;
    public int coolDown;

    public Action(int keyCode, int coolDown)
    {
        this.keyCode = keyCode;
        this.coolDown = coolDown;
    }

    public abstract long getActionTime();
    public abstract void setActionTime();
}
