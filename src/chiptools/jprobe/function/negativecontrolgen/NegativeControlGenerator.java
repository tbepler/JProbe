package chiptools.jprobe.function.negativecontrolgen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import jprobe.services.data.Data;
import jprobe.services.function.Argument;
import util.genome.Chromosome;
import util.genome.GenomicCoordinate;
import util.genome.GenomicSequence;
import util.genome.Strand;
import util.genome.kmer.Kmer;
import util.genome.kmer.Kmers;
import util.genome.peak.Peak;
import util.genome.peak.PeakGroup;
import util.genome.peak.PeakSequence;
import util.genome.peak.PeakSequenceGroup;
import util.genome.peak.PeakUtils;
import util.genome.peak.PeakUtils.Filter;
import util.genome.probe.Probe;
import util.genome.probe.ProbeGroup;
import util.genome.reader.GenomeReader;
import util.genome.reader.GenomeReaderFactory;
import util.progress.ProgressEvent;
import util.progress.ProgressListener;
import util.progress.ProgressEvent.Type;
import chiptools.Constants;
import chiptools.jprobe.data.Peaks;
import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.AbstractChiptoolsFunction;
import chiptools.jprobe.function.args.EscoreArgument;
import chiptools.jprobe.function.args.GenomeArgument;
import chiptools.jprobe.function.args.ProbeLengthArgument;
import chiptools.jprobe.function.args.SummitArgument;

public class NegativeControlGenerator extends AbstractChiptoolsFunction<NegControlParams>{

	public NegativeControlGenerator() {
		super(NegControlParams.class);
	}

	@Override
	public Collection<Argument<? super NegControlParams>> getArguments() {
		Collection<Argument<? super NegControlParams>> args = new ArrayList<Argument<? super NegControlParams>>();
		args.add(new GenomeArgument(this, false));
		args.add(new IncludePeaksArg(this, false));
		args.add(new ExcludePeaksArg(this, true));
		args.add(new SummitArgument(this, true));
		args.add(new KmerListArgument(this, true));
		args.add(new EscoreArgument(this, true, 0.3));
		args.add(new ProbeLengthArgument(this, true));
		args.add(new NumberArg(this, true));
		
		return args;
	}

	@Override
	public Data execute(ProgressListener l, NegControlParams params) throws Exception {
		//get peaks and center peaks around the summit if necessary
		PeakGroup include = merge(toPeakGroup(params.getIncludePeaks(), params.getSummit()));
		final Map<Chromosome, List<Peak>> exclude = buildPeakMap(params.getExcludePeaks(), params.getSummit());
		//filter the include PeakGroup such that no peaks overlap with peaks in the exlude group
		l.update(new ProgressEvent(this, Type.UPDATE, "Filtering regions..."));
		include = PeakUtils.filter(include, new Filter(){

			@Override
			public boolean keep(Peak p) {
				if(exclude.containsKey(p.getChrom())){
					for(Peak ex : exclude.get(p.getChrom())){
						if(ex.getRegion().overlaps(p.getRegion())){
							return false;
						}
					}
				}
				return true;
			}
			
		});
		l.update(new ProgressEvent(this, Type.COMPLETED, "Done filtering regions."));
		
		//get genome reader for the genome file
		l.update(new ProgressEvent(this, Type.UPDATE, "Extracting genomic sequences..."));
		GenomeReader reader = GenomeReaderFactory.createGenomeReader(params.getGenomeFile(), l);
		PeakSequenceGroup peakSeqs = PeakSequenceGroup.readFromGenome(reader, include);
		l.update(new ProgressEvent(this, Type.COMPLETED, "Done extracting sequences."));
		
		Kmer kmer;
		if(params.getKmers().size() > 1){
			l.update(new ProgressEvent(this, Type.UPDATE, "Combining kmers..."));
			kmer = Kmers.combine(toUtilKmer(params.getKmers()));
			l.update(new ProgressEvent(this, Type.COMPLETED, "Done combining kmers."));
		}else if(params.getKmers().size() > 0){
			kmer = toUtilKmer(params.getKmers()).get(0);
		}else{
			kmer = null;
		}
		
		//generate probes
		ProbeGroup probes = generateProbes(
				l,
				peakSeqs,
				kmer,
				params.getEscore(),
				params.getProbeLength(),
				params.getNumPeaks()
				);
		l.update(new ProgressEvent(this, Type.COMPLETED, "Done generating probes."));
		
		return new Probes(probes);
		
	}
	
