package Actions;

public class PotionAction extends Action {

    public PotionAction(String name, int keyCode, int maxHpToTrigger, int maxManaToTrigger) {
        super(name, keyCode, maxHpToTrigger, maxManaToTrigger,1000);
    }

    public PotionAction(String name, int keyCode, int maxHpToTrigger, int maxManaToTrigger, int coolDown) {
        super(name, keyCode, maxHpToTrigger, maxManaToTrigger, coolDown);
    }

    public void setActionTime()
    {
        Action.potionTime = System.currentTimeMillis();
    }

    public long getActionTime()
    {
        return Action.potionTime;
    }
}
