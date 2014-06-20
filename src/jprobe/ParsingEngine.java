package jprobe;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

import org.osgi.framework.Bundle;

import util.progress.PrintStreamProgressBar;
import util.progress.ProgressEvent;
import util.progress.ProgressListener;
import jprobe.services.ErrorHandler;
import jprobe.services.FunctionManager;
import jprobe.services.data.Data;
import jprobe.services.function.Argument;
import jprobe.services.function.Function;

public class ParsingEngine {
	
	public static Data parseAndExecute(PrintStream report, FunctionManager funcManager, String[] args){
		if(args.length == 0 || args[0].matches(Constants.HELP_REGEX)){
			printHelpStatement(report, funcManager);
			return null;
		}
		String name = args[0];
		Function<?> func = getFunction(funcManager, name);
		if(func == null){
			report.println("No function "+name+" found.");
			printHelpStatement(report, funcManager);
			return null;
		}
		String[] funcArgs = new String[args.length - 1];
		System.arraycopy(args, 1, funcArgs, 0, funcArgs.length);
		
		return execute(report, funcManager.getProvider(func), func, funcArgs);
		
	}
	
	private static <T> Data execute(final PrintStream report, Bundle bundle, Function<T> func, String[] args){
		
		//check for help flag
		for(String s : args){
			if(s.matches(Constants.HELP_REGEX)){
				report.println(getFunctionUsage(func));
				return null;
			}
		}
		//organize argument flags
		Map<String, Argument<? super T>> flags = new HashMap<String, Argument<? super T>>();
		Collection<Argument<? super T>> requiredArgs = new HashSet<Argument<? super T>>();
		for(Argument<? super T> arg : func.getArguments()){
			if(!arg.isOptional()){
				requiredArgs.add(arg);
			}
			Character shortFlag = arg.shortFlag();
			String longFlag = arg.getName();
			if(shortFlag != null){
				flags.put(Constants.SHORT_FLAG_PREFIX + shortFlag, arg);
			}
			if(longFlag != null){
				flags.put(Constants.LONG_FLAG_PREFIX + longFlag, arg);
			}
		}
		
		//wrap a PrintStreamProgressBar around the PrintStream and use it to report progress
		final PrintStreamProgressBar progressBar = new PrintStreamProgressBar(report);
		ProgressListener l = new ProgressListener(){
			@Override
			public void update(ProgressEvent event) {
				progressBar.update(event);
			}
		};
		
		try {
			return execute(func, l, flags, requiredArgs, args);
		} catch (Exception e) {
			ErrorHandler.getInstance().handleException(e, bundle);
			report.println(getFunctionUsage(func));
			return null;
		}
	}
	
	private static <T> Data execute(
			Function<T> func,
			ProgressListener l,
			Map<String, Argument<? super T>> flags,
			Collection<Argument<? super T>> required,
			String[] args
			) throws Exception{
		
		T params = parse(l, func.newParameters(), flags, required, args);
		return func.execute(l, params);
	}
	
	private static <T> T parse(ProgressListener l, T params, Map<String, Argument<? super T>> flags, Collection<Argument<? super T>> required, String[] args){
		for(int i=0; i<args.length; i++){
			String s = args[i];
			if(!flags.containsKey(s)){
				throw new RuntimeException("unknown flag \""+s+"\".");
			}
			Argument<? super T> arg = flags.get(s);
			//find next flag
			int nextFlag = i;
			while(++nextFlag < args.length){
				if(flags.containsKey(args[nextFlag])){
					break;
				}
			}
			//get args to be parsed by the current Argument
			String[] curArgs = new String[nextFlag-i-1];
			System.arraycopy(args, i+1, curArgs, 0, curArgs.length);
			
			try{
				arg.parse(l, params, curArgs);
			}catch(Exception e){
				throw new RuntimeException(e.getMessage());
			}
			required.remove(arg);
			
			i = nextFlag - 1;
		}
		
		//ensure all required args have been parsed
		if(!required.isEmpty()){
			String message = "missing required args:";
			for(Argument<? super T> arg : required){
				message += " " + Constants.LONG_FLAG_PREFIX + arg.getName();
			}
			throw new RuntimeException(message);
		}
		
		return params;
	}
	
