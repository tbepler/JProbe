package chiptools.jprobe.data;

import jprobe.services.data.Data;

public class MutationProfileWriter extends GenericTableWriter{
	
	@Override
	public Class<? extends Data> getWriteClass() {
		return MutationProfile.class;
	}
	
}
