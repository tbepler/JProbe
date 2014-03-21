package chiptools.jprobe.function;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import chiptools.Constants;
import chiptools.jprobe.data.Probes;
import util.genome.GenomicSequence;
import util.genome.kmer.Kmer;
import util.genome.probe.Probe;
import util.genome.probe.ProbeGroup;
import util.genome.probe.ProbeUtils;
import util.genome.pwm.PWM;
import util.progress.ProgressListener;
import jprobe.services.data.Data;
import jprobe.services.function.Function;

public class ProbeGenerator implements Function{
	
	public static final String NAME = "ProbeGen";
	public static final String DESCRIPTION = Constants.CMD_DESCRIPTIONS.containsKey(chiptools.jprobe.command.ProbeGenerator.class) ? 
			Constants.CMD_DESCRIPTIONS.get(chiptools.jprobe.command.ProbeGenerator.class) : 
				"Error finding description";
	
	private final List<GenomicSequence> m_Seqs;
	private final List<String> m_Names;
	private final Kmer m_Kmer;
	private final PWM m_Pwm;
	private final int m_ProbeLen;
	private final int m_BindingSite;
	private final int m_Window;
	private final double m_Escore;
	
	public ProbeGenerator(List<GenomicSequence> seqs, List<String> names, Kmer kmer, PWM pwm, int probelen, int bindingSite, int window, double eScore){
		m_Seqs = seqs;
		m_Names = names;
		m_Kmer = kmer;
		m_Pwm = pwm;
		m_ProbeLen = probelen;
		m_BindingSite = bindingSite;
		m_Window = window;
		m_Escore = eScore;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public boolean isProgressTrackable() {
		return false;
	}

	@Override
	public int getProgressLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addListener(ProgressListener listener) {
		//do nothing
	}

	@Override
	public void removeListener(ProgressListener listener) {
		//do nothing
	}

	@Override
	public Data run() throws Exception {
		Queue<Probe> probes = new PriorityQueue<Probe>();
		for(int i=0; i<m_Seqs.size(); i++){
			try{
				GenomicSequence seq = m_Seqs.get(i);
				String name = m_Names.get(i);
				probes.addAll(ProbeUtils.extractFrom(seq, name, m_Kmer, m_Pwm, m_ProbeLen, m_BindingSite, m_Window, m_Escore));
			} catch (Exception e){
				//proceed
			}
		}
		ProbeGroup group = new ProbeGroup(probes);
		return new Probes(group);
	}

}
