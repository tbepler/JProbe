package jprobe.services.function;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.filechooser.FileFilter;

import util.progress.ProgressEvent;
import util.progress.ProgressListener;
import util.progress.ProgressEvent.Type;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.data.DataReader;
import jprobe.services.function.components.DataArgsComponent;
import jprobe.services.function.components.DataArgsComponent.DataValidFunction;
import jprobe.services.function.components.ValidListener;
import jprobe.services.function.components.ValidNotifier;

public abstract class DataArgument<P,D extends Data> extends AbstractArgument<P> implements ValidListener {
	
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
	//lazily instantiate the component
	private DataArgsComponent<D> m_Component = null;
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
	protected DataArgsComponent<D> createDataArgsComponent(
			JProbeCore core,
			int minArgs,
			int maxArgs,
			boolean allowDuplicates,
			Class<D> dataClass,
			DataValidFunction validFunction
			){
		return new DataArgsComponent<D>(
				core,
				minArgs,
				maxArgs,
				allowDuplicates,
				dataClass,
				validFunction
				);
	}
	
	/**
	 * Process the data passed to this data argument and fill the given parameter object accordingly.
	 * @param params
	 * @param data
	 */
	protected abstract void process(P params, List<D> data);

	private void initComponent(){
		m_Component = this.createDataArgsComponent(
				m_Core,
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
		m_Component.addListener(this);
	}

	@Override
	public boolean isValid() {
		if(m_Component == null) this.initComponent();
		return m_Component.isStateValid();
	}
	
	protected boolean isValid(Data d){
		return m_DataClass.isAssignableFrom(d.getClass());
	}

	@Override
	public JComponent getComponent() {
		if(m_Component == null) this.initComponent();
		return m_Component;
	}

	@Override
	public void process(P params) {
		process(params, m_Component.getDataArgs());
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
		if(core.getDataManager().contains(arg)){
			Data d = core.getDataManager().getData(arg);
			if(validFunction.isValid(d) && dataClass.isAssignableFrom(d.getClass())){
				return dataClass.cast(d);
			}
		}
		DataReader reader = getDataReader(core, dataClass);
		l.update(new ProgressEvent(null, Type.UPDATE, "Reading "+dataClass.getSimpleName()+" from file "+arg));
		Data read = readFile(arg, reader, getFileFilters(reader));
		if(read == null){
			throw new RuntimeException("Unable to read data \""+dataClass.getName()+"\" from file \""+arg+"\"");
		}
		if(validFunction.isValid(read) && dataClass.isAssignableFrom(read.getClass())){
			l.update(new ProgressEvent(null, Type.COMPLETED, "Done reading "+dataClass.getSimpleName()+" from file "+arg));
			return dataClass.cast(read);
		}else{
			throw new RuntimeException("Data in file \""+arg+"\" is not valid");
		}
	}
	
	private static DataReader getDataReader(JProbeCore core, Class<? extends Data> dataClass){
		DataReader reader = core.getDataManager().getDataReader(dataClass);
		if(reader == null){
			throw new RuntimeException("No DataReader for class \""+dataClass.getName()+"\" found.");
		}
		return reader;
	}
	
	protected static FileFilter[] getFileFilters(DataReader reader){
		FileFilter[] formats = reader.getValidReadFormats();
		if(formats.length < 1){
			throw new RuntimeException("No valid read formats for data \""+reader.getReadClass().getName()+"\"");
		}
		return formats;
	}
	
	protected DataReader getDataReader(){
		return getDataReader(m_Core, m_DataClass);
	}
	
	protected static Data readFile(String file, DataReader reader, FileFilter[] formats){
		File f = new File(file);
		if(!f.canRead()){
			throw new RuntimeException("File \""+file+"\" not readable");
		}
		Data d = null;
		int filter = 0;
		while(d == null && filter<formats.length){
			try{
				d = reader.read(formats[filter], new FileInputStream(f));
			} catch(Exception e){
				//do nothing
			}
			++filter;
		}
		return d;
	}

	@Override
	public void update(ValidNotifier notifier, boolean valid) {
		this.notifyListeners();
	}


}
