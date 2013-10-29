package datatypes.probe;
import datatypes.Sequence;

public class Probe<S extends Sequence> implements Sequence{
	
	Sequence seq;
	
	
	
	public Probe(S seq){
		
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
	
}
	