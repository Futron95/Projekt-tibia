package Actions;

public class SupportAction extends Action {

    public SupportAction(String name, int keyCode, int maxHpToTrigger, int maxManaToTrigger)
    {
        super(name, keyCode, maxHpToTrigger, maxManaToTrigger, 1000);
    }

    public SupportAction(String name, int keyCode, int maxHpToTrigger, int maxManaToTrigger, int coolDown) {
        super(name, keyCode, maxHpToTrigger, maxManaToTrigger, coolDown);
    }

    public void setActionTime()
    {
        Action.supportTime = System.currentTimeMillis();
    }

    public long getActionTime()
    {
        return Action.supportTime;
    }
}
