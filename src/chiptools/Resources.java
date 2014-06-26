package chiptools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jprobe.services.ErrorHandler;
import jprobe.services.function.Argument;
import jprobe.services.function.Function;

@SuppressWarnings("rawtypes")
public class Resources {
	
	private static final String DELIM = "\t";
	
	private static final int FUNCTION_CLASS = 0;
	private static final int FUNCTION_ARGS_FILE = 1;
	private static final int FUNCTION_NAME = 2;
	private static final int FUNCTION_CATEGORY = 3;
	private static final int FUNCTION_DESCRIPTION = 4;
	private static final int NUM_FUNC_ENTRIES = 5;
	private static final Map<Class<? extends Function>, String[]> FUNCTION_MAP = new HashMap<Class<? extends Function>, String[]>();
	
	@SuppressWarnings("unchecked")
	private static void readFunctions(){
		BufferedReader reader = new BufferedReader(new InputStreamReader(Constants.class.getResourceAsStream(Constants.FUNCTIONS_FILE)));
		String line;
		try {
			while((line = reader.readLine()) != null){
				String[] tokens = line.split(DELIM);
				if(tokens.length == NUM_FUNC_ENTRIES){
					Class<?> clazz = Class.forName(tokens[FUNCTION_CLASS]);
					if(Function.class.isAssignableFrom(clazz)){
						FUNCTION_MAP.put((Class<? extends Function>) clazz , tokens);
					}
				}else if(!line.matches("^\\s*$")){ //if the line isn't empty or whitespace, report a warning
					ErrorHandler.getInstance().handleWarning(
							"Chiptools plugin could not read function resource line: \""+line+"\"",
							null
							);
				}
			}
			reader.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Collection<Class<? extends Function>> getFunctionClasses(){
		if(FUNCTION_MAP.isEmpty()){
			readFunctions();
		}
		return FUNCTION_MAP.keySet();
	}
	
	private static String[] getFunctionEntries(Class<? extends Function> clazz){
		if(FUNCTION_MAP.isEmpty()){
			readFunctions();
		}
		return FUNCTION_MAP.get(clazz);
	}
	
	public static String getFunctionName(Class<? extends Function> clazz){
		String[] entries = getFunctionEntries(clazz);
		if(entries != null){
			return entries[FUNCTION_NAME];
		}
		return null;
	}
	
	public static String getFunctionCategory(Class<? extends Function> clazz){
		String[] entries = getFunctionEntries(clazz);
		if(entries != null){
			return entries[FUNCTION_CATEGORY];
		}
		return null;
	}
	
	public static String getFunctionDescription(Class<? extends Function> clazz){
		String[] entries = getFunctionEntries(clazz);
		if(entries != null){
			return entries[FUNCTION_DESCRIPTION];
		}
		return null;
	}
	
	private static String getArgsFile(Class<? extends Function> clazz){
		String[] entries = getFunctionEntries(clazz);
		if(entries != null){
			return entries[FUNCTION_ARGS_FILE];
		}
		return null;
	}
	
	private static final int ARGUMENT_CLASS = 0;
	private static final int ARGUMENT_NAME = 1;
	private static final int ARGUMENT_FLAG = 2;
	private static final int ARGUMENT_PROTOTYPE = 3;
	private static final int ARGUMENT_CATEGORY = 4;
	private static final int ARGUMENT_DESCRIPTION = 5;
	private static final int NUM_ARG_ENTRIES = 6;
	private static final Map<Class<? extends Function>, Map<Class<? extends Argument>, String[]>> ARGS_MAP =
			new HashMap<Class<? extends Function>, Map<Class<? extends Argument>, String[]>>();
	
	@SuppressWarnings("unchecked")
	private static Map<Class<? extends Argument>, String[]> readArguments(String argsFile){
		Map<Class<? extends Argument>, String[]> map = new HashMap<Class<? extends Argument>, String[]>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(Constants.class.getResourceAsStream(argsFile)));
		String line;
		try {
			while((line = reader.readLine()) != null){
				String[] tokens = line.split(DELIM);
				if(tokens.length == NUM_ARG_ENTRIES){
					Class<?> clazz = Class.forName(tokens[ARGUMENT_CLASS]);
					if(Argument.class.isAssignableFrom(clazz)){
						map.put((Class<? extends Argument>) clazz , tokens);
					}
				}else if(!line.matches("^\\s*$")){ //if the line isn't empty or whitespace, report a warning
					ErrorHandler.getInstance().handleWarning(
							"Chiptools plugin could not read argument resource \""+argsFile+"\" line: \""+line+"\"",
							null
							);
				}
			}
			reader.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return map;
	}
	
	private static Map<Class<? extends Argument>, String[]> getArguments(Class<? extends Function> clazz){
		if(!ARGS_MAP.containsKey(clazz)){
			//read the arguments file for the function class and store it in the ARGS_MAP
			String argsFile = getArgsFile(clazz);
			if(argsFile != null){
				argsFile = Constants.RESOURCES_PATH + "/" + argsFile;
				Map<Class<? extends Argument>, String[]> argMap = readArguments(argsFile);
				ARGS_MAP.put(clazz, argMap);
				return argMap;
			}
		}
		return ARGS_MAP.get(clazz);
	}
	
	private static String[] getArgumentEntries(Class<? extends Function> funcClass, Class<? extends Argument> argClass){
		Map<Class<? extends Argument>, String[]> argMap = getArguments(funcClass);
		if(argMap != null){
			return argMap.get(argClass);
		}
		return null;
	}
	
	public static String getArgumentName(Class<? extends Function> funcClass, Class<? extends Argument> argClass){
		String[] entries = getArgumentEntries(funcClass, argClass);
		if(entries != null){
			return entries[ARGUMENT_NAME];
		}
		return null;
	}
	
	public static Character getArgumentFlag(Class<? extends Function> funcClass, Class<? extends Argument> argClass){
		String[] entries = getArgumentEntries(funcClass, argClass);
		if(entries != null && entries[ARGUMENT_FLAG].length() > 0){
			return entries[ARGUMENT_FLAG].charAt(0);
		}
		return null;
	}
	
	public static String getArgumentPrototype(Class<? extends Function> funcClass, Class<? extends Argument> argClass){
		String[] entries = getArgumentEntries(funcClass, argClass);
		if(entries != null){
			return entries[ARGUMENT_PROTOTYPE];
		}
		return null;
	}
	
	public static String getArgumentCategory(Class<? extends Function> funcClass, Class<? extends Argument> argClass){
		String[] entries = getArgumentEntries(funcClass, argClass);
		if(entries != null){
			return entries[ARGUMENT_CATEGORY];
		}
		return null;
	}
	
	public static String getArgumentDescription(Class<? extends Function> funcClass, Class<? extends Argument> argClass){
		String[] entries = getArgumentEntries(funcClass, argClass);
		if(entries != null){
			return entries[ARGUMENT_DESCRIPTION];
		}
		return null;
	}
	
}
