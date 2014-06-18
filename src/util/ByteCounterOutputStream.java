package util;

import java.io.IOException;
import java.io.OutputStream;

public class ByteCounterOutputStream extends OutputStream{
	
	private long m_Bytes = 0;
	private final OutputStream m_Out;
	
	public ByteCounterOutputStream(OutputStream out){
		m_Out = out;
	}
	
	@Override
	public void close() throws IOException {
		m_Out.close();
	}

	@Override
	public void flush() throws IOException {
		m_Out.flush();
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		m_Out.write(b, off, len);
		m_Bytes += len;
	}

	@Override
	public void write(byte[] b) throws IOException {
		m_Out.write(b);
		m_Bytes += b.length;
	}

	@Override
	public void write(int b) throws IOException {
		m_Out.write(b);
		++m_Bytes;
	}
	
	public long bytesWritten(){
		return m_Bytes;
	}

}
