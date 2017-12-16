import java.awt.*;
import Actions.Action;

public class Checker {

    private static Color barsColor = new Color(219,79,79);  //kolor serduszka używany przy określeniu czy paski many i hp są tak gdzie powinny
    private static boolean bars = true;   //wskazuje czy paski zdrowia i many sa w odpowiednim miejscu (na podstawie konkretnego piksela z serduszka kolo paska hp)

    public boolean running;             //true oznacza ze reagowanie jest wlaczone
    public Pixel pixel;                  //pixel do sprawdzenia przy orzekaniu czy dokonać akcji(leczenie albo picie many)
    public Action action;
    public static boolean checkBars()       //metoda statyczna sprawdzająca czy paski hp i many są w przewidywanym miejscu
    {
        boolean newBars = Main.robot.getPixelColor((int)Main.screenSize.getWidth()-160, 33).equals(barsColor);    //sprawdzanie czy paski hp i many są w odpowiednim miejscu
        if (bars != newBars) {
            if (newBars)
                System.out.println("Paski hp i many na odpowiednim miejscu.");
            else
                System.out.println("Paski hp i many w złym miejscu!");
            bars = newBars;
        }
        return newBars;
    }

    public Checker(boolean running, Pixel pixel, Action action)   //konstruktor
    {
        this.running = running;
        this.pixel = pixel;
        this.action = action;
    }

    private boolean checkColor()        //metoda sprawdzajaca warunek, ze pixel jest odpowiedniego koloru do wykonania akcji (dla healingu czerwien pixela musi byc mniejsza niz 150 zeby stwierdzic ze pasek hp nie jest zapelniony w danym miejscu)
    {
        Pixel checkedPixel = Pixel.getPixel(pixel.x, pixel.y);
        if (checkedPixel.color.getRed() > pixel.color.getRed()      &&
                checkedPixel.color.getGreen() > pixel.color.getGreen()  &&
                checkedPixel.color.getBlue() > pixel.color.getBlue())
            return false;
        return true;
    }

    public void check()             //metoda wciskająca programowo odpowiedni przycisk, gdy zostaną spełnione wszystkie niezbędne warunki
    {
        if(!checkBars())                //czy paski hp i many na przewidywanym miejscu?
            return;
        long newAction = System.currentTimeMillis();
        if (running && newAction-action.getActionTime()>action.coolDown && checkColor())   //sprawdza czy reagowanie jest wlaczone, ostatnia akcja była dawniej niż cooldown a sprawdzany kolor się zgadza
        {
            Main.robot.keyPress(action.keyCode);                              //programowe wcisniecie klawisza akcji (np. F1 dla healingu)
            action.setActionTime();
            try {
                Thread.sleep(Main.r.nextInt(40)+80);        //losowe opoznienie (80-120ms) aby zasymulowac czynnik ludzki
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
