import java.awt.*;

public class Checker {

    private static Color barsColor = new Color(219,79,79);  //kolor serduszka używany przy określeniu czy paski many i hp są tak gdzie powinny
    private static boolean bars = true;   //wskazuje czy paski zdrowia i many sa w odpowiednim miejscu (na podstawie konkretnego piksela z serduszka kolo paska hp)

    public boolean running;             //true oznacza ze reagowanie jest wlaczone
    public Pixel pixel;                  //pixel do sprawdzenia przy orzekaniu czy dokonać akcji(leczenie albo picie many)
    private int keyCode;                //kod klawisza do wcisniecia w celu wywolania akcji, np. F1 to KeyEvent.VK_F1
    private long lastAction = 0;        //przechowuje moment, w którym ostatni raz wciśnięto klawisz akcji

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

    public Checker(boolean running, Pixel pixel, int keyCode)   //konstruktor
    {
        this.running = running;
        this.pixel = pixel;
        this.keyCode = keyCode;
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
        if (running && newAction-lastAction>1000 && checkColor())   //jezeli reagowanie jest wlaczone, ostatnia akcja była wiecej niż 1000ms temu a sprawdzany kolor się zgadza
        {
            Main.robot.keyPress(keyCode);                                //programowe wcisniecie klawisza akcji (np. F1 dla healingu)
            lastAction = System.currentTimeMillis();
            try {
                Thread.sleep(Main.r.nextInt(40)+80);        //losowe opoznienie (80-120ms) aby zasymulowac czynnik ludzki
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
