package datatypes;
import datatypes.bindingsite.BindingSite;
import datatypes.bindingsite.NBindingSites;
import datatypes.location.Location;
import datatypes.sequence.Sequence;

public class Probe implements Sequence, NBindingSites, Location{
	
	private Sequence seq;
	private NBindingSites bindingSites;
	private Location loc;
	
	private int id;
	
	public Probe(Sequence seq, NBindingSites bindingSites, Location loc, int id){
		this.seq = seq;
		this.bindingSites = bindingSites;
		this.loc = loc;
		this.id = id;
	}
	
	public Probe(Sequence seq, NBindingSites bindingSites, Location loc){
		this(seq, bindingSites, loc, 0);
	}
	
	@Override
	public String toString(){
		return getSeq()+"\t"+getName()+"";
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	@Override
	public String getSeq() {
		return seq.getSeq();
	}

	@Override
	public String seqToString() {
		return seq.seqToString();
	}

	@Override
	public int length() {
		return seq.length();
	}

	@Override
	public BindingSite[] getBindingSites() {
		return bindingSites.getBindingSites();
	}

	@Override
	public String getName() {
		return bindingSites.getName();
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
		return  loc.locationToString();
	}

	@Override
	public String getMutationFlag() {
		return seq.getMutationFlag();
	}

	@Override
	public String getOrientationFlag() {
		return seq.getOrientationFlag();
	}
	
}
	