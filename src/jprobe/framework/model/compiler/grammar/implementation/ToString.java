package jprobe.framework.model.compiler.grammar.implementation;

public class ToString {
	
	public static final String DEFAULT_INDENT = "  ";
	
	public static String nestedToString(Object ... objs){
		return nestedToString(DEFAULT_INDENT, objs);
	}
	
	public static String nestedToString(String indent, Object ... objs){
		StringBuilder builder = new StringBuilder();
		for(Object o : objs){
			String s = o.toString();
			String[] lines = s.split("\n");
			for(String line : lines){
				builder.append(indent).append(line).append("\n");
			}
		}
		return builder.toString();
	}
	
}
