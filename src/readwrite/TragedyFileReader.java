package readwrite;


public class TragedyFileReader {
	
	private static final FileFormat[] PEAK_READ_FORMATS = new FileFormat[]{FileFormat.BED, FileFormat.ENCODEPEAK, FileFormat.XML};
	
	public FileFormat[] getPeakReadFormats(){
		return PEAK_READ_FORMATS;
	}
	
	
	
}
