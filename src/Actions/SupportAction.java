package Actions;

public class SupportAction extends Action {

    public SupportAction(int keyCode) {
        super(keyCode, 1000);
    }

    public SupportAction(int keyCode, int coolDown) {
        super(keyCode, coolDown);
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
