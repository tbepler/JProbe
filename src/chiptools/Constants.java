package chiptools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import plugins.genome.services.GenomeFunction;
import jprobe.services.command.Command;
import jprobe.services.data.DataReader;
import jprobe.services.data.DataWriter;
import jprobe.services.function.Function;
import util.genome.peak.PeakSequence;

public class Constants {
	
	public static final String AUTHOR = "Tristan Bepler";
	
	public static final String HELP_TAG = "-help";
	
	public static final String RESOURCES_PATH = "/chiptools/jprobe/resources";
	public static final String CMD_NAMES_FILE = "/chiptools/jprobe/resources/command_names.txt";
	public static final String CMD_DESCRIPTIONS_FILE = "/chiptools/jprobe/resources/command_descriptions.txt";
	public static final String GENOME_FUNCTION_FILE = RESOURCES_PATH + "/genome_functions.txt";
	public static final String FUNCTIONS_FILE = RESOURCES_PATH + "/functions.txt";
	public static final String READER_WRITER_FILE = RESOURCES_PATH + "/data_reader_writer.txt";
	
	public static final String DATA_PACKAGE = "chiptools.jprobe.data.";
	
	public static List<Class<? extends DataReader>> READER_CLASSES = getClasses(DataReader.class, READER_WRITER_FILE, DATA_PACKAGE);
	public static List<Class<? extends DataWriter>> WRITER_CLASSES = getClasses(DataWriter.class, READER_WRITER_FILE, DATA_PACKAGE);

	public static final String FUNCTION_PACKAGE = "chiptools.jprobe.function.";
	
	public static final List<Class<? extends GenomeFunction>> GENOME_FUNCTION_CLASSES = getClasses(GenomeFunction.class, GENOME_FUNCTION_FILE, FUNCTION_PACKAGE);
	public static final List<Class<? extends Function>> FUNCTION_CLASSES = getClasses(Function.class, FUNCTIONS_FILE, FUNCTION_PACKAGE);
	
	public static String getName(Class<? extends Function> clazz){
		//TODO
		return null;
	}
	
	public static String getDescription(Class<? extends Function> clazz){
		//TODO
		return null;
	}
	
