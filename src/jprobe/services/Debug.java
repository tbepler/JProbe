package jprobe.services;

public enum Debug {
	OFF,
	LOG,
	FULL;
	
	private static volatile Debug debugLevel;
	
	public static void setLevel(Debug level){
		Debug.debugLevel = level;
	}
	
	public static Debug getLevel(){
		return debugLevel;
	}
	
	public static Debug fromString(String s){
		return fromInt(Integer.parseInt(s));
	}
	
	public static Debug fromInt(int i){
		if(i == 0){
			return OFF;
		}
		if(i == 1){
			return LOG;
		}
		if(i == 2){
			return FULL;
		}
		return LOG;
	}
	
	@Override
	public String toString(){
		switch(this){
		case OFF:
			return "0";
		case LOG:
			return "1";
		case FULL:
			return "2";
		default:
			return "1";
		}
	}
	
}