	private static <T> String getFunctionUsage(Function<T> func){
		String usage = func.getName() + " --- " + func.getDescription() + "\n";
		//append general usage statement
		usage += "Usage: " + func.getName() + " [" + Constants.SHORT_FLAG_PREFIX + Constants.HELP_SHORT_FLAG+"]";
		Collection<Argument<? super T>> args = func.getArguments();
		//required args first
		for(Argument<? super T> arg : args){
			if(!arg.isOptional()){
				usage += " " + shortUsage(arg);
			}
		}
		//now optional args
		for(Argument<? super T> arg : args){
			if(arg.isOptional()){
				usage += " [" + shortUsage(arg) + "]";
			}
		}
		usage += "\n\n";
		//append detailed arg usage, required first
		usage += "Required:\n";
		for(Argument<? super T> arg : args){
			if(!arg.isOptional()){
				usage += detailedUsage(arg) + "\n";
			}
		}
		usage += "\n" + "Optional:\n";
		for(Argument<? super T> arg : args){
			if(arg.isOptional()){
				usage += detailedUsage(arg) + "\n";
			}
		}
		//append help usage
		usage += Constants.SHORT_FLAG_PREFIX + Constants.HELP_SHORT_FLAG;
		usage += "\t" + Constants.LONG_FLAG_PREFIX + Constants.HELP_LONG_FLAG;
		usage += "\t" + "Display this help message";
		usage += "\n";
		
		return usage;
	}
	
	private static String detailedUsage(Argument<?> arg){
		String usage = "";
		String prototype = arg.getPrototypeValue();
		Character shortFlag = arg.shortFlag();
		if(shortFlag != null){
			usage += Constants.SHORT_FLAG_PREFIX + shortFlag;
			if(prototype != null){
				usage += " " + prototype;
			}
		}
		usage += "\t";
		String longFlag = arg.getName();
		if(longFlag != null){
			usage += Constants.LONG_FLAG_PREFIX + longFlag;
			if(prototype != null){
				usage += " " + prototype;
			}
		}
		usage += "\t" + arg.getTooltip();
		
		return usage;
	}
	
	private static String shortUsage(Argument<?> arg){
		String usage = "";
		if(arg.shortFlag() != null){
			usage += Constants.SHORT_FLAG_PREFIX + arg.shortFlag();
		}else{
			usage += Constants.LONG_FLAG_PREFIX + arg.getName();
		}
		String prototype = arg.getPrototypeValue();
		if(prototype != null){
			usage += " " + prototype;
		}
		return usage;
	}
	
	private static Function<?> getFunction(FunctionManager mngr, String name){
		Function<?>[] funcs = mngr.getFunctions(name);
		if(funcs.length < 1){
			if(name.matches(Constants.INDEXED_FUNC_REGEX)){
				try{
					int index = Integer.parseInt(name.substring(name.lastIndexOf('[')+1, name.lastIndexOf(']')));
					name = name.substring(0, name.lastIndexOf('['));
					return getFunction(mngr, name, index);
				} catch (Exception e){
					return null;
				}
			}
			return null;
		}
		if(funcs.length > 1){
			return null;
		}
		return funcs[0];
		
	}
	
	private static Function<?> getFunction(FunctionManager mngr, String name, int index){
		Function<?>[] funcs = mngr.getFunctions(name);
		if(funcs.length < 1){
			return null;
		}
		if(index < 0 || index >= funcs.length){
			return null;
		}
		return funcs[index];
	}
	
	private static void printHelpStatement(PrintStream out, FunctionManager mngr){
		Map<String, Collection<Function<?>>> categories = groupByCategory(mngr.getAllFunctions());
		
		out.println(Constants.NAME + " " + Constants.VERSION + "\nCreated by "+Constants.AUTHOR);
		out.println(Constants.HELP_MESSAGE);
		if(categories.isEmpty()){
			out.println("None added");
		}
		
		out.println("");
		printFunctions(out, categories);
		
	}
	
	private static void printFunctions(PrintStream out, Map<String, Collection<Function<?>>> categories){
		for(String cat : categories.keySet()){
			out.println(cat + ":");
			for(Function<?> func : categories.get(cat)){
				out.println("<"+func.getName()+">\t"+func.getDescription());
			}
			out.println("");
		}
	}
	
	private static Map<String, Collection<Function<?>>> groupByCategory(Function<?>[] funcs){
		Map<String, Collection<Function<?>>> categories = new TreeMap<String, Collection<Function<?>>>();
		for(Function<?> func : funcs){
			String cat = func.getCategory();
			if(categories.containsKey(cat)){
				categories.get(cat).add(func);
			}else{
				Collection<Function<?>> group = new PriorityQueue<Function<?>>(10, new Comparator<Function<?>>(){

					@Override
					public int compare(Function<?> arg0, Function<?> arg1) {
						return arg0.getName().compareTo(arg1.getName());
					}
					
				});
				group.add(func);
				categories.put(cat, group);
			}
		}
		return categories;
	}
	
}
