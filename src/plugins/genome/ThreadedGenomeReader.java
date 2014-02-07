package plugins.genome;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jprobe.services.ErrorHandler;
import jprobe.services.function.ProgressEvent;
import jprobe.services.function.ProgressEvent.Type;
import plugins.genome.services.reader.AbstractGenomeReader;
import plugins.genome.services.reader.LocationBoundedSequenceQuery;
import plugins.genome.services.reader.LocationQuery;
import plugins.genome.services.reader.SequenceQuery;
import plugins.genome.services.utils.Chromosome;
import plugins.genome.services.utils.GenomicLocation;

public class ThreadedGenomeReader extends AbstractGenomeReader{
	
	private static final Map<String, LocationComparator> GENOME_INDEX_HASH = new HashMap<String, LocationComparator>();
	private static final String CHR_TAG = ">chr";
	
	public static class LocationComparator implements Comparator<GenomicLocation>, Serializable{
		private static final long serialVersionUID = 1L;
		
		private final Map<Chromosome, Integer> m_ChrPriority = new HashMap<Chromosome, Integer>();
		
		public LocationComparator(List<Chromosome> chrOrder){
			for(int i=0; i<chrOrder.size(); i++){
				Chromosome chr = chrOrder.get(i);
				m_ChrPriority.put(chr, i);
			}
		}
		
		public int numChromosomes(){
			return m_ChrPriority.size();
		}
		
		private int getPriority(Chromosome chr){
			return m_ChrPriority.containsKey(chr) ? m_ChrPriority.get(chr) : Integer.MAX_VALUE;
		}
		
		private int getPriority(GenomicLocation loc){
			return getPriority(loc.getChromosome());
		}

		@Override
		public int compare(GenomicLocation arg0, GenomicLocation arg1) {
			if(arg0.getChromosome().equals(arg1.getChromosome())){
				long start = arg0.getBaseIndex();
				long end = arg0.getBaseIndex();
				if(start > end){
					return 1;
				}
				if(start < end){
					return -1;
				}
				return 0;
			}
			return getPriority(arg0) - getPriority(arg1);
		}
		
	}
	
	private final int PROCESSORS = Runtime.getRuntime().availableProcessors();
	
	private final ExecutorService m_ThreadPool;
	private final File m_Genome;
	
	public ThreadedGenomeReader(File genome){
		m_Genome = genome;
		m_ThreadPool = Executors.newFixedThreadPool(PROCESSORS > 1 ? PROCESSORS -1 : 1);
	}

	@Override
	public void read(LocationQuery[] locationQueries, SequenceQuery[] sequenceQueries, LocationBoundedSequenceQuery[] boundedQueries) {
		try {
			final LocationComparator genomeComp = this.getLocationComparator(m_Genome);
			Queue<LocationQuery> remainingLocationQueries = this.generateLocationQueryQueue(genomeComp, locationQueries);
			Queue<LocationBoundedSequenceQuery> remainingBoundedQueries = this.generateBoundedQueryQueue(genomeComp, boundedQueries);
			int numChrs = genomeComp.numChromosomes();
			
		} catch (FileNotFoundException e) {
			this.notifyListeners(new ProgressEvent(this, Type.COMPLETED));
			ErrorHandler.getInstance().handleException(e, GenomeActivator.getBundle());
		}
	}
	
	protected Queue<LocationBoundedSequenceQuery> generateBoundedQueryQueue(final LocationComparator genomeComp, LocationBoundedSequenceQuery[] boundedQueries){
		Queue<LocationBoundedSequenceQuery> queue = new PriorityQueue<LocationBoundedSequenceQuery>(10, new Comparator<LocationBoundedSequenceQuery>(){
			@Override
			public int compare(LocationBoundedSequenceQuery o1, LocationBoundedSequenceQuery o2) {
				return genomeComp.compare(o1.getStart(), o2.getStart());
			}
		});
		for(LocationBoundedSequenceQuery q : boundedQueries){
			queue.add(q);
		}
		return queue;
	}
	
	protected Queue<LocationQuery> generateLocationQueryQueue(final LocationComparator genomeComp, LocationQuery[] locationQueries){
		Queue<LocationQuery> remainingLocationQueries = new PriorityQueue<LocationQuery>(10, new Comparator<LocationQuery>(){
			@Override
			public int compare(LocationQuery o1, LocationQuery o2) {
				return genomeComp.compare(o1.getStart(), o2.getStart());
			}
		});
		for(LocationQuery q : locationQueries){
			remainingLocationQueries.add(q);
		}
		return remainingLocationQueries;
	}
	
	protected boolean isChrMarker(String line){
		return line.startsWith(CHR_TAG);
	}
	
	protected Chromosome parseLine(String line){
		return new Chromosome(line.substring(CHR_TAG.length()));
	}

	protected LocationComparator getLocationComparator(File genome) throws FileNotFoundException{
		String key = genome.getAbsolutePath();
		if(GENOME_INDEX_HASH.containsKey(key)){
			return GENOME_INDEX_HASH.get(key);
		}else{
			LocationComparator comp = this.prereadGenome(genome);
			GENOME_INDEX_HASH.put(key, comp);
			return comp;
		}
	}
	
	private LocationComparator prereadGenome(File genome) throws FileNotFoundException{
		Scanner s = new Scanner(genome);
		this.notifyListeners(new ProgressEvent(this, ProgressEvent.Type.UPDATE, 0, "Prereading genome file: "+genome.getPath(), true));
		List<Chromosome> chromosomes = new ArrayList<Chromosome>();
		while(s.hasNextLine()){
			String line = s.nextLine();
			if(isChrMarker(line)){
				Chromosome chr = parseLine(line);
				chromosomes.add(chr);
			}
		}
		s.close();
		return new LocationComparator(chromosomes);
	}
}
