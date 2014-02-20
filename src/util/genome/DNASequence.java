package util.genome;

import java.io.Serializable;

/**
 * A more memory efficient way of storing DNA sequences than strings.
 * 
 * @author Tristan Bepler
 *
 */
public class DNASequence implements Serializable{
	
	private static final byte A = 00000000;
	private static final byte T = 00000001;
	private static final byte G = 00000010;
	private static final byte C = 00000011;
	
	private static byte[] parseString(String seq){
		byte[] bytes = new byte[seq.length() + Math.min(1, seq.length() % 4)];
		for(int i=0; i<seq.length(); i++){
			assign(bytes, seq.charAt(i), i);
		}
		return bytes;
	}
	
	private static void assign(byte[] bytes, char base, int pos){
		int index = pos/4;
		byte b = bytes[index];
		int offset = (pos % 4)*2;
		if(base == 'a' || base == 'A'){
			b = (byte) (b & ~(0x03 << offset));
		}else if(base == 'c' || base == 'C'){
			b = (byte) (b | (0x03 << offset));
		}else if(base == 't' || base == 'T'){
			b = (byte) (b | (0x01 << offset));
			b = (byte) (b & ~(0x01 << (offset + 1)));
		}else if(base == 'g' || base == 'G'){
			b = (byte) (b & ~(0x01 << offset));
			b = (byte) (b | (0x01 << (offset + 1)));
		}else{
			throw new RuntimeException("Error: "+base+" is not a DNA character!");
		}
		bytes[index] = b;
	}
	
	private byte[] m_Sequence;
	private long m_Length;
	
	
	
}
