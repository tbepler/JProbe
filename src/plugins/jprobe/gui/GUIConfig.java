package plugins.jprobe.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import jprobe.services.ErrorHandler;

public class GUIConfig {
	
	public static final String WIDTH_TAG = "default_width";
	public static final String HEIGHT_TAG = "default_height";
	
	private int m_Width = Constants.DEFAULT_WIDTH;
	private int m_Height = Constants.DEFAULT_HEIGHT;
	
	public GUIConfig(File configFile){
		this.scanFile(configFile);
	}
	
	public int getWidth(){
		return m_Width;
	}
	
	public int getHeight(){
		return m_Height;
	}
	
	private void scanFile(File file){
		Scanner s;
		try {
			s = new Scanner(file);
			this.readFromScanner(s);
			s.close();
		} catch (FileNotFoundException e) {
			ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
		}
	}
	
	private void readFromScanner(Scanner s){
		while(s.hasNextLine()){
			this.parseLine(s.nextLine());
		}
	}
	
	private void parseLine(String line){
		line = line.trim();
		String value = this.extractValue(line);
		if(line.startsWith(WIDTH_TAG)){
			try{
				m_Width = Integer.parseInt(value);
			} catch (Exception e){
				ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
			}
		}
		if(line.startsWith(HEIGHT_TAG)){
			try{
				m_Height = Integer.parseInt(value);
			} catch (Exception e){
				ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
			}
		}
	}
	
	private String extractValue(String line){
		return line.substring(line.indexOf('=')+1).trim();
	}
	
	
	
	
	
	
	
}
