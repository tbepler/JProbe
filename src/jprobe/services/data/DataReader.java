package jprobe.services.data;

import java.util.Scanner;

import javax.swing.filechooser.FileNameExtensionFilter;


public interface DataReader {
	public FileNameExtensionFilter[] getValidReadFormats();
	public Data read(FileNameExtensionFilter format, Scanner s) throws Exception;
}
