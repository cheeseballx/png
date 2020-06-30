package png.mandel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javax.swing.plaf.basic.BasicBorders.MarginBorder;

import png.Utils;

public class Creator {
    
    private static double fromR = -2.5;
    private static double toR = 1.5;
    private static double fromI = -1.5;
    private static double toI = 1.5;

    private static int W = 400;
    private static int H = 300;
    

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
                    //long val = diverge(0,0,r, i,0);
                    long val = mandelbrot(r,i,255,2);

                    if (val < 0) baos.write(new byte[]{(byte)0,(byte)0,(byte)0});
                    else if (val >255) baos.write(new byte[]{0,0,0});
                    else baos.write(new byte[]{Utils.longToBytes(val)[3] ,0,0});
                }
        }
        catch(Exception e){
            System.out.println("error" + e.toString());
            e.printStackTrace();
        }    
        return baos.toByteArray();
    }

    public static int diverge(double zr, double zi,double r,double i,int depth){
        double zrtemp = zr*zr - (zi*zi) + r;
        zi = 2 * zr * zi + i;
        zr = zrtemp;

        if (depth > 256){
            return -1;
        }

        double sqdist = zr*zr + zi*zi;
        if (sqdist > 4 ){
            return depth;
        }

        return diverge(zr, zi, r, i,depth+1); 
    }

    public static int mandelbrot(double real,double imag,int maxIter,double dist){
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