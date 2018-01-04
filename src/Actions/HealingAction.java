package Actions;

public class HealingAction extends Action {

    public HealingAction(String name, int keyCode, int maxHpToTrigger, int maxManaToTrigger) {
        super(name, keyCode, maxHpToTrigger, maxManaToTrigger,1000);
    }

    public HealingAction(String name, int keyCode, int maxHpToTrigger, int maxManaToTrigger, int coolDown) {
        super(name, keyCode, maxHpToTrigger, maxManaToTrigger, coolDown);
    }

    public void setActionTime()
    {
        Action.healingTime = System.currentTimeMillis();
    }

    public long getActionTime()
    {
        return Action.healingTime;
    }
}
