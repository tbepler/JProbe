package chiptools.jprobe.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;

import jprobe.services.JProbeCore;

public class PeakFinder2 extends AbstractChiptoolsCommand{
	
	private static class Parameter{
		public File GENOME = null;
		public InputStream PEAK_IN = System.in;
		public PrintStream OUT = System.out;
		public int SUMMIT = -1;
		public boolean HELP = false;
	}
	
	private static final Flag<Parameter> GENOME_FLAG = new AbstractFlag<Parameter>(
			'g',
			"genome",
			"the genome file peak sequences will be extracted from",
			true,
			"FILE"
			){

		@Override
		public Parameter parse(Parameter param, String[] args) {
			if(args.length < 1){
				throw new RuntimeException("Genome flag requires an argument");
			}
			param.GENOME = new File(args[0]);
			return param;
		}
		
	};
	
	private static final Flag<Parameter> PEAK_FLAG = new AbstractFlag<Parameter>(
			'p',
			"peaks",
			"file containing the peaks for which sequences will be extracted. peaks are read from standard in if unspecified",
			true,
			"FILE"
			){

		@Override
		public Parameter parse(Parameter param, String[] args) {
			if(args.length > 0){
				File peakFile = new File(args[0]);
				try {
					param.PEAK_IN = new FileInputStream(peakFile);
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
			return param;
		}
		
	};
	
	protected static class PeakFinderHelper extends jprobe.services.command.AbstractCommand.CommandHelper<Parameter>{
		
		public PeakFinderHelper(Flag<Parameter>... argFlags) {
			super(argFlags);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected String generalUsage() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected Parameter newEmptyParameter() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void execute(JProbeCore core, Parameter param) {
			// TODO Auto-generated method stub
			
		}

		
	}
	
	protected PeakFinder2(Class<? extends AbstractChiptoolsCommand> clazz, String defaultName, String defaultDescription) {
		super(PeakFinder2.class, "peakfinder", "Error finding description");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected CommandHelper<?> generateCommandHelper() {
		// TODO Auto-generated method stub
		return null;
	}

}
