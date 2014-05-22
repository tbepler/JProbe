package chiptools.jprobe;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import chiptools.Constants;
import jprobe.services.function.Function;

public class FunctionProvider {
	
	private final List<Function> m_Functions = this.generateFunctions();
	private final List<ServiceRegistration<?>> m_Regs = new ArrayList<ServiceRegistration<?>>();
	
	private List<Function> generateFunctions(){
		List<Function> list = new ArrayList<Function>();
		for(Class<? extends Function> clazz : Constants.FUNCTION_CLASSES){
			try {
				list.add(clazz.newInstance());
			} catch (Exception e){
				throw new RuntimeException(e);
			}
		}
		return list;
	}
	
	public void start(BundleContext context){
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
