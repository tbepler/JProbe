package jprobe.services.data;

import java.util.Scanner;

import javax.swing.filechooser.FileFilter;


public interface DataReader {
	public FileFilter[] getValidReadFormats();
	public Data read(FileFilter format, Scanner s) throws Exception;
}
