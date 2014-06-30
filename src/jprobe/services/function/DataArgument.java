package jprobe.services.function;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileFilter;

import util.progress.ProgressEvent;
import util.progress.ProgressListener;
import util.progress.ProgressEvent.Type;
import jprobe.services.JProbeCore;
import jprobe.services.Workspace;
import jprobe.services.data.Data;
import jprobe.services.data.ReadException;
import jprobe.services.function.components.DataArgsComponent;
import jprobe.services.function.components.DataArgsComponent.DataValidFunction;

public abstract class DataArgument<P,D extends Data> extends AbstractArgument<P, DataArgsComponent<D>>{
	
	public static final String DEFAULT_TAG = "FILE";
	
	private static String generatePrototype(String tag, int min, int max){
		if(min == 1 && max == 1){
			return tag;
		}
		if(max == Integer.MAX_VALUE){
			return tag + "{"+min+"+}";
		}
		return tag + "{"+min+"-"+max+"}";
	}
	
	private final JProbeCore m_Core;
	private final int m_Min;
	private final int m_Max;
	private final boolean m_AllowDuplicates;
	private final Class<D> m_DataClass;
	
	protected DataArgument(
			JProbeCore core,
			String name,
			String tooltip,
			String category,
			Character shortFlag,
			String prototypeVal,
			boolean optional,
			Class<D> dataClass,
			int minArgs,
			int maxArgs,
			boolean allowDuplicates
			){
		super(name, tooltip, category, shortFlag, generatePrototype(prototypeVal, minArgs, maxArgs), optional);
		m_DataClass = dataClass;
		m_Core = core;
		m_Min = minArgs;
		m_Max = maxArgs;
		m_AllowDuplicates = allowDuplicates;
	}
	
	protected DataArgument(
			JProbeCore core,
			String name,
			String tooltip,
			String category,
			Character shortFlag,
			boolean optional,
			Class<D> dataClass,
			int minArgs,
			int maxArgs,
			boolean allowDuplicates
			){
		this(core, name, tooltip, category, shortFlag, DEFAULT_TAG, optional, dataClass, minArgs, maxArgs, allowDuplicates);
	}
	
	/**
	 * Creates the component that will be used by this argument to retrieve data from the user in a GUI.
	 * 
	 * @param core - JProbeCore from which data can be retrieved
	 * @param minArgs - minimum number of data objects required
	 * @param maxArgs - maximum number of data objects allowable
	 * @param allowDuplicates - should duplicate data objects be allowed
	 * @param dataClass - class of the data objects that are allowed
	 * @param validFunction - a function that indicates whether a data object is valid or not
	 * @return the DataArgsComponent that will be displayed for this argument
	 */
	public DataArgsComponent<D> createComponent( Workspace w ){
		return new DataArgsComponent<D>(
				w,
				m_Min,
				m_Max,
				m_AllowDuplicates,
				m_DataClass,
				new DataValidFunction(){

					@Override
					public boolean isValid(Data d) {
						return DataArgument.this.isValid(d);
					}

				}
				);
	}
	
	/**
	 * Process the data passed to this data argument and fill the given parameter object accordingly.
	 * @param params
	 * @param data
	 */
	protected abstract void process(P params, List<D> data);

	@Override
	public boolean isValid(DataArgsComponent<D> component) {
		for(D data : component.getDataArgs()){
			if(!this.isValid(data)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * This determines whether the given Data object is valid or not for this argument.
	 * @param d
	 * @return True if the given Data object is valid, False otherwise
	 */
	protected boolean isValid(Data d){
		return true;
	}

	@Override
	public void process(P params, DataArgsComponent<D> component) {
		this.process(params, component.getDataArgs());
	}
	
	@Override
	public void parse(ProgressListener l, P params, String[] args){
		if(args.length < m_Min || args.length > m_Max){
			String message;
			if(m_Min == m_Max){
				message = this.getName() + " requires "+m_Min+" arguments.";
			}else{
				message = this.getName() + " requires between "+m_Min+" and "+m_Max+" arguments.";
			}
			throw new RuntimeException( message + " Received "+args.length);
		}
		List<D> data = new ArrayList<D>();
		for(int i=0; i<args.length; i++){
			String arg = args[i];
			data.add(i, parse(
					m_Core,
					m_DataClass,
					new DataValidFunction(){

						@Override
						public boolean isValid(Data d) {
							return DataArgument.this.isValid(d);
						}

					},
					arg,
					l
					));
		}
		
		this.process(params, data);
		
	}
	
	public static <D extends Data> D parse(JProbeCore core, Class<D> dataClass, DataValidFunction validFunction, String arg, ProgressListener l){
		for(Workspace w : core.getWorkspaces()){
			if(w.contains(arg)){
				Data d = w.getData(arg);
				if(validFunction.isValid(d) && dataClass.isAssignableFrom(d.getClass())){
					return dataClass.cast(d);
				}
			}
		}
		l.update(new ProgressEvent(null, Type.UPDATE, "Reading "+dataClass.getSimpleName()+" from file "+arg));
		D read = readFile(arg, core, dataClass);
		if(read == null){
			throw new RuntimeException("Unable to read data class \""+dataClass+"\" from file \""+arg+"\"");
		}
		if(validFunction.isValid(read) && dataClass.isAssignableFrom(read.getClass())){
			l.update(new ProgressEvent(null, Type.COMPLETED, "Done reading "+dataClass.getSimpleName()+" from file "+arg));
			return dataClass.cast(read);
		}else{
			throw new RuntimeException("Data in file \""+arg+"\" is not valid");
		}
	}
	
	protected static <D extends Data> D readFile(String file, JProbeCore core, Class<D> dataClass){
		File f = new File(file);
		if(!f.canRead()){
			throw new RuntimeException("File \""+file+"\" not readable");
		}
		D d = null;
		List<FileFilter> formats = core.getReadFormats(dataClass);
		if(formats.isEmpty()){
			throw new RuntimeException("No valid read formats for data class: "+dataClass);
		}
		for(FileFilter format : formats){
			try {
				d = core.readData(dataClass, format, new BufferedInputStream(new FileInputStream(f)));
				if(d != null){
					return d;
				}
			} catch (FileNotFoundException e) {
				//do nothing, try next filter
			} catch (ReadException e) {
				//do nothing, try next filter
			}
		}
		return d;
	}


}
