package Main;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Mateusz on 30.12.2017.
 */
public class Walker {
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
            System.out.println("px "+px);
            exist=false;
            for (Pixel pix:vectors) {           //sprawdzanie czy punkt o danych wspolzednych nie wystapil juz (dzieje sie tak poniewaz wspolrzedne sa zaokragleniem do calosci)
                if (pix.x == px && pix.y == py) {
                    exist = true;
                    break;
                }
            }
            if (!exist)
                vectors.add(new Pixel(new Color(0xFF996633), px, py));  //kolor pixela to kolor gleby po ktorej mozna chodzic w podziemiach
        }
    }
}
