package jprobe.services.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jprobe.services.JProbeCore;

public abstract class AbstractCommand implements Command{
	
	public static interface Flag<P>{
		
		public static final String LONG_FORM_PREFIX = "--";
		public static final String SHORT_FORM_PREFIX = "-";
		
		/**
		 * Single character form of this flag, will be used with '-' in front
		 * on the command line
		 * @return
		 */
		public Character shortForm();
		
		/**
		 * String form of this flag, will be used with "--" in front on the
		 * command line
		 * @return
		 */
		public String longForm();
		
		/**
		 * String containing a description of this flag for the user
		 * @return
		 */
		public String description();
		
		/**
		 * A boolean specifying whether this flag takes additional arguments or not
		 * @return
		 */
		public boolean argsRequired();
		
		/**
		 * The default value to show the user if this flag takes additional arguments
		 * @return
		 */
		public String defaultValue();
		
		/**
		 * Parse parameters from args following this flag.
		 * @param param
		 * @param args
		 * @return
		 */
		public P parse(P param, String[] args);
	}
	
	public static abstract class AbstractFlag<P> implements Flag<P>{
		
		private final Character m_Short;
		private final String m_Long;
		private final String m_Desc;
		private final boolean m_ArgsReq;
		private final String m_DefaultVal;
		
		public AbstractFlag(Character shortForm, String longForm, String description, boolean argsReq, String defaultValue){
			m_Short = shortForm; m_Long = longForm; m_Desc = description; m_ArgsReq = argsReq; m_DefaultVal = defaultValue;
		}
		
		@Override
		public Character shortForm(){ return m_Short; }
		
		@Override
		public String longForm(){ return m_Long; }
		
		@Override
		public String description(){ return m_Desc; }
		
		@Override
		public boolean argsRequired(){ return m_ArgsReq; }
		
		@Override
		public String defaultValue(){ return m_DefaultVal; }
		
	}
	
	protected static abstract class CommandHelper<P>{
		private final Map<Character, Flag<P>> m_ShortFlag = new HashMap<Character, Flag<P>>();
		private final Map<String, Flag<P>> m_LongFlag = new HashMap<String, Flag<P>>();
		private final List<Flag<P>> m_Flags = new ArrayList<Flag<P>>();
		
		protected CommandHelper(Flag<P> ... argFlags){
			for(Flag<P> flag : argFlags){
				m_Flags.add(flag);
				if(flag.shortForm() != null)
					m_ShortFlag.put(flag.shortForm(), flag);
				if(flag.longForm() != null)
					m_LongFlag.put(flag.longForm(), flag);
			}
		}
		
		protected String generateUsage(){
			String s = "";
			for(Flag<?> flag : m_Flags){
				if(flag.shortForm() != null){
					s += Flag.SHORT_FORM_PREFIX + flag.shortForm();
					s += flag.argsRequired() ? " "+flag.defaultValue() : "" ;
				}
				s += "\t";
				if(flag.longForm() != null){
					s += Flag.LONG_FORM_PREFIX + flag.longForm();
					s += flag.argsRequired() ? " " + flag.defaultValue() : "" ;
				}
				s += "\t";
				s += flag.description() + "\n";
			}
			return s;
		}
		
		protected abstract String generalUsage();
		protected abstract P newEmptyParameter();
		protected abstract void execute(JProbeCore core, P param);
		
		private void execute(JProbeCore core, String[] args){
			P param = this.parse(args);
			this.execute(core, param);
		}
		
		private P parse(String[] args){
			P param = this.newEmptyParameter();
			for(int i=0; i<args.length; i++){
				Flag<P> flag = null;
				//parse argument
				if(args[i].startsWith(Flag.LONG_FORM_PREFIX)){
					String s = args[i].substring(Flag.LONG_FORM_PREFIX.length());
					flag = m_LongFlag.get(s);
				}else if(args[i].startsWith(Flag.SHORT_FORM_PREFIX)){
					char c = args[i].charAt(Flag.SHORT_FORM_PREFIX.length());
					flag = m_ShortFlag.get(c);
				}
				//check if argument was not recognized
				if(flag == null){
					throw new RuntimeException("No such argument: "+args[i]);
				}
				//find where next flag is set, to determine how many args should be passed to the flag parse method
				int end = i;
				while(++end<args.length){
					if(args[end].startsWith(Flag.LONG_FORM_PREFIX) || args[end].startsWith(Flag.SHORT_FORM_PREFIX))
						break;
				}
				String[] flagArgs = new String[end-i-1];
				System.arraycopy(args, i+1, flagArgs, 0, flagArgs.length);
				param = flag.parse(param, flagArgs);
				i = end;
			}
			return param;
		}
		
		
	}
	
	private final String m_Name;
	private final String m_Desc;
	private final CommandHelper<?> m_Helper;
	
	protected AbstractCommand(String name, String description){
		m_Name = name;
		m_Desc = description;
		m_Helper = this.generateCommandHelper();
	}
	
	protected abstract CommandHelper<?> generateCommandHelper();
	
	protected String getUsage(){
		String s = m_Helper.generalUsage() + "\n" + m_Helper.generateUsage();
		return s;
	}
	
	@Override
	public String getName() {
		return m_Name;
	}

	@Override
	public String getDescription() {
		return m_Desc;
	}

	@Override
	public void execute(JProbeCore core, String[] args) {
		m_Helper.execute(core, args);
	}

}
