package chiptools;

public class Constants {
	
	public static final int NUM_PEAK_FIELDS = 10;
	public static final String PEAKS_TOOLTIP = "A peak group data structure";
	
	public static final String POSITIVE_INT_REGEX  = "[0-9]+";
	
	public static final String STRING_FIELD_TOOLTIP = "A field containing any String";
	
	public static final String CHROM_FIELD_TOOLTIP = "A chromosome field";
	
	public static final String CHROM_BASE_FIELD_TOOLTIP = "A chromosome nucleotide base index field";
	
	public static final String UCSC_SCORE_FIELD_TOOLTIP = "A score between 0 and 1000";
	public static final short UCSC_SCORE_MAX = 1000;
	public static final short UCSC_SCORE_MIN = 0;
	
	public static final String STRAND_FIELD_TOOLTIP = "The strand this occurs on";
	
	public static final String SIGNAL_VALUE_FIELD_TOOLTIP = "Measurement of overall enrichment";
	public static final double SIGNAL_VALUE_MAX = Double.POSITIVE_INFINITY;
	public static final double SIGNAL_VALUE_MIN = 0;
	
	public static final String PVALUE_FIELD_TOOLTIP = "Measurment of statistical significance (-log10). -1 if no value is assigned.";
	public static final double PVALUE_MIN = -1;
	public static final double PVALUE_MAX = Double.POSITIVE_INFINITY;
	
	public static final String QVALUE_FIELD_TOOLTIP = "Measurement of statistical significance using false discovery rate (-log10). -1 if no value is assigned.";
	public static final double QVALUE_MIN = -1;
	public static final double QVALUE_MAX = Double.POSITIVE_INFINITY;
	
	public static final String POINT_SOURCE_FIELD_TOOLTIP = "Point-source called for this peak; 0-based offset from chromStart. -1 if no point-source called.";
	public static final int POINT_SOURCE_MIN = -1;
	
	
}
