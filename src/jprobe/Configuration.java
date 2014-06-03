package jprobe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import jprobe.services.Debug;
import jprobe.services.ErrorHandler;

/**
 * This class is responsible for reading and writing the preferences file.
 * 
 * @author Tristan Bepler
 *
 */
public class Configuration {
	
	private static final Debug DEFAULT_DEBUG_LEVEL = Debug.LOG;
	private static final String DEFAULT_STORAGE_CLEAN = "onFirstInit";
	
	private static final String DEBUG_HELP = "//debug values: 0=off, 1=log, 2=full";
		
	public static final String TAG_DEBUG_LEVEL = "debug";
	public static final String TAG_STORAGE_CLEAN = "felix_storage_clean";
	public static final String TAG_AUTOSAVE = "autosave";
	public static final String TAG_AUTOSAVE_MINS = "autosave_frequency";
	public static final String TAG_MAX_AUTOSAVES = "num_autosaves";
	public static final String TAG_LOAD_WORKSPACE = "autoload_last_workspace";
	
	private String[] m_CmdLineArgs;
	
	private Debug m_DebugLevel = DEFAULT_DEBUG_LEVEL;
	private String m_FelixStorageClean = DEFAULT_STORAGE_CLEAN;
	private boolean m_Autosave = Constants.DEFAULT_AUTOSAVE;
	private double m_AutosaveFreq = Constants.DEFAULT_AUTOSAVE_FREQUENCE;
	private int m_MaxAutosaves = Constants.DEFAULT_MAX_AUTOSAVE;
	private boolean m_LoadWorkspace = Constants.DEFAULT_LOAD_WORKSPACE;
	
	public Configuration(File configFile, String[] args){
		m_CmdLineArgs = args;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(configFile));
			String line;
			while((line = reader.readLine()) != null){
				try{
					parse(line);
				} catch (Exception e){
					ErrorHandler.getInstance().handleWarning(configFile + ": unable to parse line \""+line+"\"", null);
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			ErrorHandler.getInstance().handleException(e, null);
			configFile.getParentFile().mkdirs();
			this.writeFile(configFile);
		} catch (Exception e){
			ErrorHandler.getInstance().handleException(e, null);
		}
		Debug.setLevel(m_DebugLevel);
	}

	private void parse(String line){
		if(line.startsWith("//")){
			return;
		}
		String[] tokens = line.split("=");
		String tag = tokens[0].trim();
		String val = tokens[1].trim();
		if(tag.equals(TAG_DEBUG_LEVEL)){
			m_DebugLevel = Debug.fromString(val);
		}else if(tag.equals(TAG_STORAGE_CLEAN)){
			m_FelixStorageClean = val;
		}else if(tag.equals(TAG_AUTOSAVE)){
			m_Autosave = Boolean.parseBoolean(val);
		}else if(tag.equals(TAG_AUTOSAVE_MINS)){
			m_AutosaveFreq = Double.parseDouble(val);
		}else if(tag.equals(TAG_MAX_AUTOSAVES)){
			m_MaxAutosaves = Integer.parseInt(val);
		}else if(tag.equals(TAG_LOAD_WORKSPACE)){
			m_LoadWorkspace = Boolean.parseBoolean(val);
		}
	}
	
	public void writeFile(File f){
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(f));
			writeln(writer, DEBUG_HELP);
			writeln(writer, TAG_DEBUG_LEVEL, m_DebugLevel.toString());
			writeln(writer, TAG_STORAGE_CLEAN, m_FelixStorageClean);
			writeln(writer, TAG_AUTOSAVE, String.valueOf(m_Autosave));
			writeln(writer, TAG_AUTOSAVE_MINS, String.valueOf(m_AutosaveFreq));
			writeln(writer, TAG_MAX_AUTOSAVES, String.valueOf(m_MaxAutosaves));
			writeln(writer, TAG_LOAD_WORKSPACE, String.valueOf(m_LoadWorkspace));
			writer.close();
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, null);
		}
	}
	
	private static void writeln(Writer w, String line){
		try {
			w.write(line + "\n");
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, null);
		}
	}
	
	private static void writeln(Writer w, String tag, String value){
		try {
			w.write(tag + " = " + value + "\n");
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, null);
		}
	}
	
	public String[] getArgs(){
		return m_CmdLineArgs;
	}
	
	public boolean loadPrevWorkspace(){
		return m_LoadWorkspace;
	}
	
	public int getMaxAutosaves(){
		return m_MaxAutosaves;
	}
	
	public double getAutosaveFrequency(){
		return m_AutosaveFreq;
	}
	
	public boolean autosave(){
		return m_Autosave;
	}
	
	public String getFelixStorageClean(){
		return m_FelixStorageClean;
	}
	
	public Debug getDebugLevel(){
		return m_DebugLevel;
	}
	
}
