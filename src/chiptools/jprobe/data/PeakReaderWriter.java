package chiptools.jprobe.data;

import java.io.BufferedWriter;
import java.util.Scanner;

import javax.swing.filechooser.FileNameExtensionFilter;

import util.genome.peak.PeakGroup;
import jprobe.services.data.Data;
import jprobe.services.data.DataReader;
import jprobe.services.data.DataWriter;

public class PeakReaderWriter implements DataReader, DataWriter{
	
	private static final FileNameExtensionFilter[] WRITE_FILTERS = new FileNameExtensionFilter[]{
		new FileNameExtensionFilter("ENCODE peak format", "encodePeak", "*")
	};
	private static final FileNameExtensionFilter[] READ_FILTERS = generateReadFilters();
	
	private static FileNameExtensionFilter[] generateReadFilters(){
		FileNameExtensionFilter[] filters = new FileNameExtensionFilter[PeakGroup.FORMATS.length];
		for(int i=0; i<filters.length; i++){
			String[] format = PeakGroup.FORMATS[i];
			filters[i] = new FileNameExtensionFilter(format[0], format[1]);
		}
		return filters;
	}
	
	@Override
	public FileNameExtensionFilter[] getValidWriteFormats() {
		return WRITE_FILTERS;
	}

	@Override
	public void write(Data data, FileNameExtensionFilter format, BufferedWriter out) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FileNameExtensionFilter[] getValidReadFormats() {
		return READ_FILTERS;
	}

	@Override
	public Data read(FileNameExtensionFilter format, Scanner s) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
