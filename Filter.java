package png;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;


public class Filter {

public static byte[] fullsub(byte[] data,int cols){
    
    try{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int mult = cols * 3;
        for (int r=0; r< data.length / mult; r++){
            //baos.write(sub(Arrays.copyOfRange(data,r*mult,r*mult + mult)));
            baos.write(nofilter(Arrays.copyOfRange(data,r*mult,r*mult + mult)));
        }
        return baos.toByteArray();
    }
    catch(Exception e){
        e.printStackTrace();
    }
    return new byte[]{};
}

private static byte[] sub(byte[] line) {  
    
    byte[] output = new byte[line.length + 1];
    output[0] = 0x01; //for sub
    output[1] = line[0];
    output[2] = line[1];
    output[3] = line[2];
    for (int i=4; i<output.length; i++){
        output[i] = (byte)((line[i-4] - line[i-1]) % 256);
    }
    
    return output;  
}

private static byte[] nofilter(byte[] line) {  
    
    byte[] output = new byte[line.length + 1];
    output[0] = 0x00; //for nothing
    for (int i=1; i<output.length; i++){
        output[i] = (byte)(line[i-1]);
    }
    
    return output;  
}
}  