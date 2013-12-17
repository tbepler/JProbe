package old.datatypes.location;

public interface Location {
	
	String getChr();
	int getStart();
	int getEnd();
	Location parseLocation(String l);
	String locationToString();
	
}
