package chiptools.jprobe;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import chiptools.Constants;
import jprobe.services.function.FunctionPrototype;
import plugins.genome.services.GenomeFunction;

public class FunctionProvider {
	
	private final List<GenomeFunction> m_GenomeFunctions = this.generateGenomeFunctions();
	private final List<FunctionPrototype> m_FunctionPrototypes = this.generateFunctionPrototypes();
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
	
	private List<FunctionPrototype> generateFunctionPrototypes(){
		List<FunctionPrototype> list = new ArrayList<FunctionPrototype>();
		for(Class<? extends FunctionPrototype> clazz : Constants.FUNCTION_PROTOTYPE_CLASSES){
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
		for(FunctionPrototype prot : m_FunctionPrototypes){
			ServiceRegistration<?> reg = context.registerService(FunctionPrototype.class, prot, null);
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
