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
import jprobe.services.function.components.ValidListener;
import jprobe.services.function.components.ValidNotifier;

public abstract class DataArgument<P,D extends Data> extends AbstractArgument<P> implements ValidListener {
	
	private static String generatePrototype(int min, int max){
		if(min == 1 && max == 1){
			return "FILE";
		}
		if(max == Integer.MAX_VALUE){
			return "FILES{"+min+"+}";
		}
		return "FILES{"+min+"-"+max+"}";
	}
	
	private final JProbeCore m_Core;
	private final int m_Min;
	private final int m_Max;
	private final DataArgsComponent<D> m_Component;
	private final Class<? extends Data> m_DataClass;
	
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
		super(name, tooltip, category, shortFlag, generatePrototype(minArgs, maxArgs), optional);
		m_DataClass = dataClass;
		m_Core = core;
		m_Min = minArgs;
		m_Max = maxArgs;
		m_Component = new DataArgsComponent<D>(
				core,
				minArgs,
				maxArgs,
				allowDuplicates,
				dataClass,
				new DataArgsComponent.DataValidFunction() { @Override public boolean isValid(Data d) { return DataArgument.this.isValid(d); } }
				);
		m_Component.addListener(this);
	}
		
	protected abstract void process(P params, List<D> data);

	@Override
	public boolean isValid() { return m_Component.isStateValid(); }
	
	protected boolean isValid(Data d){
		return m_DataClass.isAssignableFrom(d.getClass());
	}

	@Override
	public JComponent getComponent() {
		return m_Component;
	}

	@Override
	public void process(P params) {
		process(params, m_Component.getDataArgs());
	}
	
	@SuppressWarnings("unchecked")
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
			if(m_Core.getDataManager().contains(arg) && isValid(m_Core.getDataManager().getData(arg))){
				data.add(i, (D) m_Core.getDataManager().getData(arg));
			}else{
				DataReader reader = this.getDataReader();
				l.update(new ProgressEvent(this, Type.UPDATE, "Reading "+m_DataClass.getSimpleName()+" from file "+arg));
				Data read = this.readFile(arg, reader, this.getFileFilters(reader));
				if(read == null){
					throw new RuntimeException("Unable to read data \""+m_DataClass.getName()+"\" from file \""+arg+"\"");
				}
				if(isValid(read)){
					data.add(i, (D) read);
				}else{
					throw new RuntimeException("Data in file \""+arg+"\" is not valid");
				}
			}
		}
		
		this.process(params, data);
		
	}
	
	protected FileFilter[] getFileFilters(DataReader reader){
		FileFilter[] formats = reader.getValidReadFormats();
		if(formats.length < 1){
			throw new RuntimeException("No valid read formats for data \""+m_DataClass.getName()+"\"");
		}
		return formats;
	}
	
	protected DataReader getDataReader(){
		DataReader reader = m_Core.getDataManager().getDataReader(m_DataClass);
		if(reader == null){
			throw new RuntimeException("No DataReader for class \""+m_DataClass.getName()+"\" found.");
		}
		return reader;
	}
	
	protected Data readFile(String file, DataReader reader, FileFilter[] formats){
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
