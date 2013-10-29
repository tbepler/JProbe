package datatypes.location;


public class GenomeLocation implements Location{
	
	private String chr;
	private int start;
	private int end;
	
	public GenomeLocation(String chr, int start, int end){
		this.chr = chr;
		this.start = start;
		this.end = end;
	}
	
	@Override
	public String getChr() {
		return chr;
	}

	@Override
	public int getStart() {
		return start;
	}

	@Override
	public int getEnd() {
		return end;
	}

	@Override
	public Location parseLocation(String l) {
		return new GenomeLocation(l.substring(0, l.indexOf(':')),
								  Integer.parseInt(l.substring(l.indexOf(':')+1),l.indexOf('-')),
								  Integer.parseInt(l.substring(l.indexOf('-')+1)));
	}

	@Override
	public String locationToString() {
		return chr + ":" + start + "-" + end;
	}

}
