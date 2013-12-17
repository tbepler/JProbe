package old.datatypes.bindingsite;

import old.datatypes.sequence.Sequence;

/**
 * This class represents single binding sites. This class is not designed to be used alone, but rather as part of a larger
 * class.
 * @author Tristan Bepler
 *
 */
public class OneBindingSite extends AbstractNBindingSites{
	
	public OneBindingSite(Sequence seq, int start, int end){
		super(seq, new int[]{start}, new int[]{end});
	}

	protected OneBindingSite(Sequence seq, int[] starts, int[] ends) {
		super(seq, starts, ends);
	}

	@Override
	public String getName() {
		return "Bound1";
	}

}
