package core.datatypes;

import exceptions.IllegalStringException;

public class OnePeakProbe extends Probe{

	public OnePeakProbe(String seq, String chromosome, int start, int end) throws IllegalStringException {
		super(seq, chromosome, start, end);
		// TODO Auto-generated constructor stub
	}

	public OnePeakProbe(String seq, boolean wildtype, boolean forward,
			String chromosome, int start, int end)
			throws IllegalStringException {
		super(seq, wildtype, forward, chromosome, start, end);
		// TODO Auto-generated constructor stub
	}

	public OnePeakProbe(String seq, boolean wildtype, boolean forward)
			throws IllegalStringException {
		super(seq, wildtype, forward);
		// TODO Auto-generated constructor stub
	}

	public OnePeakProbe(String seq, boolean wildtype, String chromosome,
			int start, int end) throws IllegalStringException {
		super(seq, wildtype, chromosome, start, end);
		// TODO Auto-generated constructor stub
	}

	public OnePeakProbe(String seq, boolean wildtype)
			throws IllegalStringException {
		super(seq, wildtype);
		// TODO Auto-generated constructor stub
	}

	public OnePeakProbe(String seq) throws IllegalStringException {
		super(seq);
		// TODO Auto-generated constructor stub
	}

}