	public static String getCategory(Class<? extends Function> clazz){
		//TODO
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> List<Class<? extends T>> getClasses(Class<T> clazz, String file, String pckg){
		List<Class<? extends T>> list = new ArrayList<Class<? extends T>>();
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(Constants.class.getResourceAsStream(file)));
			String line;
			while((line = reader.readLine()) != null){
				try{
					String name = line.trim();
					Class<?> c = Class.forName(pckg+name);
					if(clazz.isAssignableFrom(c)){
						list.add((Class<? extends T>) c);
					}
				} catch (Exception e){
					e.printStackTrace();
				}
			}
			reader.close();
		} catch (Exception e){
			//do nothing
		}
		return Collections.unmodifiableList(list);
	}
	
	public static final String CMD_PACKAGE = "chiptools.jprobe.command.";
	
	public static final List<Class<? extends Command>> CMD_CLASSES = getCommandClasses();
	
	@SuppressWarnings("unchecked")
	private static List<Class<? extends Command>> getCommandClasses(){
		List<Class<? extends Command>> list = new ArrayList<Class<? extends Command>>();
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(Constants.class.getResourceAsStream(CMD_NAMES_FILE)));
			String line;
			while((line = reader.readLine()) != null){
				try{
					String[] split = line.split("\\s+");
					Class<?> clazz = Class.forName(CMD_PACKAGE+split[0]);
					if(Command.class.isAssignableFrom(clazz)){
						list.add((Class<? extends Command>) clazz);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			reader.close();
		} catch (Exception e){
			//do nothing
		}
		return Collections.unmodifiableList(list);
	}
	
	public static final Map<Class<?>, String> CMD_NAMES = readCmdNames();
			
	private static Map<Class<?>, String> readCmdNames(){
		Map<Class<?>, String> map = new HashMap<Class<?>, String>();
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(Constants.class.getResourceAsStream(CMD_NAMES_FILE)));
			String line;
			while((line = reader.readLine()) != null){
				try{
					String[] split = line.split("\\s+");
					Class<?> clazz = Class.forName(CMD_PACKAGE+split[0]);
					map.put(clazz, split[1]);
				}catch (Exception e){
					//do nothing
				}
			}
			reader.close();
		} catch (Exception e){
			//do nothing
		}
		return Collections.unmodifiableMap(map);
	}
	
	public static final Map<Class<?>, String> CMD_DESCRIPTIONS = readCmdDescriptions();
	
	private static Map<Class<?>, String> readCmdDescriptions(){
		Map<Class<?>, String> map = new HashMap<Class<?>, String>();
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(Constants.class.getResourceAsStream(CMD_DESCRIPTIONS_FILE)));
			String line;
			while((line = reader.readLine()) != null){
				try{
					String[] split = line.split("(---)");
					Class<?> clazz = Class.forName(CMD_PACKAGE+split[0]);
					map.put(clazz, split[1]);
				}catch (Exception e){
					//do nothing
				}
			}
			reader.close();
		} catch (Exception e){
			//do nothing
		}
		return Collections.unmodifiableMap(map);
	}
	
	public static final String GENOME_PEAK_FINDER_NAME = "PeakFinder";
	public static final String GENOME_PEAK_FINDER_TOOLTIP = "This function extracts the sequences for a group of peaks from the genome.";
	
	public static final String PROBE_GEN_FUNCTION_NAME = "ProbeGen";
	public static final String PROBE_GEN_FUNCTION_TOOLTIP = CMD_DESCRIPTIONS.containsKey(chiptools.jprobe.command.ProbeGenerator.class) ? 
			CMD_DESCRIPTIONS.get(chiptools.jprobe.command.ProbeGenerator.class) : 
			"Error finding description";
	
	public static final String PEAK_PARAM_NAME = "Peaks";
	public static final String PEAK_PARAM_TOOLTIP = "The peaks to extract sequences for";
	
	public static final String FILE_WILDCARD = "*";
	
	public static final int NUM_PEAK_FIELDS = 10;
	public static final String PEAKS_TOOLTIP = "A peak group data structure";
	public static final String[] PEAK_HEADER = new String[]{
		"chrom",
		"chromStart",
		"chromEnd",
		"name",
		"score",
		"strand",
		"signalValue",
		"pValue",
		"qValue",
		"peak"
	};
	
	public static final int NUM_PEAK_SEQ_FIELDS = PeakSequence.ELEMENTS;
	public static final String PEAK_SEQ_TOOLTIP = "A group of peak sequences";
	public static final String[] PEAK_SEQ_HEADER = new String[]{
		"sequence",
		"region",
		"name",
		"score",
		"strand",
		"signalValue",
		"pValue",
		"qValue",
		"peak"
	};
	
	public static final String POSITIVE_INT_REGEX  = "[0-9]+";
	
	public static final String GENOMIC_REGION_FIELD_TOOLTIP = "A genomic region";
	
	public static final String STRING_FIELD_TOOLTIP = "A field containing any String";
	
	public static final String CHROM_FIELD_TOOLTIP = "A chromosome field";
	
	public static final String CHROM_BASE_FIELD_TOOLTIP = "A chromosome nucleotide base index field";
	
	public static final String UCSC_SCORE_FIELD_TOOLTIP = "A score between 0 and 1000";
	public static final short UCSC_SCORE_MAX = 1000;
	public static final short UCSC_SCORE_MIN = 0;
	
	public static final String STRAND_FIELD_TOOLTIP = "The strand this occurs on";
	
	public static final String SIGNAL_VALUE_FIELD_TOOLTIP = "Measurement of overall enrichment";
	public static final double SIGNAL_VALUE_MAX = Double.POSITIVE_INFINITY;
	public static final double SIGNAL_VALUE_MIN = -1;
	
	public static final String PVALUE_FIELD_TOOLTIP = "Measurment of statistical significance (-log10). -1 if no value is assigned.";
	public static final double PVALUE_MIN = -1;
	public static final double PVALUE_MAX = Double.POSITIVE_INFINITY;
	
	public static final String QVALUE_FIELD_TOOLTIP = "Measurement of statistical significance using false discovery rate (-log10). -1 if no value is assigned.";
	public static final double QVALUE_MIN = -1;
	public static final double QVALUE_MAX = Double.POSITIVE_INFINITY;
	
	public static final String POINT_SOURCE_FIELD_TOOLTIP = "Point-source called for this peak; 0-based offset from chromStart. -1 if no point-source called.";
	public static final int POINT_SOURCE_MIN = -1;
	
	
}
