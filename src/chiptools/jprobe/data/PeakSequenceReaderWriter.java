package chiptools.jprobe.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import chiptools.Constants;
import util.genome.peak.PeakSequence;
import util.genome.peak.PeakSequenceGroup;
import jprobe.services.data.Data;
import jprobe.services.data.DataReader;
import jprobe.services.data.DataWriter;

public class PeakSequenceReaderWriter implements DataReader, DataWriter{
	
	private static final FileNameExtensionFilter[] WRITE_FILTERS = new FileNameExtensionFilter[]{
		new FileNameExtensionFilter("PeakSeq format (.peakSeq, .*)", "peakSeq", "*")
	};
	
	private static final FileFilter[] READ_FILTERS = generateReadFilters();
	
	private static FileFilter[] generateReadFilters(){
		FileFilter[] filters = new FileFilter[PeakSequenceGroup.FORMATS.length];
		for(int i=0; i<filters.length; i++){
			String[] format = PeakSequenceGroup.FORMATS[i];
			final String descrip = format[0];
			final String[] exts = new String[format.length-1];
			System.arraycopy(format, 1, exts, 0, exts.length);
			if(containsWildcard(exts)){
				filters[i] = new FileFilter(){

					@Override
					public boolean accept(File arg0) {
						return true;
					}

					@Override
					public String getDescription() {
						return descrip;
					}
					
				};
			}else{
				filters[i] = new FileNameExtensionFilter(descrip, exts);
			}
		}
		return filters;
	}
	
	private static boolean containsWildcard(String[] exts){
		for(String s : exts){
			if(s.equals(Constants.FILE_WILDCARD)){
				return true;
			}
		}
		return false;
	}

	@Override
	public FileNameExtensionFilter[] getValidWriteFormats() {
		return WRITE_FILTERS;
	}

	@Override
	public void write(Data data, FileNameExtensionFilter format, BufferedWriter out) throws Exception {
		PeakSequenceGroup group = ((PeakSequences) data).getPeakSeqs();
		for(PeakSequence p : group){
			out.write(p.toString() + "\n");
		}
	}

	@Override
	public FileFilter[] getValidReadFormats() {
		return READ_FILTERS;
	}

	@Override
	public Data read(FileFilter format, InputStream s) throws Exception {
		PeakSequenceGroup peakSeqs = PeakSequenceGroup.parsePeakSeqGroup(s);
		return new PeakSequences(peakSeqs);
	}

	@Override
	public Class<? extends Data> getWriteClass() {
		return PeakSequences.class;
	}

	@Override
	public Class<? extends Data> getReadClass() {
		return PeakSequences.class;
	}

}
