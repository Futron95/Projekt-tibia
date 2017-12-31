package Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Mateusz on 30.12.2017.
 */
public class Walker {

    private static Robot robot;
    public static int lastWalkingVector = 0;
    private static int plusAngle, minusAngle;
    private static Rectangle screenRect = new Rectangle(Main.screenSize);
    private static BufferedImage capture;
    public static ArrayList<Pixel> vectors = new ArrayList<Pixel>();    //lista punktow, ktore beda sprawdzane przy wyznaczaniu drogi
    public static void fillVectorsList()
    {
        int x,y = 113;                                  //wspolrzedne srodka minimapy
        x = (int)Main.screenSize.getWidth()-115;
        int px,py;                                      //wspolrzedne obliczanych punktow
        boolean exist;
        for (int angle=0;angle<360;angle+=3)        //kazdy punkt jest 3 stopnie w innym kierunku
        {
            px = (int)(Math.cos(Math.toRadians(angle))*10)+x;
            py = (int)(Math.sin(Math.toRadians(angle))*10)+y;
            exist=false;
            for (Pixel pix:vectors) {           //sprawdzanie czy punkt o danych wspolzednych nie wystapil juz (dzieje sie tak poniewaz wspolrzedne sa zaokragleniem do calosci)
                if (pix.x == px && pix.y == py) {
                    exist = true;
                    break;
                }
            }
            if (!exist)
                vectors.add(new Pixel(new Color(0), px, py));
        }
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static boolean isWalking()       //metoda sprawdzajaca czy postac sie porusza. najpierw aktualizuje informacje o kolorach punktow wokol postaci na minimapie a nastepnie 20 razy co 100ms sprawdza czy cos sie zmienilo
    {
        int i,j,color;

        capture = robot.createScreenCapture(screenRect);
        for(i = 0;i<vectors.size();i++)
        {
            vectors.get(i).color = new Color(capture.getRGB(vectors.get(i).x, vectors.get(i).y));
        }

        for(i=0;i<20;i++)
        {
            /*try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            capture = robot.createScreenCapture(screenRect);
            for(j = 0;j<vectors.size();j++)
            {
                color = capture.getRGB(vectors.get(j).x, vectors.get(j).y);
                if(vectors.get(j).color.getRGB() != color) {
                    return true;
                }
            }
        }
        System.out.println("is walking return false");
        return false;
    }

    public static boolean walk()
    {
        System.out.println("Walk");
        plusAngle = lastWalkingVector;
        minusAngle = lastWalkingVector;
        Pixel pix;
        capture = robot.createScreenCapture(screenRect);
        do          //petla szukajaca na minimapie pixela koloru gleby po ktorej mozna chodzic, wektory sa przegladane tak zeby na poczatku sprawdzic ostatnio uzyty a potem sprawdzac coraz szerzej
        {
            pix = vectors.get(plusAngle);
            if(capture.getRGB(pix.x, pix.y)==Pixel.GROUND_COLOR)
            {
                System.out.println("kolor gleby znaleziony");
                if(Attacker.isAttacking())
                    return true;
                Main.mouseClick(pix.x, pix.y);
                if (isWalking()) {
                    lastWalkingVector = plusAngle;
                    return true;
                }
            }
            pix = vectors.get(minusAngle);
            if(minusAngle != plusAngle && capture.getRGB(pix.x, pix.y)==Pixel.GROUND_COLOR)
            {
                System.out.println("kolor gleby znaleziony");
                if(Attacker.isAttacking())
                    return true;
                Main.mouseClick(pix.x, pix.y);
                if(isWalking())
                {
                    lastWalkingVector = minusAngle;
                    return true;
                }
            }
            plusAngle = (++plusAngle)%vectors.size();
            minusAngle = --minusAngle;
            if (minusAngle<0)
                minusAngle = vectors.size()-1;
        }while (plusAngle!=minusAngle);
        System.out.println("walk return false");
        return false;
    }
}
