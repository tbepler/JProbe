package plugins.jprobe.gui;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
	public static final String LOAD_WORKSPACE_TAG = "autoload_previous_workspaces";
	public static final String LAST_WORKSPACE_TAG = "previous_workspaces";
	
	private static final String SEP = File.separator;
	
	private final File m_File;
	
	private List<Dimension> m_Dim = new ArrayList<Dimension>();
	//Frame.NORMAL
	private List<Integer> m_ExtendedState = new ArrayList<Integer>();
	//Constants.CENTER
	private List<Integer> m_X = new ArrayList<Integer>();
	private List<Integer> m_Y = new ArrayList<Integer>();
	private boolean m_Autosave = true;
	private long m_AutosaveFreq = 10 * 60 * 1000; //every 10 minutes
	private int m_MaxAutosaves = 3;
	private boolean m_LoadPrevWorkspaces = true;
	private List<String> m_PrevWorkspaces = new ArrayList<String>();
	
	public GUIConfig(File configFile){
		m_File = configFile;
		this.scanFile(configFile);
	}
	
	public Dimension getDimension(int index){
		if(index < m_Dim.size() && index >= 0){
			return m_Dim.get(index);
		}
		return Constants.DEFAULT_DIMENSION;
	}

	public void setDimension(int index, Dimension d){
		if(index >= 0){
			while(m_Dim.size() <= index){
				m_Dim.add(Constants.DEFAULT_DIMENSION);
			}
			m_Dim.set(index, d);
		}
	}
	
	public List<Dimension> getDimensions(){
		return Collections.unmodifiableList(m_Dim);
	}
	
	public void setDimensions(List<Dimension> dim){
		m_Dim = dim;
	}
	
	public int getExtendedState(int index){
		if(index < m_ExtendedState.size() && index >= 0){
			return m_ExtendedState.get(index);
		}
		return Constants.DEFAULT_EXTENDED_STATE;
	}
	
	public void setExtendedState(int index, int state){
		if(index >= 0){
			while(m_ExtendedState.size() <= index){
				m_ExtendedState.add(Constants.DEFAULT_EXTENDED_STATE);
			}
			m_ExtendedState.set(index, state);
		}
	}
	
	public List<Integer> getExtendedStates(){
		return Collections.unmodifiableList(m_ExtendedState);
	}
	
	public void setExtendedStates(List<Integer> states){
		m_ExtendedState = states;
	}
	
	public int getX(int index){
		if(index < m_X.size() && index >= 0){
			return m_X.get(index);
		}
		return Constants.CENTER;
	}

	public void setX(int index, int x){
		if(index >= 0){
			while(m_X.size() <= index){
				m_X.add(Constants.CENTER);
			}
			m_X.set(index, x);
		}
	}
	
	public List<Integer> getXs(){
		return Collections.unmodifiableList(m_X);
	}
	
	public void setXs(List<Integer> xs){
		m_X = xs;
	}
	
	public int getY(int index){
		if(index < m_Y.size() && index >= 0){
			return m_Y.get(index);
		}
		return Constants.CENTER;
	}
	
	public void setY(int index, int y){
		if(index >= 0){
			while(m_Y.size() <= index){
				m_Y.add(Constants.CENTER);
			}
			m_Y.set(index, y);
		}
	}
	
	public List<Integer> getYs(){
		return Collections.unmodifiableList(m_Y);
	}
	
	public void setYs(List<Integer> ys){
		m_Y = ys;
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
		m_LoadPrevWorkspaces = load;
	}
	
	public boolean getLoadWorkspace(){
		return m_LoadPrevWorkspaces;
	}
	
	public String getPrevWorkspace(int index){
		if(m_PrevWorkspaces.size() < index && index >= 0){
			return m_PrevWorkspaces.get(index);
		}
		return null;
	}
	
	public void setPrevWorkspace(int index, String path){
		if(index >= 0){
			while(m_PrevWorkspaces.size() <= index){
				m_PrevWorkspaces.add(null);
			}
			m_PrevWorkspaces.set(index, path);
		}
	}
	
	public void setPrevWorkspaces(List<String> paths){
		m_PrevWorkspaces = paths;
	}
	
	public List<String> getPrevWorkspace(){
		return Collections.unmodifiableList(m_PrevWorkspaces);
	}
	
	public void save(){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(m_File));
			List<Integer> widths = new ArrayList<Integer>(m_Dim.size());
			List<Integer> heights = new ArrayList<Integer>(m_Dim.size());
			for(Dimension d : m_Dim){
				widths.add(d.width);
				heights.add(d.height);
			}
			writeln(writer, WIDTH_TAG, widths);
			writeln(writer, HEIGHT_TAG, heights);
			writeln(writer, EXTENDEDSTATE_TAG, m_ExtendedState);
			writeln(writer, X_TAG, m_X);
			writeln(writer, Y_TAG, m_Y);
			writeln(writer, AUTOSAVE_TAG, m_Autosave);
			writeln(writer, AUTOSAVE_FREQ_TAG, m_AutosaveFreq);
			writeln(writer, MAX_AUTOSAVES_TAG, m_MaxAutosaves);
			writeln(writer, LOAD_WORKSPACE_TAG, m_LoadPrevWorkspaces);
			writeln(writer, LAST_WORKSPACE_TAG, m_PrevWorkspaces);
			writer.close();
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
		}
	}
	
	private static <T> void writeln(Writer w, String tag, T value){
		try {
			String val;
			if(value instanceof Collection){
				StringBuilder builder = new StringBuilder();
				boolean first = true;
				for(Object o : (Collection<?>) value){
					if(o != null){
						if(first){
							builder.append(o);
							first = false;
						}else{
							builder.append(SEP);
							builder.append(o);
						}
					}
				}
				val = builder.toString();
			}else{
				val = value.toString();
			}
			w.write(tag + "=" + val + "\n");
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, GUIActivator.getBundle());
		}
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
	
	private void parseWidths(String val){
		String[] tokens = val.split(SEP);
		for(int i=0; i<tokens.length; i++){
			try{
				Dimension d = this.getDimension(i);
				d.width = Integer.parseInt(tokens[i].trim());
				this.setDimension(i, d);
			}catch(Exception e){
				//do nothing and proceed
			}
		}
	}
	
	private void parseHeights(String val){
		String[] tokens = val.split(SEP);
		for(int i=0; i<tokens.length; i++){
			try{
				Dimension d = this.getDimension(i);
				d.height = Integer.parseInt(tokens[i].trim());
				this.setDimension(i, d);
			}catch(Exception e){
				//do nothing and proceed
			}
		}
	}
	
	private void parseExtendedStates(String value){
		String[] tokens = value.split(SEP);
		for(int i=0; i<tokens.length; i++){
			try{
				this.setExtendedState(i, Integer.parseInt(tokens[i].trim()));
			}catch(Exception e){
				//do nothing and proceed
			}
		}
	}
	
	private void parseXs(String value){
		String[] tokens = value.split(SEP);
		for(int i=0; i<tokens.length; i++){
			try{
				this.setX(i, Integer.parseInt(tokens[i].trim()));
			}catch(Exception e){
				//do nothing and proceed
			}
		}
	}
	
	private void parseYs(String value){
		String[] tokens = value.split(SEP);
		for(int i=0; i<tokens.length; i++){
			try{
				this.setY(i, Integer.parseInt(tokens[i].trim()));
			}catch(Exception e){
				//do nothing and proceed
			}
		}
	}
	
	private void parsePrevWorkspaces(String value){
		String[] tokens = value.split(SEP);
		for(int i=0; i<tokens.length; i++){
			this.setPrevWorkspace(i, tokens[i].trim());
		}
	}
	
	private void parse(String line){
		String[] tokens = line.split("=");
		String tag = tokens[0].trim();
		String value = tokens[1].trim();
		if(tag.equals(WIDTH_TAG)){
			this.parseWidths(value);
		}else if(tag.equals(HEIGHT_TAG)){
			this.parseHeights(value);
		}else if(tag.equals(EXTENDEDSTATE_TAG)){
			this.parseExtendedStates(value);
		}else if(tag.equals(X_TAG)){
			this.parseXs(value);
		}else if(tag.equals(Y_TAG)){
			this.parseYs(value);
		}else if(tag.equals(AUTOSAVE_TAG)){
			m_Autosave = Boolean.parseBoolean(value);
		}else if(tag.equals(AUTOSAVE_FREQ_TAG)){
			m_AutosaveFreq = Long.parseLong(value);
		}else if(tag.equals(MAX_AUTOSAVES_TAG)){
			m_MaxAutosaves = Integer.parseInt(value);
		}else if(tag.equals(LOAD_WORKSPACE_TAG)){
			m_LoadPrevWorkspaces = Boolean.parseBoolean(value);
		}else if(tag.equals(LAST_WORKSPACE_TAG)){
			this.parsePrevWorkspaces(value);
		}
	}
	
	
	
}
