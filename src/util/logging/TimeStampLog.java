package util.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class TimeStampLog implements Log{

	private BufferedWriter writer;
	
	public TimeStampLog(File log){
		if(!log.exists()){
			log.getParentFile().mkdirs();
		}
		try {
			writer = new BufferedWriter(new FileWriter(log));
		} catch (IOException e) {
			System.err.println("Error creating file writer: "+e.getMessage());
		}
	}
	
	@Override
	public void write(String message) {
		if(writer == null){
			return;
		}
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("<").append(Calendar.getInstance().getTime()).append(">");
			builder.append("<").append(Thread.currentThread().getName()).append(">");
			builder.append(message).append("\n");
			writer.write(builder.toString());
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
