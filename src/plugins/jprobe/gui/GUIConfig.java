package plugins.jprobe.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import jprobe.services.ErrorHandler;

public class GUIConfig {
	
	public static final String WIDTH_TAG = "width";
	public static final String HEIGHT_TAG = "height";
	public static final String EXTENDEDSTATE_TAG = "extended_state";
	public static final String X_TAG = "xpos";
	public static final String Y_TAG = "ypos";
	public static final String AUTOSAVE_TAG = "autosave";
	public static final String AUTOSAVE_FREQ_TAG = "autosave_frequency_millis";
	public static final String MAX_AUTOSAVES_TAG = "max_autosaves";
	public static final String LOAD_WORKSPACE_TAG = "autoload_last_workspace";
	public static final String LAST_WORKSPACE_TAG = "last_workspace";
	
	private final File m_File;
	
	private Dimension m_Dim = new Dimension(Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT);
	private int m_ExtendedState = Frame.NORMAL;
	private int m_X = Constants.CENTER;
	private int m_Y = Constants.CENTER;
	private boolean m_Autosave = true;
	private long m_AutosaveFreq = 10 * 60 * 1000; //every 10 minutes
	private int m_MaxAutosaves = 3;
	private boolean m_LoadWorkspace = true;
	private String m_LastWorkspace = "";
	
	public GUIConfig(File configFile){
		m_File = configFile;
		this.scanFile(configFile);
	}
	
	public void setAutosave(boolean autosave){
		m_Autosave = autosave;
	}
	
	public boolean getAutosave(){
		return m_Autosave;
	}
	
	public void setAutosaveFreq(long freq){
		m_AutosaveFreq = freq;
	}
	
	public long getAutosaveFreq(){
		return m_AutosaveFreq;
	}
	
	public void setMaxAutosaves(int max){
		m_MaxAutosaves = max;
	}
	
	public int getMaxAutosaves(){
		return m_MaxAutosaves;
	}
	
	public void setLoadWorkspace(boolean load){
		m_LoadWorkspace = load;
	}
	
	public boolean getLoadWorkspace(){
		return m_LoadWorkspace;
	}
	
	public void setLastWorkspace(String path){
		m_LastWorkspace = path;
	}
	
	public String getLastWorkspace(){
		return m_LastWorkspace;
	}
	
	public void save(Dimension d, int extendedState, int x, int y){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(m_File));
			writeln(writer, WIDTH_TAG, d.width);
			writeln(writer, HEIGHT_TAG, d.height);
			writeln(writer, EXTENDEDSTATE_TAG, extendedState);
			writeln(writer, X_TAG, x);
			writeln(writer, Y_TAG, y);
			writeln(writer, AUTOSAVE_TAG, m_Autosave);
			writeln(writer, AUTOSAVE_FREQ_TAG, m_AutosaveFreq);
			writeln(writer, MAX_AUTOSAVES_TAG, m_MaxAutosaves);
			writeln(writer, LOAD_WORKSPACE_TAG, m_LoadWorkspace);
			writeln(writer, LAST_WORKSPACE_TAG, m_LastWorkspace);
			writer.close();
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
		}
	}
	
	private static <T> void writeln(Writer w, String tag, T value){
		try {
			w.write(tag + "=" + value + "\n");
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
		}
	}
	
	public Dimension getDimension(){
		return m_Dim;
	}
	
	public int getExtendedState(){
		return m_ExtendedState;
	}
	
	public int getX(){
		return m_X;
	}
	
	public int getY(){
		return m_Y;
	}
	
	private void scanFile(File file){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			try {
				while((line = reader.readLine()) != null){
					try{
						this.parse(line);
					} catch(Exception e){
						//ignore
					}
				}
				reader.close();
			} catch (IOException e) {
				ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
			}
		} catch (FileNotFoundException e) {
			ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
		}
	}
	
	private void parse(String line){
		String[] tokens = line.split("=");
		String tag = tokens[0].trim();
		String value = tokens[1].trim();
		if(tag.equals(WIDTH_TAG)){
			m_Dim.width = Integer.parseInt(value);
		}else if(tag.equals(HEIGHT_TAG)){
			m_Dim.height = Integer.parseInt(value);
		}else if(tag.equals(EXTENDEDSTATE_TAG)){
			m_ExtendedState = Integer.parseInt(value);
		}else if(tag.equals(X_TAG)){
			m_X = Integer.parseInt(value);
		}else if(tag.equals(Y_TAG)){
			m_Y = Integer.parseInt(value);
		}else if(tag.equals(AUTOSAVE_TAG)){
			m_Autosave = Boolean.parseBoolean(value);
		}else if(tag.equals(AUTOSAVE_FREQ_TAG)){
			m_AutosaveFreq = Long.parseLong(value);
		}else if(tag.equals(MAX_AUTOSAVES_TAG)){
			m_MaxAutosaves = Integer.parseInt(value);
		}else if(tag.equals(LOAD_WORKSPACE_TAG)){
			m_LoadWorkspace = Boolean.parseBoolean(value);
		}else if(tag.equals(LAST_WORKSPACE_TAG)){
			m_LastWorkspace = value;
		}
	}
	
	
	
}
