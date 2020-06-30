package png;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/*
The IHDR chunk must appear FIRST. It contains:

   Width:              4 bytes
   Height:             4 bytes
   Bit depth:          1 byte
   Color type:         1 byte
   Compression method: 1 byte
   Filter method:      1 byte
   Interlace method:   1 byte
*/

public class PNG {

    public static final byte[] MAGIC_PNG = { (byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a };

    private int width;
    private int height;
    private byte[] raw;
    
    public PNG(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void setData(byte[] data){
        this.raw = data;
    }

    public void writePic(String picname){

        byte[] pixels = {};
        try {

            pixels = Filter.fullsub(this.raw,this.width);
            pixels = Compression.compress(pixels,9);
        
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        byte[] startbytes = new byte[ (4 +4 + 5) ];
    
        System.arraycopy(Utils.longToBytes(width), 0,startbytes,                   0, 4);
        System.arraycopy(Utils.longToBytes(height),0,startbytes,                   4, 4);
        System.arraycopy(new byte[] { 0x08, 0x02, 0x00, 0x00, 0x00 },0,startbytes, 8, 5);

        Chunk start = new Chunk(Chunk.Type.IHDR,startbytes);
        Chunk data = new Chunk(Chunk.Type.IDAT, pixels);
        Chunk end = new Chunk(Chunk.Type.IEND, new byte[] {});

        try {
            File file = new File(picname);
            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(MAGIC_PNG);
            fos.write(start.genBytes());
            fos.write(data.genBytes());
            fos.write(end.genBytes());

            fos.close();

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }   
}