	private static ProbeGroup generateProbes(ProgressListener l, PeakSequenceGroup peakSeqs, Kmer kmer, double escore, int length){
		List<Probe> probes = new ArrayList<Probe>();
		int count = 0;
		int percentProgress = fireProbeGenerationEvent(l, count, peakSeqs.size(), -1);
		for(PeakSequence peakSeq : peakSeqs){
			GenomicCoordinate start = peakSeq.getStart();
			//iterate over every subregion of the peak sequence of size = length
			while(start.getBaseIndex() + length - 1 <= peakSeq.getRegion().getEnd().getBaseIndex()){
				String name = peakSeq.getName() + "_" + Constants.NEG_CTRL_PROBE_NAME + (probes.size()+1);
				Probe p = createProbe(
						peakSeq.getGenomicSequence(),
						start,
						length,
						name,
						kmer,
						escore
						);
				if(p != null){
					probes.add(p);
					//System.err.println("Probe count = "+probes.size());
					start = start.increment(length);
				}else{
					//increment by length/2 for better efficiency
					start = start.increment(length/2);
				}
			}
			percentProgress = fireProbeGenerationEvent(l, ++count, peakSeqs.size(), percentProgress);
		}
		return new ProbeGroup(probes);
	}
	
	private static ProbeGroup generateProbes(ProgressListener l, PeakSequenceGroup peakSeqs, Kmer kmer, double escore, int length, int num){
		if(num < 0){
			return generateProbes(l, peakSeqs, kmer, escore, length);
		}
		int percentProgress = fireProbeGenerationEvent(l, 0, num, -1);
		List<Probe> probes = new ArrayList<Probe>();
		Random r = new Random();
		Map<PeakSequence, GenomicCoordinate> lastCoord = new HashMap<PeakSequence, GenomicCoordinate>();
		Set<PeakSequence> fullySearched = new HashSet<PeakSequence>();
		//build tree map of chromosome to peak sequences in order to access peak sequences one each chromosome sequentially
		TreeMap<Chromosome, List<PeakSequence>> seqMap = new TreeMap<Chromosome, List<PeakSequence>>();
		for(PeakSequence s : peakSeqs){
			if(seqMap.containsKey(s.getChrom())){
				seqMap.get(s.getChrom()).add(s);
			}else{
				List<PeakSequence> list = new ArrayList<PeakSequence>();
				list.add(s);
				seqMap.put(s.getChrom(), list);
			}
		}
		//create a mapping of chromosome to the number of sequences remaining
		//this is used to ensure that fully searched sequences are not randomly selected again
		Map<Chromosome, Integer> seqsRemaining = new HashMap<Chromosome, Integer>();
		for(Entry<Chromosome, List<PeakSequence>> entry : seqMap.entrySet()){
			seqsRemaining.put(entry.getKey(), entry.getValue().size());
		}
		//initialize the chromosome and peak sequences to the first chromosome
		Chromosome curChrom = seqMap.firstKey();
		List<PeakSequence> curSeqs = seqMap.firstEntry().getValue();
		while(probes.size() < num && fullySearched.size() < peakSeqs.size()){
			//check if there are sequences remaining to be queried on this chromosome
			if(seqsRemaining.get(curChrom) > 0){
				processSequence(
						probes,
						kmer,
						escore,
						length,
						lastCoord,
						fullySearched,
						r,
						seqsRemaining,
						curChrom,
						curSeqs
						);
			}
			Entry<Chromosome, List<PeakSequence>> nextEntry;
			if(curChrom.equals(seqMap.lastKey())){
				nextEntry = seqMap.firstEntry();
			}else{
				nextEntry = seqMap.higherEntry(curChrom);
			}
			curSeqs = nextEntry.getValue();
			curChrom = nextEntry.getKey();
			percentProgress = fireProbeGenerationEvent(l, probes.size(), num, percentProgress);
		}
		
		return new ProbeGroup(probes);
	}
	
	
	private static int fireProbeGenerationEvent(ProgressListener l, int progress, int maxProgress, int prevProgress){
		if(progress * 100 / maxProgress != prevProgress){
			l.update(new ProgressEvent(null, Type.UPDATE, progress, maxProgress, "Generating probes..."));
			return progress*100/maxProgress;
		}
		return prevProgress;
	}

