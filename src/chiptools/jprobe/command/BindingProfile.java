package chiptools.jprobe.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import util.ArgsParser;
import util.ArgsParser.Argument;
import util.genome.Sequences;
import util.genome.Sequences.Profile;
import util.genome.kmer.Kmer;
import util.genome.kmer.Kmers;
import util.genome.pwm.PWM;
import chiptools.Constants;
import chiptools.jprobe.ChiptoolsActivator;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;
import jprobe.services.command.Command;

public class BindingProfile implements Command{
	
	public static final String USAGE_FILE = "bindingprofile_usage.txt";
	
	public static final String HELP_TAG = "-help";
	public static final String KMER_TAG = "-k";
	public static final String PWM_TAG = "-p";
	
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
	
	private final String m_Name = Constants.CMD_NAMES.containsKey(BindingProfile.class) ? Constants.CMD_NAMES.get(BindingProfile.class) : "bindingprofile";
	private final String m_Description = Constants.CMD_DESCRIPTIONS.containsKey(BindingProfile.class)
			? Constants.CMD_DESCRIPTIONS.get(BindingProfile.class)
			: "Error finding description";

	@Override
	public String getName() {
		return m_Name;
	}

	@Override
	public String getDescription() {
		return m_Description;
	}
	
	protected void printUsage(){
		System.out.println(m_Name+" - "+m_Description);
		System.out.println(USAGE);
	}
	
	private class HelpArg extends Argument{
		
		private boolean helped = false;
		
		@Override
		public String getTag() {
			return HELP_TAG;
		}

		@Override
		public void parse(String[] args) {
			printUsage();
			helped = true;
		}
		
		public boolean helpRequested(){
			return helped;
		}
		
	}
	
	private class FileArg extends Argument{

		private String[] m_Args = new String[]{};
		private final String m_Tag;
		
		public FileArg(String tag){
			m_Tag = tag;
		}
		
		@Override
		public String getTag() {
			return m_Tag;
		}

		@Override
		public void parse(String[] args) {
			m_Args = args;
		}
		
		public String[] getFiles(){
			return m_Args;
		}
		
	}

	@Override
	public void execute(JProbeCore core, String[] args) {
		if(args.length < 1){
			this.printUsage();
			return;
		}
		Argument[] parse = new Argument[]{
				new FileArg(KMER_TAG),
				new FileArg(PWM_TAG),
				new HelpArg()
		};
		ArgsParser.parse(args, parse);
		if(((HelpArg) parse[2]).helpRequested()){
			return;
		}
		String[] kmerFiles = ((FileArg) parse[0]).getFiles();
		String[] pwmFiles = ((FileArg) parse[1]).getFiles();
		//read kmer files
		Kmer[] kmers = new Kmer[kmerFiles.length];
		for(int i=0; i<kmers.length; i++){
			try {
				kmers[i] = Kmers.readKmer(new FileInputStream(new File(kmerFiles[i])));
			} catch (FileNotFoundException e) {
				ErrorHandler.getInstance().handleException(e, ChiptoolsActivator.getBundle());
				kmers[i] = null;
			}
		}
		//read pwm files
		PWM[] pwms = new PWM[pwmFiles.length];
		for(int i=0; i<pwms.length; i++){
			try {
				pwms[i] = PWM.readPWM(new FileInputStream(new File(pwmFiles[i])));
			} catch (FileNotFoundException e) {
				ErrorHandler.getInstance().handleException(e, ChiptoolsActivator.getBundle());
				pwms[i] = null;
			}
		}
		//read sequences from stdin and print profiles to stdout
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line;
		try {
			while((line = reader.readLine()) != null){
				try{
					String[] tokens = line.split("\\s+");
					String seq = tokens[0].toUpperCase();
					String name = tokens[2];
					Profile p = Sequences.profile(seq, kmers, kmerFiles, pwms, pwmFiles);
					System.out.println(name+">");
					System.out.println(seq);
					System.out.println(p);
				} catch (Exception e){
					ErrorHandler.getInstance().handleException(e, ChiptoolsActivator.getBundle());
				}
			}
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, ChiptoolsActivator.getBundle());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
