package language.functions.core;

public class AddProcedure implements Procedure{

	@Override
	public int params() {
		return 2;
	}

	@Override
	public Object exec(Object... args) {
		assert(args.length == 2);
		if(args[0] instanceof Integer && args[1] instanceof Integer){
			return (Integer) args[0] + (Integer) args[1];
		}
		throw new RuntimeException("Unable to add "+args[0]+", "+args[1]);
	}

}
