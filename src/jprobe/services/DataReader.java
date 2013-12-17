package jprobe.services;

import java.util.Map;
import java.util.Scanner;

public interface DataReader {
	public Map<String, String[]> getValidReadFormats();
	public Data read(String format, Scanner s) throws Exception;
}
