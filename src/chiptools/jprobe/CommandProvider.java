package chiptools.jprobe;

import java.util.ArrayList;
import java.util.List;

import jprobe.services.command.Command;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import chiptools.Constants;

public class CommandProvider {
	
	private final List<Command> m_Cmds = this.generateCommands();
	private final List<ServiceRegistration<Command>> m_Regs = new ArrayList<ServiceRegistration<Command>>();
	
	private List<Command> generateCommands(){
		List<Command> list = new ArrayList<Command>();
		for(Class<? extends Command> clazz : Constants.CMD_CLASSES){
			try {
				list.add(clazz.newInstance());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public void start(BundleContext context){
		for(Command cmd : m_Cmds){
			ServiceRegistration<Command> reg = context.registerService(Command.class, cmd, null);
			m_Regs.add(reg);
		}
	}
	
	public void stop(BundleContext context){
		for(ServiceRegistration<Command> reg : m_Regs){
			reg.unregister();
		}
		m_Regs.clear();
	}
	
}
