package chiptools.jprobe.function;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import util.genome.GenomicSequence;
import util.genome.peak.PeakSequence;
import util.genome.peak.PeakSequenceGroup;
import util.genome.probe.Probe;
import util.genome.probe.ProbeGroup;
import util.genome.probe.ProbeUtils;
import util.progress.ProgressListener;
import chiptools.Constants;
import chiptools.jprobe.data.Kmer;
import chiptools.jprobe.data.PWM;
import chiptools.jprobe.data.PeakSequences;
import chiptools.jprobe.data.Probes;
import chiptools.jprobe.field.DecimalField;
import chiptools.jprobe.field.IntField;
import chiptools.jprobe.params.FieldParam;
import chiptools.jprobe.params.KmerParam;
import chiptools.jprobe.params.PWMParam;
import chiptools.jprobe.params.PeakSeqsParam;
import jprobe.services.data.Data;
import jprobe.services.data.Field;
import jprobe.services.function.DataParameter;
import jprobe.services.function.FieldParameter;
import jprobe.services.function.Function;

public class ProbeGenerator implements Function{

	@Override
	public String getName() {
		return Constants.PROBE_GEN_FUNCTION_NAME;
	}

	@Override
	public String getDescription() {
		return Constants.PROBE_GEN_FUNCTION_TOOLTIP;
	}

	@Override
	public DataParameter[] getDataParameters() {
		return new DataParameter[]{
			new PeakSeqsParam(),
			new KmerParam(),
			new PWMParam()
		};
	}

	@Override
	public FieldParameter[] getFieldParameters() {
		return new FieldParameter[]{
			new FieldParam(new IntField(36, 1, Integer.MAX_VALUE, "Probe length"), "Probe Length", "Length of probes that will be created", false),
			new FieldParam(new IntField(9, 1, Integer.MAX_VALUE, "Binding site"), "Binding site", "Length of binding sites to be checked", false),
			new FieldParam(new IntField(3, 1, Integer.MAX_VALUE, "Window size"), "Window size", "Size of the window to be scanned with the PWM", false),
			new FieldParam(new DecimalField(0.3, -0.5, 0.5, "EScore"), "EScore", "Minimum escore that must span the binding site for a probe to be created", false)
		};
	}

	@Override
	public Data run(ProgressListener listener, Data[] dataArgs, Field[] fieldArgs) throws Exception {
		PeakSequenceGroup peakSeqs = ((PeakSequences) dataArgs[0]).getPeakSeqs();
		List<GenomicSequence> seqs = new ArrayList<GenomicSequence>();
		List<String> names = new ArrayList<String>();
		for(PeakSequence p : peakSeqs){
			seqs.add(p.getGenomicSequence());
			names.add(p.getName());
		}
		util.genome.kmer.Kmer kmer = ((Kmer) dataArgs[1]).getKmer();
		util.genome.pwm.PWM pwm = ((PWM) dataArgs[2]).getPWM();
		int probeLen = ((IntField) fieldArgs[0]).getValue();
		int bindingSite = ((IntField) fieldArgs[1]).getValue();
		int window = ((IntField) fieldArgs[2]).getValue();
		double escore = ((DecimalField) fieldArgs[3]).getValue();
		
		Queue<Probe> probes = new PriorityQueue<Probe>();
		for(int i=0; i<seqs.size(); i++){
			try{
				GenomicSequence seq = seqs.get(i);
				String name = names.get(i);
				probes.addAll(ProbeUtils.extractFrom(seq, name, kmer, pwm, probeLen, bindingSite, window, escore));
			} catch (Exception e){
				//proceed
			}
		}
		ProbeGroup group = new ProbeGroup(probes);
		return new Probes(group);
	}

}
