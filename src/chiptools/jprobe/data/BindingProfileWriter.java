package chiptools.jprobe.data;

import java.io.BufferedWriter;

import javax.swing.filechooser.FileNameExtensionFilter;

import util.genome.Sequences.Profile;
import jprobe.services.data.Data;
import jprobe.services.data.DataWriter;

public class BindingProfileWriter implements DataWriter{

	@Override
	public FileNameExtensionFilter[] getValidWriteFormats() {
		return new FileNameExtensionFilter[]{
				new FileNameExtensionFilter("BindingProfile file", "txt", "*")	
		};
	}

	@Override
	public Class<? extends Data> getWriteClass() {
		return BindingProfile.class;
	}

	@Override
	public void write(Data data, FileNameExtensionFilter format, BufferedWriter out) throws Exception {
		BindingProfile bindingProfiles = (BindingProfile) data;
		for(Profile p : bindingProfiles){
			out.write(p.toString());
		}
	}

}
