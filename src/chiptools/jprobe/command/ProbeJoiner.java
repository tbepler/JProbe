package chiptools.jprobe.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import chiptools.jprobe.ChiptoolsActivator;
import util.ArgsParser;
import util.ArgsParser.Argument;
import util.genome.ParsingException;
import util.genome.probe.Probe;
import util.genome.probe.ProbeUtils;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;

public class ProbeJoiner extends AbstractCommand{
	
	public static final String NUM_BINDING_SITES_TAG = "--bindingSites";
	public static final String MIN_SITE_DIST_TAG = "--minDistance";
	public static final String MAX_SITE_DIST_TAG = "--maxDistance";
	public static final String PROBE_LEN_TAG = "--probeLength";
	
	public static final int DEFAULT_NUM_BINDING_SITES = -1;
	public static final int DEFAULT_MIN_SITE_DIST = 2;
	public static final int DEFAULT_MAX_SITE_DIST = 16;
	public static final int DEFAULT_PROBE_LEN = 0;
	
	private static final String USAGE_FILE = "probejoiner_usage.txt";
	
	private class IntArg extends ArgsParser.Argument{
		
		private int m_Val;
		private String m_Tag;
		
		public IntArg(int defaultVal, String tag){
			m_Val = defaultVal;
			m_Tag = tag;
		}
		
		@Override
		public String getTag() {
			return m_Tag;
		}

		@Override
		public void parse(String[] args) {
			try{
				m_Val = Integer.parseInt(args[0]);
			} catch (Exception e){
				ErrorHandler.getInstance().handleException(e, ChiptoolsActivator.getBundle());
			}
		}
		
		public int getValue(){
			return m_Val;
		}
		
	}
	
	public ProbeJoiner(){
		super("probejoiner", "Error finding description", USAGE_FILE);
	}

	@Override
	public void execute(JProbeCore core, String[] args) {
		if(args.length < 1){
			this.printUsage();
			return;
		}
		Argument[] parse = new Argument[]{
			new HelpArg(),
			new IntArg(DEFAULT_NUM_BINDING_SITES, NUM_BINDING_SITES_TAG),
			new IntArg(DEFAULT_MIN_SITE_DIST, MIN_SITE_DIST_TAG),
			new IntArg(DEFAULT_MAX_SITE_DIST, MAX_SITE_DIST_TAG),
			new IntArg(DEFAULT_PROBE_LEN, PROBE_LEN_TAG)
		};
		ArgsParser.parse(args, parse);
		if(((HelpArg) parse[0]).helpRequested()){
			this.printUsage();
			return;
		}
		int numSites = ((IntArg) parse[1]).getValue();
		if(numSites < 0){
			System.err.println("Error: must specify zero or more binding sites for combined probes.");
			return;
		}
		int minDist = ((IntArg) parse[2]).getValue();
		int maxDist = ((IntArg) parse[3]).getValue();
		int probeLen = ((IntArg) parse[4]).getValue();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		List<Probe> input = new ArrayList<Probe>();
		//read the input probes
		String line;
		try {
			while((line = reader.readLine()) != null){
				try {
					input.add(Probe.parseProbe(line));
				} catch (ParsingException e) {
					ErrorHandler.getInstance().handleException(e, ChiptoolsActivator.getBundle());
				}
			}
			reader.close();
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, ChiptoolsActivator.getBundle());
		}
		//iterate over the input probes and join them
		int count = 1;
		for(int i=0; i<input.size(); i++){
			Probe combined;
			if(probeLen > 0){
				combined = ProbeUtils.join(input, i, numSites, minDist, maxDist, probeLen);
			}else{
				//if the probe length is less than or equal to zero then don't bound the probe length
				combined = ProbeUtils.join(input, i, numSites, minDist, maxDist);
			}
			if(combined != null){
				System.out.println(combined.toString(count++));
			}
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	

}
