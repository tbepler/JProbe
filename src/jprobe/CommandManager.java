package jprobe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import jprobe.services.AbstractServiceListener;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;
import jprobe.services.command.Command;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;

public class CommandManager extends AbstractServiceListener<Command>{
	
	private static final String INDEXED_CMD_REGEX = "^.+\\[\\d+\\]$";
	
	private Map<String, List<Command>> m_Commands = new HashMap<String, List<Command>>();
	
	public CommandManager(BundleContext context) {
		super(Command.class, context);
		try {
			for(ServiceReference<Command> ref : context.getServiceReferences(Command.class, null)){
				this.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, ref));
			}
		} catch (InvalidSyntaxException e) {
			//do nothing
		}
	}
	
	private void printHelpStatement(){
		Queue<String> names = new PriorityQueue<String>(m_Commands.keySet());
		System.out.println(Constants.NAME + " " + Constants.VERSION + "\nCreated by "+Constants.AUTHOR);
		System.out.println(Constants.HELP_MESSAGE);
		if(names.isEmpty()){
			System.out.println("None added");
		}
		while(!names.isEmpty()){
			String name = names.poll();
			List<Command> cmds = m_Commands.get(name);
			if(cmds.size() == 1){
				System.out.println("<"+name+">\t"+cmds.get(0).getDescription());
			}else{
				for(int i=0; i<cmds.size(); i++){
					System.out.println("<"+name+"["+i+"]>\t"+cmds.get(i).getDescription());
				}
			}
		}
		System.out.println("");
	}
	
	public void execute(JProbeCore core, String[] args){
		if(args.length == 0 || args[0].matches(Constants.HELP_REGEX)){
			this.printHelpStatement();
			System.exit(0);
		}
		String cmdName = args[0];
		Command cmd = this.getCommand(cmdName);
		if(cmd == null){
			System.err.println("Error: no command \""+cmdName+"\" found.");
			return;
		}
		String[] cmdArgs = new String[args.length - 1];
		System.arraycopy(args, 1, cmdArgs, 0, cmdArgs.length);
		cmd.execute(core, cmdArgs);
	}
	
	private void printNameCollisionStatement(List<Command> cmds){
		System.err.println("Warning: there is a naming collision on the command \""+cmds.get(0).getName()+"\"\n"
				+ "Please append [index] to the command name to select the desired command.");
		for(int i=0; i<cmds.size(); i++){
			System.err.println("["+i+"] "+cmds.get(i).getName() + " -");
			System.err.println(cmds.get(i).getDescription());
		}
	}
	
	protected Command getCommand(String name){
		if(!m_Commands.containsKey(name)){
			if(name.matches(INDEXED_CMD_REGEX)){
				try{
					int index = Integer.parseInt(name.substring(name.lastIndexOf('[')+1, name.lastIndexOf(']')));
					name = name.substring(0, name.lastIndexOf('['));
					return this.getCommand(name, index);
				} catch (Exception e){
					return null;
				}
			}
			return null;
		}
		List<Command> cmds = m_Commands.get(name);
		if(cmds.size() > 1){
			this.printNameCollisionStatement(cmds);
			return null;
		}
		return cmds.get(0);
		
	}
	
	protected Command getCommand(String name, int index){
		if(!m_Commands.containsKey(name)){
			return null;
		}
		List<Command> cmds = m_Commands.get(name);
		if(index < 0 || index >= cmds.size()){
			this.printNameCollisionStatement(cmds);
			return null;
		}
		return cmds.get(index);
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
		String name = cmd.getName();
		if(m_Commands.containsKey(name)){
			List<Command> list = m_Commands.get(name);
			list.remove(cmd);
			if(list.isEmpty()){
				m_Commands.remove(name);
			}
		}
	}

	
	
	
}
