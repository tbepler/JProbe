package old.datatypes.bindingsite;

import old.datatypes.sequence.Sequence;

public class UnknownBindingSites extends AbstractNBindingSites{

	private String name;
	
	public UnknownBindingSites(Sequence seq, String name){
		this(seq, new int[]{}, new int[]{});
		this.name = name;
	}
	
	protected UnknownBindingSites(Sequence seq, int[] starts, int[] ends) {
		super(seq, starts, ends);
	}

	@Override
	public String getName() {
		return name;
	}

}
