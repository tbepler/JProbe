package chiptools.jprobe.command;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import util.genome.GenomicSequence;
import util.genome.peak.AbstractPeakQuery;
import util.genome.peak.Peak;
import util.genome.peak.PeakGroup;
import util.genome.peak.PeakSequence;
import util.genome.reader.GenomeReader;
import util.genome.reader.GenomeReaderFactory;
import util.genome.reader.query.LocationBoundedSequenceQuery;
import util.genome.reader.query.LocationQuery;
import util.genome.reader.query.SequenceQuery;
import util.progress.ProgressEvent;
import util.progress.ProgressListener;
import chiptools.Constants;
import chiptools.jprobe.ChiptoolsActivator;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;
import jprobe.services.command.Command;

public class PeakFinder implements Command{
	
	public static final String USAGE_FILE = "peakfinder_usage.txt";
	
	public static final String GENOME_TAG = "-g";
	public static final String PEAK_TAG = "-p";
	public static final String OUTPUT_TAG = "-o";
	public static final String SUPPRESS_TAG = "-s";
	public static final String HELP_TAG = "-help";
	
	private static final String USAGE = readUsage();
	
	private static String readUsage(){
		String usage = "Author: "+Constants.AUTHOR+"\n";
		BufferedReader reader = new BufferedReader(new InputStreamReader(PeakFinder.class.getResourceAsStream(Constants.RESOURCES_PATH+"/"+USAGE_FILE)));
		String line;
		try {
			while((line = reader.readLine()) != null){
				usage += "\n"+line;
			}
			usage += "\n";
		} catch (IOException e) {
			//stuff
		}
		return usage;
	}
	
	private class Config{
		
		public final InputStream ORIGINAL_INPUT;
		public final PrintStream ORIGINAL_OUTPUT;
		public final File GENOME;
		public final boolean SUPPRESS;
		
		public Config(InputStream oin, PrintStream oout, File genome, boolean suppress){
			ORIGINAL_INPUT = oin;
			ORIGINAL_OUTPUT = oout;
			GENOME = genome;
			SUPPRESS = suppress;
		}
		
	}
	
	
	private final String m_Name = Constants.CMD_NAMES.containsKey(PeakFinder.class) ? Constants.CMD_NAMES.get(PeakFinder.class) : "peakfinder";
	private final String m_Description = Constants.CMD_DESCRIPTIONS.containsKey(PeakFinder.class)
			? Constants.CMD_DESCRIPTIONS.get(PeakFinder.class)
			: "Error finding description";
	
	@Override
	public String getName() {
		return m_Name;
	}

	@Override
	public String getDescription() {
		return m_Description;
	}

	@Override
	public void execute(JProbeCore core, String[] args) {
		switch(core.getMode()){
		case COMMAND:
			this.executeCommand(args);
			break;
		case INTERACTIVE:
			//TODO
			break;
		}
	}
	
	protected void printUsage(){
		System.out.println(m_Name+" - "+m_Description);
		System.out.println(USAGE);
	}
	
	protected Config parseArgs(String[] args){
		InputStream oin = System.in;
		PrintStream oout = System.out;
		File genome = null;
		boolean suppress = false;
		if(args.length == 0){
			this.printUsage();;
			return null;
		}
		for(int i=0; i<args.length; i++){
			if(args[i].equals(SUPPRESS_TAG)){
				suppress = true;
			}
			if(args[i].equals(HELP_TAG)){
				this.printUsage();;
				return null;
			}
			if(args[i].equals(GENOME_TAG) && i+1<args.length){
				genome = new File(args[i+1]);
			}
			if(args[i].equals(PEAK_TAG) && i+1<args.length){
				try {
					System.setIn(new FileInputStream(new File(args[i+1])));
				} catch (FileNotFoundException e) {
					ErrorHandler.getInstance().handleException(e, ChiptoolsActivator.getBundle());
					return null;
				}
			}
			if(args[i].equals(OUTPUT_TAG) && i+1<args.length){
				try {
					System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(new File(args[i+1])))));
				} catch (FileNotFoundException e) {
					ErrorHandler.getInstance().handleException(e, ChiptoolsActivator.getBundle());
					return null;
				}
			}
		}
		if(genome == null){
			ErrorHandler.getInstance().handleException(new Exception("Error: no genome file specified"), ChiptoolsActivator.getBundle());
		}
		return new Config(oin, oout, genome, suppress);
	}
	
	protected void executeCommand(String[] args){
		Config config = this.parseArgs(args);
		if(config == null || config.GENOME == null){
			return;
		}
		File genome = config.GENOME;
		PeakGroup peaks = PeakGroup.parsePeakGroup(System.in);
		Collection<ProgressListener> l = new HashSet<ProgressListener>();
		if(!config.SUPPRESS){
			l.add(new ProgressListener(){

				@Override
				public void update(ProgressEvent event) {
					if(event.getMessage() == null) return;
					System.err.println(event.getMessage());
				}

			});
		}
		GenomeReader reader = GenomeReaderFactory.createGenomeReader(genome, l);
		List<LocationQuery> queries = new ArrayList<LocationQuery>();
		for(Peak peak : peaks){
			queries.add(new AbstractPeakQuery(peak){
				private static final long serialVersionUID = 1L;

				@Override
				public void process(GenomicSequence found) {
					PeakSequence peakSeq = new PeakSequence(found, this.getPeak());
					System.out.println(peakSeq.toString());
				}
				
			});
		}
		reader.read(queries, new ArrayList<SequenceQuery>(), new ArrayList<LocationBoundedSequenceQuery>());
		System.setIn(config.ORIGINAL_INPUT);
		System.out.flush();
		System.setOut(config.ORIGINAL_OUTPUT);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
}
