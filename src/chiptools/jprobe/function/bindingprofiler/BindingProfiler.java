package chiptools.jprobe.function.bindingprofiler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jprobe.services.data.Data;
import jprobe.services.function.Argument;
import util.genome.Sequences;
import util.genome.Sequences.Profile;
import util.genome.probe.Probe;
import util.genome.probe.ProbeGroup;
import util.progress.ProgressEvent;
import util.progress.ProgressListener;
import util.progress.ProgressEvent.Type;
import chiptools.jprobe.data.BindingProfile;
import chiptools.jprobe.function.AbstractChiptoolsFunction;
import chiptools.jprobe.function.args.ProbesArgument;

public class BindingProfiler extends AbstractChiptoolsFunction<BindingProfileParams>{

	public BindingProfiler() {
		super(BindingProfileParams.class);
	}

	@Override
	public Collection<Argument<? super BindingProfileParams>> getArguments() {
		Collection<Argument<? super BindingProfileParams>> args = new ArrayList<Argument<? super BindingProfileParams>>();
		args.add(new ProbesArgument(this, false));
		args.add(new BindingKmerArgument(this, true));
		args.add(new BindingPWMArgument(this, true));
		return args;
	}
	
	protected int fireProgressUpdate(ProgressListener l, int progress, int maxProgress, int prevPercent){
		int percent = progress*100/maxProgress;
		if(percent != prevPercent){
			l.update(new ProgressEvent(this, Type.UPDATE, progress, maxProgress, "Profiling binding..."));
		}
		return percent;
	}

	@Override
	public Data execute(ProgressListener l, BindingProfileParams params) throws Exception {
		util.genome.kmer.Kmer[] kmers = new util.genome.kmer.Kmer[params.getKmers().size()];
		for(int i=0; i<kmers.length; i++){
			kmers[i] = params.getKmers().get(i).getKmer();
		}
		String[] kmerNames = params.KMER_NAMES.toArray(new String[params.KMER_NAMES.size()]);
		util.genome.pwm.PWM[] pwms = new util.genome.pwm.PWM[params.getPWMs().size()];
		for(int i=0; i<pwms.length; i++){
			pwms[i] = params.getPWMs().get(i).getPWM();
		}
		String[] pwmNames = params.PWM_NAMES.toArray(new String[params.PWM_NAMES.size()]);
		List<Profile> bindingProfiles = new ArrayList<Profile>();
		ProbeGroup group = params.getProbes().getProbeGroup();
		int percentComplete = this.fireProgressUpdate(l, 0, group.size(), -1);
		for(int i=0; i<group.size(); i++){
			Probe p = group.getProbe(i);
			bindingProfiles.add(Sequences.profile(p.getSequence(), p.getName(i+1), kmers, kmerNames, pwms, pwmNames));
			percentComplete = this.fireProgressUpdate(l, i+1, group.size(), percentComplete);
		}
		l.update(new ProgressEvent(this, Type.COMPLETED, "Done profiling binding."));
		return new BindingProfile(bindingProfiles);
	}

}
