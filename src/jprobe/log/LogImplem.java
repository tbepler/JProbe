package jprobe.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.osgi.framework.Bundle;

public class LogImplem implements Log{
	
	private Calendar calendar;
	private BufferedWriter writer;
	
	public LogImplem(File log) throws IOException{
		calendar = new GregorianCalendar(TimeZone.getDefault());
		writer = new BufferedWriter(new FileWriter(log));
	}
	
	@Override
	public void write(Bundle bundle, String output) {
		try {
			writer.write("<"+calendar.getTime()+"><"+bundle.getSymbolicName()+">"+ output);
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
