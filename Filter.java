package png;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class Filter {

    public static byte[] fullsub(byte[] data,int cols){
    
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int mult = cols * 3;
            for (int r=0; r< data.length / mult; r++){
            
                byte[] line = Arrays.copyOfRange(data,r*mult,r*mult + mult);
                
                //just need this for top-filter and the average
                byte[] lineabove = new byte[line.length];
                if (r>0)
                    lineabove = Arrays.copyOfRange(data,(r-1)*mult,(r-1)*mult + mult);

                //line = nofilter(line);
                //line = sub(line,3);
                //line = top(line,lineabove);
                line = average(line,lineabove,3);

                baos.write(line);
            }
            return baos.toByteArray();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new byte[]{};
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
            output[i] = (byte)( (line[i-1] - (int)( (sub+(int)top[i-1]) /2 )) % 256);
        }
        return output;  
    }

    private static byte[] nofilter(byte[] line) {  
        byte[] output = new byte[line.length + 1];
        output[0] = 0x00; //for nothing
        for (int i=1; i<output.length; i++)
            output[i] = (byte)(line[i-1]);
        return output;  
    }
}  