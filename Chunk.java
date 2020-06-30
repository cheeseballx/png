package png;

import java.util.zip.CRC32;

public class Chunk{
    
    public enum Type{
        IHDR(new byte[]{0x49,0x48,0x44,0x52}),
        IEND(new byte[]{0x49,0x45,0x4e,0x44}),
        IDAT(new byte[]{0x49,0x44,0x41,0x54});

        private final byte[] value;
        private Type(final byte[] value) { this.value = value; }
        public byte[] getValue(){return value; }
    }

    private Type type;
    private byte[] data;

    public Chunk(Type type,byte[] data){
        this.type = type;   //take the type here
        this.data = data;   //take the data
    }

    private byte[] calcCrc(){

        byte[] test = new byte[4 + this.data.length];
        System.arraycopy(this.type.getValue(), 0,test,0, 4);
        System.arraycopy(this.data, 0,test,4, this.data.length);

        CRC32 crc = new CRC32();
        crc.update(test);

        return Utils.longToBytes(crc.getValue());
    }

    private byte[] calcLen(){
        return new byte[] {
            (byte)(this.data.length >>> 24),
            (byte)(this.data.length >>> 16),
            (byte)(this.data.length >>> 8),
            (byte)this.data.length };
    }

    @Override
    public String toString() {
        return String.format("|| %s(%d) | %s(%s) | %s| %s||", 
            Utils.toByteString(this.calcLen()),
            this.data.length, 
            Utils.toByteString(this.type.getValue()),
            this.type.name(), 
            Utils.toByteString(this.data),
            Utils.toByteString(this.calcCrc()) );
    }

    public byte[] genBytes(){
        byte[] out = new byte[ (4 + 4 + this.data.length + 4) ];
       
        System.arraycopy(this.calcLen(),         0,out,0,                    4);
        System.arraycopy(this.type.getValue(),   0,out,4,                    4);
        System.arraycopy(this.data,              0,out,8,                    this.data.length);
        System.arraycopy(this.calcCrc(),         0,out,8 + this.data.length, 4);
        
        return out; 
    }
}