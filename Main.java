package png;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import jdk.jshell.execution.Util;
import png.mandel.Creator;

public class Main {

    public static void main(String args[]) {

        byte[] raw = Creator.getBytes();

        PNG pic = new PNG(Creator.getX(),Creator.getY());
        pic.setData(raw);
        pic.writePic();

        //makeSystemCallXXD();

        System.exit(0);
    }

    static void makeSystemCallXXD(){
        Process p;
        try {
            p = Runtime.getRuntime().exec("xxd test.png");
        
         String s = null;   
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
            
            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    
}