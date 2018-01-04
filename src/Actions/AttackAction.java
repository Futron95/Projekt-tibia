package Actions;

public class AttackAction extends Action {

    public AttackAction(String name, int keyCode, int maxHpToTrigger, int maxManaToTrigger) {
        super(name, keyCode, maxHpToTrigger, maxManaToTrigger, 1000);
    }

    public AttackAction(String name, int keyCode, int maxHpToTrigger, int maxManaToTrigger, int coolDown) {
        super(name, keyCode, maxHpToTrigger, maxManaToTrigger, coolDown);
    }

    public void setActionTime()
    {
        Action.attackTime = System.currentTimeMillis();
    }

    public long getActionTime()
    {
        return Action.attackTime;
    }
}
