package old.datatypes.bindingsite;

import old.datatypes.sequence.Sequence;

/**
 * This class represents two binding sites. This class is not designed to be used alone, but rather as part of a larger
 * class.
 * @author Tristan Bepler
 *
 */
public class TwoBindingSites extends AbstractNBindingSites{
	
	private String type;
	
	public TwoBindingSites(Sequence seq, String type, int startA, int endA, int startB, int endB){
		super(seq, new int[]{startA, startB}, new int[]{endA, endB});
		this.type = type;
	}
	
	public TwoBindingSites(Sequence seq, int startA, int endA, int startB, int endB){
		this(seq, "", startA, endA, startB, endB);
	}

	protected TwoBindingSites(Sequence seq, int[] starts, int[] ends) {
		super(seq, starts, ends);
	}

	@Override
	public String getName() {
		return "Bound2"+type;
	}

}
