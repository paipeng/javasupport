package deng.javaexamples;

//http://www.rgagnon.com/javadetails/java-0026.html
//http://www.rgagnon.com/javadetails/java-0007.html
public class UnsignedByte {
	public static void main(String args[]) {
		
		//bigAndLittleEndian();
		
//		byte b1 = 127;
//		byte b2 = -128;
//		byte b3 = -1;
//		System.out.println(b1);
//		System.out.println(b2);
//		System.out.println(b3);
//		System.out.println(unsignedByteToInt(b1));
//		System.out.println(unsignedByteToInt(b2));
//		System.out.println(unsignedByteToInt(b3));
//		System.out.println(intToSignedByte(127));
//		System.out.println(intToSignedByte(128));
//		System.out.println(intToSignedByte(255));
//	    /*
//		127
//		-128
//		-1
//		127
//		128
//		255
//		127
//		-128
//		-1
//	    */

		//System.out.println();		
		//bytesToInt(new byte[] { i2b(0x00), i2b(0x00), i2b(0x00), i2b(0x01) });
		
//		System.out.println();		
//		bytesToInt(new byte[] { i2b(0x00), i2b(0x00), i2b(0x00), i2b(0x00) });
//		bytesToInt(new byte[] { i2b(0xFF), i2b(0xFF), i2b(0xFF), i2b(0xFF) });
//		bytesToInt(new byte[] { i2b(0x7F), i2b(0xFF), i2b(0xFF), i2b(0xFF) });
//		bytesToInt(new byte[] { i2b(0x80), i2b(0x00), i2b(0x00), i2b(0x00) });
//		
//		System.out.println();		
//		System.out.println(0);
//		System.out.println(-1);
//		System.out.println(Integer.MAX_VALUE);
//		System.out.println(Integer.MIN_VALUE);		
////		System.out.println(Integer.toHexString(Integer.MAX_VALUE));
////		System.out.println(Integer.toHexString(Integer.MIN_VALUE));
//
//		System.out.println();
//		System.out.println(0x00000000);
//		System.out.println(0xFFFFFFFF);
//		System.out.println(0x7FFFFFFF);
//		System.out.println(0x80000000);
//		
//		defaultUnsignedByteValue();
		
		System.out.println(byteToHex(i2b(0xFF)));
		System.out.println(byteToHex(i2b(0xAB)));
		System.out.println(byteToHex(i2b(0x0D)));
		System.out.println(byteToHex2(i2b(0xFF)));
		System.out.println(byteToHex2(i2b(0xAB)));
		System.out.println(byteToHex2(i2b(0x0D)));
	}
	
	private static void defaultUnsignedByteValue() {
		byte[] fourBytes = new byte[4];
		for(byte b : fourBytes) {
			System.out.println(byteToHex(b));
		}
	}

	private static void bigAndLittleEndian() {
	    //  before 0x01020304
	    //    after  0x04030201
	    int v = 0x01020304;
	    System.out.println("before : 0x" + Integer.toString(v,16));
	    System.out.println("after  : 0x" + Integer.toString(swabInt(v),16));
	}
	
	public final static int swabInt(int v) {
		return  (v >>> 24) | (v << 24) | 
	      ((v << 8) & 0x00FF0000) | ((v >> 8) & 0x0000FF00);
    }

	private static byte i2b(int i) {
		return intToSignedByte(i);
	}
	private static byte intToSignedByte(int i) {
		return Integer.valueOf(i).byteValue();
	}
	
	public static String byteToHex(byte b){
	  int i = b & 0xFF;
	  return Integer.toHexString(i);
	}
	public static String byteToHex2(byte b){
	  return String.format("%02X", b);
	}
		
	private static void bytesToInt(byte[] buf) {
		int i = 0;
		int pos = 0;
		i += unsignedByteToInt(buf[pos++]) << 24;
		i += unsignedByteToInt(buf[pos++]) << 16;
		i += unsignedByteToInt(buf[pos++]) << 8;
		i += unsignedByteToInt(buf[pos++]) << 0;
		System.out.println(i);
	}

	public static int unsignedByteToInt(byte b) {
		return (int) b & 0xFF;
	}
}