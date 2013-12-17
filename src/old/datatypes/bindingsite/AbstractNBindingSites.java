package old.datatypes.bindingsite;

import old.datatypes.sequence.Sequence;

/**
 * Abstract class that implements the NBindingSites interface.
 * @author Tristan
 *
 */
public abstract class AbstractNBindingSites implements NBindingSites{
	
	private BindingSite[] sites;
	
	protected AbstractNBindingSites(Sequence seq, int[] starts, int[] ends){
		int count = Math.min(starts.length, ends.length);
		sites = new BindingSite[count];
		for(int i=0; i<count; i++){
			sites[i] = new BindingSite(seq, starts[i], ends[i]);
		}
	}
	
	@Override
	public BindingSite[] getBindingSites() {
		return sites;
	}

	@Override
	public abstract String getName();

}
