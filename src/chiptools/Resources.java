package chiptools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jprobe.services.function.Argument;
import jprobe.services.function.Function;

public class Resources {
	
	private static final String DELIM = "\t";
	
	private static final int FUNCTION_CLASS = 0;
	private static final int FUNCTION_ARGS_FILE = 1;
	private static final int FUNCTION_NAME = 2;
	private static final int FUNCTION_CATEGORY = 3;
	private static final int FUNCTION_DESCRIPTION = 4;
	private static final Map<Class<? extends Function<?>>, String[]> FUNCTION_MAP = new HashMap<Class<? extends Function<?>>, String[]>();
	
	@SuppressWarnings("unchecked")
	private static void readFunctions(){
		BufferedReader reader = new BufferedReader(new InputStreamReader(Constants.class.getResourceAsStream(Constants.FUNCTIONS_FILE)));
		String line;
		try {
			while((line = reader.readLine()) != null){
				String[] tokens = line.split(DELIM);
				Class<?> clazz = Class.forName(tokens[FUNCTION_CLASS]);
				if(Function.class.isAssignableFrom(clazz)){
					FUNCTION_MAP.put((Class<? extends Function<?>>) clazz , tokens);
				}
			}
			reader.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Collection<Class<? extends Function<?>>> getFunctionClasses(){
		if(FUNCTION_MAP.isEmpty()){
			readFunctions();
		}
		return FUNCTION_MAP.keySet();
	}
	
	private static String[] getFunctionEntries(Class<? extends Function<?>> clazz){
		if(FUNCTION_MAP.isEmpty()){
			readFunctions();
		}
		return FUNCTION_MAP.get(clazz);
	}
	
	public static String getFunctionName(Class<? extends Function<?>> clazz){
		String[] entries = getFunctionEntries(clazz);
		if(entries != null){
			return entries[FUNCTION_NAME];
		}
		return null;
	}
	
	public static String getFunctionCategory(Class<? extends Function<?>> clazz){
		String[] entries = getFunctionEntries(clazz);
		if(entries != null){
			return entries[FUNCTION_CATEGORY];
		}
		return null;
	}
	
	public static String getFunctionDescription(Class<? extends Function<?>> clazz){
		String[] entries = getFunctionEntries(clazz);
		if(entries != null){
			return entries[FUNCTION_DESCRIPTION];
		}
		return null;
	}
	
	private static final int ARGUMENT_CLASS = 0;
	private static final int ARUMENT_NAME = 1;
	private static final int ARGUMENT_FLAG = 2;
	private static final int ARGUMENT_PROTOTYPE = 3;
	private static final int ARGUMENT_CATEGORY = 4;
	private static final int ARGUMENT_DESCRIPTION = 5;
	private static final Map<Class<? extends Function<?>>, Map<Class<? extends Argument<?>>, String[]>> ARGS_MAP =
			new HashMap<Class<? extends Function<?>>, Map<Class<? extends Argument<?>>, String[]>>();
	
	
	
	
}
