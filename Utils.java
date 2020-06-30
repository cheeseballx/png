package png;

public class Utils {

    public static byte[] longToBytes(final long l) {
        return new byte[] {
             (byte)(l >> 24),
             (byte)(l >> 16),
             (byte)(l >>  8),
             (byte)(l      )
        };
     }

     public static String toByteString(final byte... bytes) {
        String out = "";

        for (final byte b:bytes){
            out += String.format("%02x ", b);
        }
        return out;
     }
    
}