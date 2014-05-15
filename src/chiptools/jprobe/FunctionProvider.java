package chiptools.jprobe;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import chiptools.Constants;
import jprobe.services.function.Function;
import plugins.genome.services.GenomeFunction;

public class FunctionProvider {
	
	private final List<GenomeFunction> m_GenomeFunctions = this.generateGenomeFunctions();
	private final List<Function> m_Functions = this.generateFunctions();
	private final List<ServiceRegistration<?>> m_Regs = new ArrayList<ServiceRegistration<?>>();
	
	private List<GenomeFunction> generateGenomeFunctions(){
		List<GenomeFunction> list = new ArrayList<GenomeFunction>();
		for(Class<? extends GenomeFunction> clazz : Constants.GENOME_FUNCTION_CLASSES){
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
	
	private List<Function> generateFunctions(){
		List<Function> list = new ArrayList<Function>();
		for(Class<? extends Function> clazz : Constants.FUNCTION_CLASSES){
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
		for(GenomeFunction gf : m_GenomeFunctions){
			ServiceRegistration<?> reg = context.registerService(GenomeFunction.class, gf, null);
			m_Regs.add(reg);
		}
		for(Function prot : m_Functions){
			ServiceRegistration<?> reg = context.registerService(Function.class, prot, null);
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
