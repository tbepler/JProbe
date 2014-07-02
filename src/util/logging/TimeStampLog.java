package util.logging;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Calendar;

public class TimeStampLog implements Log{

	private final File m_LogFile;
	
	public TimeStampLog(File log){
		if(!log.exists()){
			log.getParentFile().mkdirs();
		}
		m_LogFile = log;
	}
	
	@Override
	public void write(Level level, String message) {
		if(this.shouldLog(level)){
			OutputStream out = null;
			PrintWriter writer = null;
			try {
				out = new BufferedOutputStream(new FileOutputStream(m_LogFile, true));
				writer = new PrintWriter(out);
				this.printPreliminaries(writer);
				this.printMessage(writer, level, message);
			} catch (FileNotFoundException e1) {
				//derp
			}finally{
				if(writer != null){
					writer.close();
				}
				if(out != null){
					try {
						out.close();
					} catch (IOException e) {
						//derp
					}
				}
			}
		}
	}
	
	protected boolean shouldLog(Level l){
		return true;
	}
	
	protected void printPreliminaries(PrintWriter writer){
		writer.println("-----");
		writer.print("<");
		writer.print(Calendar.getInstance().getTime());
		writer.print(">");
		writer.print("<");
		writer.print(Thread.currentThread().getName());
		writer.print(">");
	}
	
	protected void printMessage(PrintWriter writer, Level level, String message){
		writer.print(" ");
		writer.print(level.toString());
		writer.print(": ");
		writer.println(message);
	}

}
