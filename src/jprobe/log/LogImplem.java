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
	
	public LogImplem(File log){
		calendar = new GregorianCalendar(TimeZone.getDefault());
		try {
			writer = new BufferedWriter(new FileWriter(log));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void write(Bundle bundle, String message) {
		try {
			writer.write("<"+calendar.getTime()+"><"+bundle.getSymbolicName()+">"+ message);
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
