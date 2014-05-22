package jprobe;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

import jprobe.services.FunctionManager;
import jprobe.services.data.Data;
import jprobe.services.function.Function;

public class ParsingEngine {

	private static final String INDEXED_FUNC_REGEX = "^.+\\[\\d+\\]$";
	
	public Data parse(PrintStream out, FunctionManager funcManager, String[] args){
		if(args.length == 0 || args[0].equals(Constants.ARG_HELP)){
			printHelpStatement(out, funcManager);
			return null;
		}
		String name = args[0];
		Function<?> func = getFunction(funcManager, name);
		if(func == null){
			out.println("No function "+name+" found.");
			printHelpStatement(out, funcManager);
			return null;
		}
		String[] funcArgs = new String[args.length - 1];
		System.arraycopy(args, 1, funcArgs, 0, funcArgs.length);
		
		return parse(func, funcArgs);
		
	}
	
	private Data parse(Function<?> func, String[] args){
		
		
		return null;
	}
	
	private Function<?> getFunction(FunctionManager mngr, String name){
		Function<?>[] funcs = mngr.getFunctions(name);
		if(funcs.length < 1){
			if(name.matches(INDEXED_FUNC_REGEX)){
				try{
					int index = Integer.parseInt(name.substring(name.lastIndexOf('[')+1, name.lastIndexOf(']')));
					name = name.substring(0, name.lastIndexOf('['));
					return this.getFunction(mngr, name, index);
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
	
	private Function<?> getFunction(FunctionManager mngr, String name, int index){
		Function<?>[] funcs = mngr.getFunctions(name);
		if(funcs.length < 1){
			return null;
		}
		if(index < 0 || index >= funcs.length){
			return null;
		}
		return funcs[index];
	}
	
	private void printHelpStatement(PrintStream out, FunctionManager mngr){
		Map<String, Collection<Function<?>>> categories = groupByCategory(mngr.getAllFunctions());
		
		out.println(Constants.NAME + " " + Constants.VERSION + "\nCreated by "+Constants.AUTHOR);
		out.println(Constants.HELP_MESSAGE);
		if(categories.isEmpty()){
			out.println("None added");
		}
		
		out.println("");
		printFunctions(out, categories);
		
	}
	
	private void printFunctions(PrintStream out, Map<String, Collection<Function<?>>> categories){
		for(String cat : categories.keySet()){
			out.println(cat + ":");
			for(Function<?> func : categories.get(cat)){
				out.println("<"+func.getName()+">\t"+func.getDescription());
			}
			out.println("");
		}
	}
	
	private Map<String, Collection<Function<?>>> groupByCategory(Function<?>[] funcs){
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
	
	private Collection<String> getDuplicateNames(Function<?>[] funcs){
		Collection<String> dups = new HashSet<String>();
		Collection<String> seen = new HashSet<String>();
		for(Function<?> func : funcs){
			if(seen.contains(func.getName())){
				dups.add(func.getName());
			}else{
				seen.add(func.getName());
			}
		}
		return dups;
	}
	
}
