package Actions;

public class PotionAction extends Action {

    public PotionAction(int keyCode) {
        super(keyCode, 1000);
    }

    public PotionAction(int keyCode, int coolDown) {
        super(keyCode, coolDown);
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
