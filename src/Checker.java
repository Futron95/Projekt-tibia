import java.awt.*;

public class Checker {
    public boolean running;             //true oznacza ze leczenie jest wlaczone
    public static boolean bars = true;   //wskazuje czy paski zdrowia i many sa w odpowiednim miejscu (na podstawie konkretnego piksela z serduszka kolo paska hp)
    public Pixel pixel;                  //pixel do sprawdzenia przy orzekaniu czy dokonać akcji(leczenie albo picie many)
    private int keyCode;                //kod klawisza do wcisniecia w celu wywolania akcji, np. F1 to KeyEvent.VK_F1
    private long lastAction = 0;        //przechowuje moment, w którym ostatni raz wciśnięto klawisz akcji
    private static Color barsColor = new Color(219,79,79);  //kolor serduszka używany przy określeniu czy paski many i hp są tak gdzie powinny

    public static boolean checkBars()
    {
        boolean newBars = Main.robot.getPixelColor(2400, 33).equals(barsColor);    //sprawdzanie czy paski hp i many są w odpowiednim miejscu
        if (bars != newBars) {
            if (newBars)
                System.out.println("Paski hp i many na odpowiednim miejscu.");
            else
                System.out.println("Paski hp i many w złym miejscu!");
            bars = newBars;
        }
        return newBars;
    }

    private boolean checkColor()        //metoda sprawdzajaca warunek, ze pixel jest odpowiedniego koloru do wykonania akcji (dla healingu red musi byc mniejsze niz 150)
    {
        Pixel checkedPixel = Pixel.getPixel(pixel.x, pixel.y);
        if (checkedPixel.color.getRed() > pixel.color.getRed()      &&
                checkedPixel.color.getGreen() > pixel.color.getGreen()  &&
                checkedPixel.color.getBlue() > pixel.color.getBlue())
            return false;
        return true;
    }

    public void check()
    {
        long newAction = System.currentTimeMillis();
        if(!checkBars())
            return;
        if (running && newAction-lastAction>1000 && checkColor()) { //jezeli wartosc R piksela na pasku zdrowia jest mniejsza niz 150 znaczy ze postac nie ma 100% hp
            Main.robot.keyPress(keyCode);                                //programowe wcisniecie F1
            lastAction = System.currentTimeMillis();
            try {
                Thread.sleep(Main.r.nextInt(40)+80);        //losowe opoznienie (80-120ms) aby zasymulowac czynnik ludzki
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Checker(boolean running, Pixel pixel, int keyCode)
    {
        this.running = running;
        this.pixel = pixel;
        this.keyCode = keyCode;
    }
}