	private static void processSequence(
			List<Probe> probes,
			Kmer kmer,
			double escore,
			int length,
			Map<PeakSequence, GenomicCoordinate> lastCoord,
			Set<PeakSequence> fullySearched,
			Random r,
			Map<Chromosome, Integer> seqsRemaining,
			Chromosome curChrom,
			List<PeakSequence> curSeqs
			) {
		
		int numSeqs = seqsRemaining.get(curChrom);
		//pick a peak sequence randomly from the currently selected sequences
		int peakIndex = r.nextInt(numSeqs);
		PeakSequence peakSeq = curSeqs.get(peakIndex);
		//check that this sequence hasn't been fully searched already
		if(!fullySearched.contains(peakSeq)){
			GenomicSequence seq = peakSeq.getGenomicSequence();
			GenomicCoordinate startCoord;
			//get last coordinate for this sequence, if it has been queried before
			if(lastCoord.containsKey(peakSeq)){
				startCoord = lastCoord.get(peakSeq);
			}else{
				startCoord = seq.getStart();
			}
			//create the search region
			boolean proceed = false;
			while(!proceed){
				//check that the region is fully contained by the sequence
				if(startCoord.getBaseIndex() + length - 1 > seq.getRegion().getEnd().getBaseIndex()){
					//swap this peak to the last sequence position, and decrement the number of sequences remaining
					PeakSequence last = curSeqs.get(numSeqs - 1);
					curSeqs.set(numSeqs - 1, peakSeq);
					curSeqs.set(peakIndex, last);
					seqsRemaining.put(curChrom, numSeqs - 1);
					//flag peak sequence as fully searched and terminate loop
					lastCoord.put(peakSeq, startCoord);
					fullySearched.add(peakSeq);
					proceed = true;
				}else{
					//check the region location and generate a probe or increment the region
					String name = peakSeq.getName() + "_" + Constants.NEG_CTRL_PROBE_NAME + (probes.size()+1);
					Probe p = createProbe(seq, startCoord, length, name, kmer, escore);
					if(p != null){
						probes.add(p);
						lastCoord.put(peakSeq, startCoord.increment(length));
						proceed = true;
					}else{
						startCoord = startCoord.increment(1);
					}
				}
			}
		}
	}
	
	private static Probe createProbe(GenomicSequence seq, GenomicCoordinate start, int len, String name, Kmer kmer, double escore){
		if(kmer != null){
			for(int wordLen : kmer.getWordLengths()){
				if(!subseqsMeetScoreThreshold(seq.getSequence(), seq.toIndex(start), len, wordLen, kmer, escore)){
					return null;
				}
			}
		}
		return new Probe(seq.subsequence(start, start.increment(len - 1)), name, Strand.PLUS);
	}
	
	private static boolean subseqsMeetScoreThreshold(String seq, int start, int len, int subseqLen, Kmer kmer, double escore){
		for(int i=start; i<start + len - subseqLen; i++){
			String subseq = seq.substring(i, i+subseqLen);
			try{
				if(kmer.escore(subseq) > escore){
					return false;
				}
			}catch(Exception e){
				//the subseq contained unrecognized characters -- return false
				return false;
			}
		}
		return true;
	}
	
	private static List<Kmer> toUtilKmer(List<chiptools.jprobe.data.Kmer> kmers){
		List<Kmer> l = new ArrayList<Kmer>();
		for(chiptools.jprobe.data.Kmer k : kmers){
			l.add(k.getKmer());
		}
		return l;
	}
	
	private static Map<Chromosome, List<Peak>> buildPeakMap(List<Peaks> groups, int summit){
		Map<Chromosome, List<Peak>> map = new HashMap<Chromosome, List<Peak>>();
		for(Peaks peaks : groups){
			PeakGroup group = peaks.getPeaks();
			for(Peak p : group){
				if(summit >= 0){
					p = p.aroundSummit(summit);
				}
				if(map.containsKey(p.getChrom())){
					map.get(p.getChrom()).add(p);
				}else{
					List<Peak> list = new ArrayList<Peak>();
					list.add(p);
					map.put(p.getChrom(), list);
				}
			}
		}
		return map;
	}
	
	private static PeakGroup merge(List<PeakGroup> groups){
		List<Peak> peaks = new ArrayList<Peak>();
		for(PeakGroup group : groups){
			for(Peak p : group){
				peaks.add(p);
			}
		}
		return new PeakGroup(peaks);
	}

	private static List<PeakGroup> toPeakGroup(List<Peaks> peaks, int summit){
		List<PeakGroup> group = new ArrayList<PeakGroup>();
		if(summit >= 0){
			for(Peaks p : peaks){
				group.add(p.getPeaks().aroundSummit(summit));
			}
		}else{
			for(Peaks p : peaks){
				group.add(p.getPeaks());
			}
		}
		return group;
	}
	
	
	
	
	
	
}
