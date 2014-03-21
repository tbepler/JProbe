package chiptools.jprobe.params;

import chiptools.jprobe.data.PeakSequences;
import jprobe.services.data.Data;
import jprobe.services.function.DataParameter;

public class PeakSeqsParam implements DataParameter{

	@Override
	public String getName() {
		return "PeakSeqs";
	}

	@Override
	public String getDescription() {
		return "Data containing peak sequences";
	}

	@Override
	public boolean isOptional() {
		return false;
	}

	@Override
	public Class<? extends Data> getType() {
		return PeakSequences.class;
	}

	@Override
	public boolean isValid(Data data) {
		return data instanceof PeakSequences;
	}

}
