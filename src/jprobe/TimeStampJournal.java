package jprobe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import jprobe.services.Journal;

import org.osgi.framework.Bundle;

public class TimeStampJournal implements Journal{

	private BufferedWriter writer;
	
	public TimeStampJournal(File log){
		if(!log.exists()){
			log.getParentFile().mkdirs();
		}
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
			writer.write("<"+Calendar.getInstance().getTime()+"><"+bundle.getSymbolicName()+">"+ message+"\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
