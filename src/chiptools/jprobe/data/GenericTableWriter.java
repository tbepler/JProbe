package chiptools.jprobe.data;

import java.io.BufferedWriter;

import javax.swing.filechooser.FileNameExtensionFilter;

import jprobe.services.data.Data;
import jprobe.services.data.DataWriter;

public class GenericTableWriter implements DataWriter{

	@Override
	public FileNameExtensionFilter[] getValidWriteFormats() {
		return new FileNameExtensionFilter[]{
				new FileNameExtensionFilter("Tab delimited table", "txt", "*")	
		};
	}

	@Override
	public Class<? extends Data> getWriteClass() {
		return GenericTable.class;
	}

	@Override
	public void write(Data data, FileNameExtensionFilter format,
			BufferedWriter out) throws Exception {
		GenericTable table = (GenericTable) data;
		table.write(out);
	}

}
