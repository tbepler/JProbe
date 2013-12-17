package old.datatypes.bindingsite;

import old.datatypes.sequence.Sequence;

/**
 * This class represents the unbound state. It is not designed to be used independently, but rather as part of another class.
 * @author Tristan Bepler
 *
 */
public class Unbound extends AbstractNBindingSites{
	
	public Unbound(Sequence seq){
		super(seq, new int[]{}, new int[]{});
	}

	protected Unbound(Sequence seq, int[] starts, int[] ends) {
		super(seq, starts, ends);
	}

	@Override
	public String getName() {
		return "Unbound";
	}

}
