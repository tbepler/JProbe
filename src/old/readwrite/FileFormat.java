package old.readwrite;

public enum FileFormat {
	
	BED("BED"), ENCODEPEAK("ENCODE peak"), XML("XML");
	
	
	private final String name;
	FileFormat(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
}
