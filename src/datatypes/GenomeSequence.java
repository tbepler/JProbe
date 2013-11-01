package datatypes;

import datatypes.location.Location;
import datatypes.sequence.Sequence;

public class GenomeSequence implements Sequence, Location, DataType<GenomeSequence>{

	private Sequence seq;
	private Location loc;
	private int id;
	
	public GenomeSequence(Sequence seq, Location loc){
		this.seq = seq;
		this.loc = loc;
	}
	
	@Override
	public GenomeSequence parse(String s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String writeToString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChr() {
		return loc.getChr();
	}

	@Override
	public int getStart() {
		return loc.getStart();
	}

	@Override
	public int getEnd() {
		return loc.getEnd();
	}

	@Override
	public Location parseLocation(String l) {
		return loc.parseLocation(l);
	}

	@Override
	public String locationToString() {
		return loc.locationToString();
	}

	@Override
	public String getSeq() {
		return seq.getSeq();
	}

	@Override
	public int length() {
		return seq.length();
	}

	@Override
	public String getMutationFlag() {
		return seq.getMutationFlag();
	}

	@Override
	public String getOrientationFlag() {
		return seq.getOrientationFlag();
	}

	@Override
	public String seqToString() {
		return seq.seqToString();
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

}
