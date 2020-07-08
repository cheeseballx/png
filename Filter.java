package png;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Filter {

    public final static int NO_FILTER = 0;            //DO NO FILTERING AT ALL
    public final static int SUB_FILTER = 1;           //DO THE SUBFILTERING ALL OVER THE IMAGE
    public final static int TOP_FILTER = 2;           //TOP FILTER ALL OVER THE IMAGE
    public final static int AVERAGE_FILTER = 3;       //AVERAGE FILTER ALL OVER THE IMAGE
    public final static int PAETH_FILTER = 4;         //PAETH FILTER ALL OVER THE IMAGE
    public final static int CHK_BFR_FILTER = 5;       //DO ALL FILTERS AND CHECK SMALLEST SIZE
    public final static int CHK_PER_LINE_FILTER = 6;  //DO ALL FILTERS EVERY LINE AND CHECK FOR SMALLEST SIZE 


    public static byte[] filter(byte[] data,int cols,int bpp, int type){
        int bpl = cols * bpp;
        int rows = data.length / bpl;

        try{
            switch(type){
                case NO_FILTER:         return fullno(data,rows,bpl); 
                case SUB_FILTER:        return fullsub(data,rows,bpl,bpp);
                case TOP_FILTER:        return fulltop(data,rows,bpl);
                case AVERAGE_FILTER:    return fullavrg(data,rows,bpl,bpp);
                case PAETH_FILTER:      return fullpaeth(data,rows,bpl,bpp); 
                
                default:  return fullno(data,rows,bpl); 
            }
        }
        catch(Exception e){
            System.out.println("ERROR " + e.toString());
        }
        return null;
    }

    private static byte[] fullno(byte[] data,int rows,int bpl) throws IOException{
        
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (int r=0; r< rows; r++){
                byte[] line = Arrays.copyOfRange(data,r * bpl,r*bpl + bpl);
                line = no(line);
                baos.write(line);
            }
        return baos.toByteArray();
    }

    private static byte[] fullsub(byte[] data,int rows,int bpl,int bpp) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int r=0; r< rows; r++){
            byte[] line = Arrays.copyOfRange(data,r * bpl,r*bpl + bpl);
            line = sub(line,bpp);
            baos.write(line);
        }
        return baos.toByteArray(); 
    }

    private static byte[] fulltop(byte[] data,int rows,int bpl) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int r=0; r< rows; r++){
            byte[] line = Arrays.copyOfRange(data,r * bpl,r*bpl + bpl);
            byte[] top = new byte[line.length];
            if (r>0) top = Arrays.copyOfRange(data,(r-1)*bpl, (r-1)*bpl + bpl);
            line = top(line,top);
            baos.write(line);
        }
        return baos.toByteArray(); 
    }

    private static byte[] fullavrg(byte[] data,int rows,int bpl,int bpp) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int r=0; r< rows; r++){
            byte[] line = Arrays.copyOfRange(data,r * bpl,r*bpl + bpl);
            byte[] top = new byte[line.length];
            if (r>0) top = Arrays.copyOfRange(data,(r-1)*bpl, (r-1)*bpl + bpl);
            line = average(line,top,bpp);
            baos.write(line);
        }
        return baos.toByteArray(); 
    }

    private static byte[] fullpaeth(byte[] data,int rows,int bpl,int bpp) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int r=0; r< rows; r++){
            byte[] line = Arrays.copyOfRange(data,r * bpl,r*bpl + bpl);
            byte[] top = new byte[line.length];
            if (r>0) top = Arrays.copyOfRange(data,(r-1)*bpl, (r-1)*bpl + bpl);
            line = paeth(line,top,bpp);
            baos.write(line);
        }
        return baos.toByteArray(); 
    }


    private static byte[] sub(byte[] line,int bpp) {  
    
        byte[] output = new byte[line.length + 1];
        output[0] = 0x01; //sub filter

        

        for (int i=1; i<output.length; i++){
            byte sub = i-bpp-1 >= 0 ? line[i-bpp-1] : 0;
            output[i] = (byte)((line[i-1] - sub)% 256);
        }
        return output;  
    }

    private static byte[] top(byte[] line,byte[] top) {  
    
        byte[] output = new byte[line.length + 1];
        output[0] = 0x02; //top filter
        
        for (int i=1; i<output.length; i++)
            output[i] = (byte)((line[i-1] - top[i-1])% 256);
        return output;  
    }

    private static byte[] average(byte[] line,byte[] top,int bpp) {  
    
        byte[] output = new byte[line.length + 1];
        output[0] = 0x03; //average filter

        for (int i=1; i<output.length; i++){
            int sub = i-bpp-1 >= 0 ? line[i-bpp-1] : 0;
            int pix = line[i-1];
            int above = top[i-1];

            if (pix < 0) pix+=256;
            if (above < 0) above+=256;
            if (sub < 0) sub+=256;
       
            int calc = pix - (above + sub) / 2;
            calc %=256;
            
            output[i] = (byte)calc;//(byte)( (line[i-1] - (byte)( (sub+(int)top[i-1]) /2 )) % 256);
        }
        return output;  
    }

    private static byte[] paeth(byte[] line,byte[] top,int bpp) {  
    
        byte[] output = new byte[line.length + 1];
        output[0] = 0x04; //paeth filter

        for (int i=1; i<output.length; i++){
            int sub = i-bpp-1 >= 0 ? line[i-bpp-1] : 0;
            int pix = line[i-1];
            int above = top[i-1];
            int subabove = i-bpp-1 >=0 ? top[i-bpp-1] : 0;

            if (pix < 0) pix+=256;
            if (above < 0) above+=256;
            if (sub < 0) sub+=256;
            if (subabove < 0 ) subabove+=256;
            
            int calc = paethPredictor(sub,above, subabove);
            calc = (pix-calc) % 256;
            output[i] = (byte)calc;
        }
        return output;  
    }

    private static byte[] no(byte[] line) {  
        byte[] output = new byte[line.length + 1];
        output[0] = 0x00; //for nothing

        for (int i=1; i<output.length; i++)
            output[i] = (byte)(line[i-1]);
        return output;  
    }


    private static int paethPredictor(int a,int b,int c){
        int p = a + b - c;
        int pa = Math.abs(p - a);
        int pb = Math.abs(p - b);
        int pc = Math.abs(p - c);
        if (pa <= pb && pa <= pc)
            return a;
        else if (pb <= pc)
            return b;
        else 
            return c;
    }
}  