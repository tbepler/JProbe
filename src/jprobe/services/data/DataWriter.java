package jprobe.services.data;

import java.io.BufferedWriter;

import javax.swing.filechooser.FileNameExtensionFilter;


public interface DataWriter {
	
	public FileNameExtensionFilter[] getValidWriteFormats();
	public Class<? extends Data> getWriteClass();
	public void write(Data data, FileNameExtensionFilter format, BufferedWriter out) throws Exception;

}
