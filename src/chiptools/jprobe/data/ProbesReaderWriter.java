package chiptools.jprobe.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import util.genome.probe.ProbeGroup;
import jprobe.services.data.Data;
import jprobe.services.data.DataReader;
import jprobe.services.data.DataWriter;

public class ProbesReaderWriter implements DataReader, DataWriter{

	@Override
	public FileNameExtensionFilter[] getValidWriteFormats() {
		return new FileNameExtensionFilter[]{
			new FileNameExtensionFilter("Probe file", "txt","*")	
		};
	}

	@Override
	public void write(Data data, FileNameExtensionFilter format, BufferedWriter out) throws Exception {
		ProbeGroup group = ((Probes) data).getProbeGroup();
		for(int i=0; i<group.size(); i++){
			out.write(group.getProbe(i).toString(i+1) + "\n");
		}
	}

	@Override
	public FileFilter[] getValidReadFormats() {
		return new FileFilter[]{
			new FileFilter(){

				@Override
				public boolean accept(File arg0) {
					return true;
				}

				@Override
				public String getDescription() {
					return "Probe file";
				}
				
			}
		};
	}

	@Override
	public Data read(FileFilter format, InputStream in) throws Exception {
		ProbeGroup probes = ProbeGroup.readProbeGroup(in);
		return new Probes(probes);
	}

	@Override
	public Class<? extends Data> getWriteClass() {
		return Probes.class;
	}

	@Override
	public Class<? extends Data> getReadClass() {
		return Probes.class;
	}

}
