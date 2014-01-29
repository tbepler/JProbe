package jprobe.save;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * This class courtesy of StackOverflow. Found at http://stackoverflow.com/questions/1194656/appending-to-an-objectoutputstream
 * 
 * @author Tristan
 *
 */
public class AppendingObjectOutputStream extends ObjectOutputStream{

	public AppendingObjectOutputStream(OutputStream out) throws IOException {
		super(out);
	}
	
	@Override
	protected void writeStreamHeader() throws IOException{
		//do not write a header, rather reset
		this.reset();
	}

}
