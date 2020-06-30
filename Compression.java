package png;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;  
import java.util.zip.Inflater;  

public class Compression {
    private static final Logger LOG = Logger.getLogger("name1");

public static byte[] compress(byte[] data,int level) throws IOException {  
    Deflater deflater = new Deflater();
    deflater.setLevel(level);
    
    deflater.setInput(data);  
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);   
    deflater.finish();  
    byte[] buffer = new byte[1024];   
    while (!deflater.finished()) {  
     int count = deflater.deflate(buffer); // returns the generated code... index  
     outputStream.write(buffer, 0, count);   
    }  
    outputStream.close();  
    byte[] output = outputStream.toByteArray();  
    LOG.info("Original: " + data.length);  
    LOG.info("Compressed: " + output.length);  
    return output;  
   }  
   public static byte[] decompress(byte[] data) throws IOException, DataFormatException {  
    Inflater inflater = new Inflater();   
    inflater.setInput(data);  
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);  
    byte[] buffer = new byte[1024];  
    while (!inflater.finished()) {  
     int count = inflater.inflate(buffer);  
     outputStream.write(buffer, 0, count);  
    }  
    outputStream.close();  
    byte[] output = outputStream.toByteArray();  
    LOG.info("Original: " + data.length);  
    LOG.info("Compressed: " + output.length);  
    return output;  
   }
}