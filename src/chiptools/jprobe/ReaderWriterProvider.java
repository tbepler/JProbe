package chiptools.jprobe;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import chiptools.Constants;
import jprobe.services.data.DataReader;
import jprobe.services.data.DataWriter;

public class ReaderWriterProvider {
	
	private final List<DataReader> m_Readers = this.generateReaders();
	private final List<DataWriter> m_Writers = this.generateWriters();
	private final List<ServiceRegistration<?>> m_Regs = new ArrayList<ServiceRegistration<?>>();
	
	private List<DataReader> generateReaders(){
		List<DataReader> list = new ArrayList<DataReader>();
		for(Class<? extends DataReader> clazz : Constants.READER_CLASSES){
			try {
				list.add(clazz.newInstance());
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	
	private List<DataWriter> generateWriters(){
		List<DataWriter> list = new ArrayList<DataWriter>();
		for(Class<? extends DataWriter> clazz : Constants.WRITER_CLASSES){
			try {
				list.add(clazz.newInstance());
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public void start(BundleContext context){
		for(DataReader reader : m_Readers){
			ServiceRegistration<?> reg = context.registerService(DataReader.class, reader, null);
			m_Regs.add(reg);
		}
		for(DataWriter writer : m_Writers){
			ServiceRegistration<?> reg = context.registerService(DataWriter.class, writer, null);
			m_Regs.add(reg);
		}
	}
	
	public void stop(BundleContext context){
		for(ServiceRegistration<?> reg : m_Regs){
			reg.unregister();
		}
		m_Regs.clear();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}