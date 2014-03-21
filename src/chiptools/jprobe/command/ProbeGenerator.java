package chiptools.jprobe.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import util.genome.GenomicRegion;
import util.genome.GenomicSequence;
import util.genome.kmer.Kmer;
import util.genome.kmer.Kmers;
import util.genome.probe.Probe;
import util.genome.probe.ProbeGroup;
import util.genome.probe.ProbeUtils;
import chiptools.Constants;
import jprobe.services.JProbeCore;
import jprobe.services.command.Command;

public class ProbeGenerator implements Command{
	
	public static final String USAGE_FILE = "probegen_usage.txt";
	
	public static final String KMER_TAG = "-k";
	public static final String PWM_TAG = "-p";
	public static final String PROBELEN_TAG = "--probeLength";
	public static final String BINDINGSITE_TAG = "--bindingSite";
	public static final String WINDOW_TAG = "--window";
	public static final String ESCORE_TAG = "--escore";
	public static final String SUPPRESS_TAG = "-s";
	public static final String HELP_TAG = "-help";
	
	public static final int DEFAULT_PROBELEN = 36;
	public static final int DEFAULT_BINDINGSITE = 9;
	public static final int DEFAULT_WINDOW = 3;
	public static final double DEFAULT_ESCORE = 0.3;
	
	protected class Config{
		public Kmer KMER = null;
		public util.genome.pwm.PWM PWM = null;
		public int PROBELEN = DEFAULT_PROBELEN;
		public int BINDINGSITE = DEFAULT_BINDINGSITE;
		public int WINDOW = DEFAULT_WINDOW;
		public double ESCORE = DEFAULT_ESCORE;
		public boolean SUPPRESS = false;
		
	}
	
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
	
	private final String NAME = Constants.CMD_NAMES.containsKey(ProbeGenerator.class) ? Constants.CMD_NAMES.get(ProbeGenerator.class) : "probegen";
	private final String DESCRIPTION = Constants.CMD_DESCRIPTIONS.containsKey(ProbeGenerator.class) ? 
			Constants.CMD_DESCRIPTIONS.get(ProbeGenerator.class) : 
			"Error finding description";
			
	protected Config parseArgs(String[] args){
		Config config = new Config();
		if(args.length < 1){
			return null;
		}
		for(int i=0; i<args.length; i++){
			String cur = args[i];
			if(cur.equals(HELP_TAG)) return null;
			if(cur.equals(KMER_TAG)){
				String kmerPath = args[i+1];
				try {
					Kmer kmer = Kmers.readKmer(new FileInputStream(new File(kmerPath)));
					config.KMER = kmer;
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
			if(cur.equals(PWM_TAG)){
				String pwmPath = args[i+1];
				try {
					util.genome.pwm.PWM pwm = util.genome.pwm.PWM.readPWM(new FileInputStream(new File(pwmPath)));
					config.PWM = pwm;
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
			if(cur.equals(PROBELEN_TAG)){
				int probelen = Integer.parseInt(args[i+1]);
				config.PROBELEN = probelen;
			}
			if(cur.equals(BINDINGSITE_TAG)){
				int bindingsite = Integer.parseInt(args[i+1]);
				config.BINDINGSITE = bindingsite;
			}
			if(cur.equals(WINDOW_TAG)){
				int window = Integer.parseInt(args[i+1]);
				config.WINDOW = window;
			}
			if(cur.equals(ESCORE_TAG)){
				double escore = Double.parseDouble(args[i+1]);
				config.ESCORE = escore;
			}
			if(cur.equals(SUPPRESS_TAG)){
				config.SUPPRESS = true;
			}
		}
		if(config.PWM == null){
			throw new RuntimeException("No PWM file specified.");
		}
		if(config.KMER == null){
			throw new RuntimeException("No Kmer file specified.");
		}
		return config;
	}
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}
	
	protected void printUsage(){
		System.out.println(NAME+" - "+DESCRIPTION);
		System.out.println(USAGE);
	}

	@Override
	public void execute(JProbeCore core, String[] args) {
		Config config;
		try{
			config = parseArgs(args);
		} catch (Exception e){
			System.err.println(e.getMessage());
			this.printUsage();
			return;
		}
		if(config == null){
			this.printUsage();
			return;
		}
		//System.err.println("Args parsed");
		//System.err.println("Kmer type: "+config.KMER.getClass());
		Queue<Probe> probes = new PriorityQueue<Probe>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line;
		try {
			while((line = reader.readLine()) != null){
				try{
					//PeakSequence peakSeq = PeakSequence.parsePeakSequence(line);
					String[] tokens = line.split("\\s");
					GenomicSequence seq = new GenomicSequence(tokens[0], GenomicRegion.parseString(tokens[1]));
					String name = tokens.length > 2 && !tokens[2].equals(".") ? tokens[2] + "_probe" : "probe";
					//System.err.println("Parsing peak "+peakSeq.getName());
					//long time = System.currentTimeMillis();
					List<Probe> peakProbes = ProbeUtils.extractFrom(
							seq,
							name,
							config.KMER,
							config.PWM,
							config.PROBELEN,
							config.BINDINGSITE,
							config.WINDOW,
							config.ESCORE
							);
					//System.err.println("Peak parsed. dt = "+(System.currentTimeMillis() - time));
					probes.addAll(peakProbes);
				} catch (Exception e){
					//proceed
				}
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ProbeGroup group = new ProbeGroup(probes);
		System.out.println(group);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
