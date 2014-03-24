package chiptools.jprobe.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import chiptools.jprobe.ChiptoolsActivator;
import util.ArgsParser;
import util.ArgsParser.Argument;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;

public class ProbeJoiner extends AbstractCommand{
	
	public static final String NUM_BINDING_SITES_TAG = "--numBindingSites";
	public static final String MIN_SITE_DIST_TAG = "--minDistance";
	public static final String MAX_SITE_DIST_TAG = "--maxDistance";
	public static final String PROBE_LEN_TAG = "--probeLength";
	public static final String SITE_WIDTH_TAG = "--bindingSite";
	
	public static final int DEFAULT_NUM_BINDING_SITES = 2;
	public static final int DEFAULT_MIN_SITE_DIST = 2;
	public static final int DEFAULT_MAX_SITE_DIST = 16;
	public static final int DEFAULT_SITE_WIDTH = 10;
	public static final int DEFAULT_PROBE_LEN = 36;
	
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
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		try {
			line = reader.readLine();
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, ChiptoolsActivator.getBundle());
		}
		if(args.length < 1 && line == null){
			this.printUsage();
			return;
		}
		Argument[] parse = new Argument[]{
			new HelpArg(),
			new IntArg(DEFAULT_NUM_BINDING_SITES, NUM_BINDING_SITES_TAG),
			new IntArg(DEFAULT_MIN_SITE_DIST, MIN_SITE_DIST_TAG),
			new IntArg(DEFAULT_MAX_SITE_DIST, MAX_SITE_DIST_TAG),
			new IntArg(DEFAULT_SITE_WIDTH, SITE_WIDTH_TAG),
			new IntArg(DEFAULT_PROBE_LEN, PROBE_LEN_TAG)
		};
		ArgsParser.parse(args, parse);
		if(((HelpArg) parse[0]).helpRequested()){
			this.printUsage();
			return;
		}
		int numSites = ((IntArg) parse[1]).getValue();
		int minDist = ((IntArg) parse[2]).getValue();
		int maxDist = ((IntArg) parse[3]).getValue();
		int siteWidth = ((IntArg) parse[4]).getValue();
		int probeLen = ((IntArg) parse[5]).getValue();
	}
	
	
	
	
	
	
	
	
	
	

}
