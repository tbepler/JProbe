package jprobe;

public enum Debug {
	OFF,
	LOG,
	FULL;
	
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
