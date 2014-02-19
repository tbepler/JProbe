package util.genome;

public enum Strand {

	PLUS,
	MINUS,
	UNKNOWN;
	
	@Override
	public String toString(){
		switch(this){
		case PLUS:
			return "+";
		case MINUS:
			return "-";
		default:
			return ".";
		}
	}
	
	public static Strand parseStrand(String s){
		if(s.equals(PLUS.toString())) return PLUS;
		if(s.equals(MINUS.toString())) return MINUS;
		return UNKNOWN;
	}
	
	
}
