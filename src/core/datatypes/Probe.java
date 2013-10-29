package core.datatypes;

import exceptions.IllegalStringException;

public class Probe extends Sequence{
	
	private boolean wildtype;
	private boolean forward;
	private String chr;
	private int start;
	private int end;
	
	public Probe(String seq, boolean wildtype, boolean forward, String chromosome, int start, int end) throws IllegalStringException {
		super(seq);
		this.wildtype = wildtype;
		this.forward = forward;
		this.chr = chromosome;
		this.start = start;
		this.end = end;
	}
	
	public Probe(String seq, boolean wildtype, String chromosome, int start, int end) throws IllegalStringException {
		this(seq, wildtype, true, chromosome, start, end);
	}
	
	public Probe(String seq,  String chromosome, int start, int end) throws IllegalStringException {
		this(seq, true, chromosome, start, end);
	}
	
	public Probe(String seq, boolean wildtype, boolean forward) throws IllegalStringException {
		this(seq, wildtype, forward, "", 0, 0);
	}
	
	public Probe(String seq, boolean wildtype) throws IllegalStringException {
		this(seq, wildtype, true);
	}

	public Probe(String seq) throws IllegalStringException {
		this(seq, true);
	}

}
