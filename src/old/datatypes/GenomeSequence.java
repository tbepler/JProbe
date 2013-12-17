package old.datatypes;

import old.datatypes.location.Location;
import old.datatypes.sequence.Sequence;

public class GenomeSequence implements Sequence, Location, DataType{

	private Sequence seq;
	private Location loc;
	private int id;
	
	public GenomeSequence(Sequence seq, Location loc){
		this.seq = seq;
		this.loc = loc;
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

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
