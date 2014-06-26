package chiptools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JFileChooser;

import jprobe.services.data.DataReader;
import jprobe.services.data.DataWriter;
import util.genome.peak.PeakSequence;

public class Constants {
	
	public static final String AUTHOR = "Tristan Bepler";
	
	public static final String HELP_TAG = "-help";
	
	public static final String PREF_TAB_NAME = "Chiptools";
	public static final String PREF_FILE_NAME = "chiptools.pref";
	
	public static final int SEQS_ARG_MAX_DISPLAY = 5;
	public static final String PROBES_CATEGORIES_SEPARATOR = "=";
	
	public static final String RESOURCES_PATH = "/chiptools/jprobe/resources";
	
	public static final String FUNCTIONS_FILE = RESOURCES_PATH + "/functions.txt";
	public static final String READER_FILE = RESOURCES_PATH + "/data_reader.txt";
	public static final String WRITER_FILE = RESOURCES_PATH + "/data_writer.txt";
	
	//instantiate the JFileChooser lazily
	private static JFileChooser CHIPTOOLS_FILE_CHOOSER = null;
	public static JFileChooser getChiptoolsFileChooser(){
		if(CHIPTOOLS_FILE_CHOOSER == null){
			CHIPTOOLS_FILE_CHOOSER = new JFileChooser();
		}
		return CHIPTOOLS_FILE_CHOOSER;
	}
	
	//instantiate lazily
	private static JFileChooser CHIPTOOLS_DIR_CHOOSER = null;
	private static JFileChooser createDirChooser(){
		JFileChooser dir = new JFileChooser();
		dir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		dir.setAcceptAllFileFilterUsed(false);
		return dir;
	}
	public static JFileChooser getChiptoolsDirChooser(){
		if(CHIPTOOLS_DIR_CHOOSER == null){
			CHIPTOOLS_DIR_CHOOSER = createDirChooser();
		}
		return CHIPTOOLS_DIR_CHOOSER;
	}
	
	public static final String DATA_PACKAGE = "chiptools.jprobe.data.";
	
	public static List<Class<? extends DataReader>> READER_CLASSES = getClasses(DataReader.class, READER_FILE, DATA_PACKAGE);
	public static List<Class<? extends DataWriter>> WRITER_CLASSES = getClasses(DataWriter.class, WRITER_FILE, DATA_PACKAGE);
	
	@SuppressWarnings("unchecked")
	private static <T> List<Class<? extends T>> getClasses(Class<T> clazz, String file, String pckg){
		List<Class<? extends T>> list = new ArrayList<Class<? extends T>>();
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(Constants.class.getResourceAsStream(file)));
			String line;
			while((line = reader.readLine()) != null){
				try{
					String name = line.split("\t")[0].trim();
					Class<?> c = Class.forName(pckg+name);
					if(clazz.isAssignableFrom(c)){
						list.add((Class<? extends T>) c);
					}
				} catch (Exception e){
					throw e;
				}
			}
			reader.close();
		} catch (Exception e){
			throw new RuntimeException(e);
		}
		return Collections.unmodifiableList(list);
	}
	
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
	
	public static final String GRUN = "ggggg";
	public static final String CRUN = "ccccc";
	
	public static final String NEG_CTRL_PROBE_NAME = "NegativeCtrl";
	
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
