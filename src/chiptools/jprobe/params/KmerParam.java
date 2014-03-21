package chiptools.jprobe.params;

import chiptools.jprobe.data.Kmer;
import jprobe.services.data.Data;
import jprobe.services.function.DataParameter;

public class KmerParam implements DataParameter{

	@Override
	public String getName() {
		return "Kmer";
	}

	@Override
	public String getDescription() {
		return "Kmer data";
	}

	@Override
	public boolean isOptional() {
		return false;
	}

	@Override
	public Class<? extends Data> getType() {
		return Kmer.class;
	}

	@Override
	public boolean isValid(Data data) {
		return data instanceof Kmer;
	}

}
