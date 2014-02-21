package jprobe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jprobe.services.AbstractServiceListener;
import jprobe.services.ErrorHandler;
import jprobe.services.command.Command;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class CommandManager extends AbstractServiceListener<Command>{
	
	private Map<String, List<Command>> m_Commands = new HashMap<String, List<Command>>();
	
	public CommandManager(BundleContext context) {
		super(Command.class, context);	
	}

	@Override
	public void register(Command cmd, Bundle provider) {
		String name = cmd.getName();
		if(m_Commands.containsKey(name)){
			ErrorHandler.getInstance().handleWarning("Warning: name collision on command \""+name+"\"", provider);
			m_Commands.get(name).add(cmd);
		}else{
			List<Command> list = new ArrayList<Command>();
			list.add(cmd);
			m_Commands.put(name, list);
		}
	}

	@Override
	public void unregister(Command cmd, Bundle provider) {
		
	}

	
	
	
}
