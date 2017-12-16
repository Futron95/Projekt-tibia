package Actions;

public class HealingAction extends Action {

    public HealingAction(int keyCode) {
        super(keyCode, 1000);
    }

    public HealingAction(int keyCode, int coolDown) {
        super(keyCode, coolDown);
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
