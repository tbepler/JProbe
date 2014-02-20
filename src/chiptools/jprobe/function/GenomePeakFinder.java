package chiptools.jprobe.function;

import chiptools.Constants;
import chiptools.jprobe.data.PeakSequences;
import chiptools.jprobe.data.Peaks;
import jprobe.services.data.Data;
import jprobe.services.data.Field;
import jprobe.services.function.DataParameter;
import jprobe.services.function.FieldParameter;
import plugins.genome.services.GenomeFunction;
import util.genome.peak.PeakGroup;
import util.genome.peak.PeakSequenceGroup;
import util.genome.reader.GenomeReader;

public class GenomePeakFinder implements GenomeFunction{

	@Override
	public String getName() {
		return Constants.GENOME_PEAK_FINDER_NAME;
	}

	@Override
	public String getDescription() {
		return Constants.GENOME_PEAK_FINDER_TOOLTIP;
	}

	@Override
	public DataParameter[] getDataParameters() {
		return new DataParameter[]{
			new DataParameter(){

				@Override
				public String getName() {
					return Constants.PEAK_PARAM_NAME;
				}

				@Override
				public String getDescription() {
					return Constants.PEAK_PARAM_TOOLTIP;
				}

				@Override
				public boolean isOptional() {
					return false;
				}

				@Override
				public Class<? extends Data> getType() {
					return Peaks.class;
				}

				@Override
				public boolean isValid(Data data) {
					return data instanceof Peaks;
				}
				
			}
		};
	}

	@Override
	public FieldParameter[] getFieldParameters() {
		return new FieldParameter[]{};
	}

	@Override
	public Data run(GenomeReader genomeReader, Data[] dataArgs, Field[] fieldArgs) throws Exception {
		PeakGroup peaks = ((Peaks)dataArgs[0]).getPeaks();
		return new PeakSequences(PeakSequenceGroup.readFromGenome(genomeReader, peaks));
	}

}
