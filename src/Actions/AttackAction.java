package Actions;

public class AttackAction extends Action {

    public AttackAction(int keyCode) {
        super(keyCode, 1000);
    }

    public AttackAction(int keyCode, int coolDown) {
        super(keyCode, coolDown);
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
