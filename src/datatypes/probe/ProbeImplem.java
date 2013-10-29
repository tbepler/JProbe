package datatypes.probe;

import datatypes.DNASequence;
import exceptions.IllegalStringException;

public class ProbeImplem extends DNASequence{
	
	private boolean wildtype;
	private boolean forward;
	private String chr;
	private int start;
	private int end;
	
	public ProbeImplem(String seq, boolean wildtype, boolean forward, String chromosome, int start, int end) throws IllegalStringException {
		super(seq);
		this.wildtype = wildtype;
		this.forward = forward;
		this.chr = chromosome;
		this.start = start;
		this.end = end;
	}
	
	public ProbeImplem(String seq, boolean wildtype, String chromosome, int start, int end) throws IllegalStringException {
		this(seq, wildtype, true, chromosome, start, end);
	}
	
	public ProbeImplem(String seq,  String chromosome, int start, int end) throws IllegalStringException {
		this(seq, true, chromosome, start, end);
	}
	
	public ProbeImplem(String seq, boolean wildtype, boolean forward) throws IllegalStringException {
		this(seq, wildtype, forward, "", 0, 0);
	}
	
	public ProbeImplem(String seq, boolean wildtype) throws IllegalStringException {
		this(seq, wildtype, true);
	}

	public ProbeImplem(String seq) throws IllegalStringException {
		this(seq, true);
	}

}
