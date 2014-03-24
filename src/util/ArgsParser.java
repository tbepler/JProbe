package util;

public class ArgsParser {
	
	/**
	 * This class represents an argument that should be parsed from the command line.
	 * The tag is a regex that, when matched, represents the start of strings for
	 * this argument.
	 * 
	 * @author Tristan Bepler
	 *
	 */
	public static abstract class Argument{
		/**
		 * Returns a regex that marks the start of strings for this argument.
		 * @return
		 */
		public abstract String getTag();
		/**
		 * Parse strings for this argument. The args array will contain
		 * all the strings following the tag matched string up until the next
		 * tag is reached or the cmd line string array is empty.
		 * @param args
		 */
		public abstract void parse(String[] args);
	}
	
	/**
	 * Parse the given arguments from the cmd line array.
	 * @param cmdLine
	 * @param args
	 */
	public static void parse(String[] cmdLine, Argument[] args){
		for(int i=0; i<cmdLine.length; i++){
			String cur = cmdLine[i];
			//iterate over the args and check if cur matches any tags
			for(Argument arg : args){
				if(cur.matches(arg.getTag())){
					//find the index of the next tag or the end of the array if there is no next tag
					int end = i;
					boolean done = false;
					while(!done){
						if(++end == cmdLine.length) break;
						String endStr = cmdLine[end];
						//if endStr matches any arg tags, end found
						for(Argument a : args){
							if(endStr.matches(a.getTag())){
								done = true;
								break;
							}
						}
					}
					//create a subarray of cmdLine to pass to the arg for parsing
					String[] subarray = new String[end - i - 1];
					System.arraycopy(cmdLine, i+1, subarray, 0, subarray.length);
					arg.parse(subarray);
					//update i to end - 1 and break
					i = end - 1;
					break;
				}
			}
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
}
