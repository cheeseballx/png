package png.mandel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javax.swing.plaf.basic.BasicBorders.MarginBorder;

import png.Utils;

public class Creator {
    
    private static double fromR = -2.01; //-2.5;
    private static double toR = 2.01; //1.5;
    private static double fromI = -2.01; //-1.5;
    private static double toI = 2.01; //1.5;

    private static int W = 12000;
    private static int H = 12000;
    

    public static byte[] getBytes(){
        double stepX = (toR - fromR) / W;
        double stepY = (toI - fromI) / H;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            for (int y = 0; y<H; y++)
            for (int x = 0; x<W; x++)
                {
                    double r = fromR + stepX * x;
                    double i = fromI + stepY * y;
                    long val = mandelbrot(r,i,255,2);

                    if (val < 0) baos.write(new byte[]{(byte)0,(byte)0,(byte)0});
                    else baos.write(getColor(val));
                }
        }
        catch(Exception e){
            System.out.println("error" + e.toString());
            e.printStackTrace();
        }    
        return baos.toByteArray();
    }
    
    private static byte[] getColor(long val){
        int red = 255;
        int green = 255;
        int blue = 255;

        if (val == 0){
            red = 0;
            green = 0;
            blue = 255;
        }

        if (val == 1){
            red = 255;
            blue  = 0;
            green = 0;
        }
        else if (val == 2){
            red = 200;
            blue =  0;
            green = 0;
        }
        else if (val == 3){
            red = 150;
            blue = 0;
            green = 0;
        }
        else if (val == 4){
            red = 100;
            blue = 0;
            green = 0;
        }
        else if (val == 5){
            red = 50;
            blue = 0;
            green = 0;
        }
        else if (val == 6){
            red = 0;
            blue = 0;
            green = 255;
        }
        else if (val == 7){
            red = 0;
            blue = 0;
            green = 200;
        }
        else if (val == 8){
            red = 0;
            blue = 0;
            green = 150;
        }
        else if (val == 9){
            red = 0;
            blue = 0;
            green = 100;
        }
        else if (val == 10){
            red = 0;
            blue = 0;
            green = 50;
        }


        return new byte[]{(byte)red,(byte)green,(byte)blue};
    } 

    private static int mandelbrot(double real,double imag,int maxIter,double dist){
        double a=0;
        double b=0;
        double temp = 0.0;

        for (int i=0; i<= maxIter; i++){
            temp = a*a - (b*b) + real;
            b = 2 * a * b + imag;
            a = temp;

            if (a*a + b*b > dist*dist)
                return i;
        }

        return -1;

    }

    public static int getX(){
        return W;
    }

    public static int getY(){
        return H;
    }
}