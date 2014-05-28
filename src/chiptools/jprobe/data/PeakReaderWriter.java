package chiptools.jprobe.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import chiptools.Constants;
import util.genome.peak.Peak;
import util.genome.peak.PeakGroup;
import jprobe.services.data.Data;
import jprobe.services.data.DataReader;
import jprobe.services.data.DataWriter;

public class PeakReaderWriter implements DataReader, DataWriter{
	
	private static final FileNameExtensionFilter[] WRITE_FILTERS = new FileNameExtensionFilter[]{
		new FileNameExtensionFilter("ENCODE peak format (.encodePeak, .*)", "encodePeak", "*")
	};
	private static final FileFilter[] READ_FILTERS = generateReadFilters();
	
	private static FileFilter[] generateReadFilters(){
		FileFilter[] filters = new FileFilter[PeakGroup.FORMATS.length];
		for(int i=0; i<filters.length; i++){
			final String[] format = PeakGroup.FORMATS[i];
			if(format[1].equals(Constants.FILE_WILDCARD)){
				filters[i] = new FileFilter(){

					@Override
					public boolean accept(File arg0) {
						return true;
					}

					@Override
					public String getDescription() {
						return format[0];
					}

					
				};
			}else{
				filters[i] = new FileNameExtensionFilter(format[0], format[1]);
			}
		}
		return filters;
	}
	
	@Override
	public FileNameExtensionFilter[] getValidWriteFormats() {
		return WRITE_FILTERS;
	}

	@Override
	public void write(Data data, FileNameExtensionFilter format, BufferedWriter out) throws Exception {
		PeakGroup group = ((Peaks) data).getPeaks();
		for(Peak p : group){
			out.write(p.toString() + "\n");
		}
	}

	@Override
	public FileFilter[] getValidReadFormats() {
		return READ_FILTERS;
	}

	@Override
	public Data read(FileFilter format, InputStream s) throws Exception {
		PeakGroup peaks = PeakGroup.parsePeakGroup(s);
		return new Peaks(peaks);
	}

	@Override
	public Class<? extends Data> getWriteClass() {
		return Peaks.class;
	}

	@Override
	public Class<? extends Data> getReadClass() {
		return Peaks.class;
	}

}
