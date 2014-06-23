package chiptools.jprobe.data;

import java.io.BufferedWriter;

import javax.swing.filechooser.FileNameExtensionFilter;

import jprobe.services.data.Data;
import jprobe.services.data.DataWriter;

public class AgilentArrayWriter implements DataWriter{

	@Override
	public FileNameExtensionFilter[] getValidWriteFormats() {
		return new FileNameExtensionFilter[]{
				new FileNameExtensionFilter("Agilent array file (.txt, .*)", "txt", "*")	
		};
	}

	@Override
	public Class<? extends Data> getWriteClass() {
		return AgilentArray.class;
	}

	@Override
	public void write(Data data, FileNameExtensionFilter format, BufferedWriter out) throws Exception {
		if(data instanceof AgilentArray){
			AgilentArray array = (AgilentArray)	data;
			for(int i=0; i<array.size(); i++){
				out.write(array.toString(i));
				out.write("\n");
			}
		}
	}

}
