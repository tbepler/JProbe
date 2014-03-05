package util.genome.reader;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import util.genome.reader.query.LocationBoundedSequenceQuery;
import util.genome.reader.query.LocationQuery;
import util.genome.reader.query.SequenceQuery;

public class TwoBitReader extends AbstractGenomeReader{
	
	private class HeaderException extends Exception{
		private static final long serialVersionUID = 1L;

		public HeaderException(String message) {
			super(message);
			// TODO Auto-generated constructor stub
		}
	}
	
	public static final int BYTES_PER_INT = 4;
	
	public static final int SIGNATURE = 0x1A412743;
	public static final int VERSION = 0x00000000;
	public static final int RESERVED = 0x00000000;
	
	private static final String FILE_MODE = "r";
	
	private final RandomAccessFile m_GenomeFile;
	private final int m_SeqCount;
	private final boolean m_ByteSwap;
	
	public TwoBitReader(File genomeFile){
		super();
		try {
			m_GenomeFile = new RandomAccessFile(genomeFile, FILE_MODE);
			//read file header
			m_GenomeFile.seek(0);
			//read signature int
			int signature = m_GenomeFile.readInt();
			//if signature doesn't match try byte swapping and check again
			if(signature != SIGNATURE){
				signature = Integer.reverseBytes(signature);
				if(signature != SIGNATURE){
					throw new HeaderException("Invalid signature");
				}
				m_ByteSwap = true;
			}else{
				m_ByteSwap = false;
			}
			m_GenomeFile.seek(m_GenomeFile.getFilePointer()+BYTES_PER_INT);
			//read version
			int version = this.nextInt();
			if(version != VERSION){
				throw new HeaderException("Invalid version");
			}
			//read sequence count
			m_SeqCount = this.nextInt();
			//read the reserved field
			int reserved = this.nextInt();
			if(reserved != RESERVED){
				throw new HeaderException("Invalid reserved field");
			}
			//read the file index
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Read the int at the file offset and advance the offset past that int
	 * @return int read
	 */
	private int nextInt() throws IOException{
		int read = this.readInt();
		m_GenomeFile.seek(m_GenomeFile.getFilePointer() + BYTES_PER_INT);
		return read;
	}
	
	/**
	 * Reads an int at the current file offset
	 * @return int read
	 */
	private int readInt() throws IOException{
		return m_ByteSwap ? Integer.reverseBytes(m_GenomeFile.readInt()) : m_GenomeFile.readInt();
	}

	@Override
	public void read(List<LocationQuery> locationQueries, List<SequenceQuery> sequenceQueries, List<LocationBoundedSequenceQuery> boundedQueries) {
		// TODO Auto-generated method stub
		
	}

}
