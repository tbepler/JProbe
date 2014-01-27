package plugins.testDataAndFunction;

import java.util.Scanner;

import javax.swing.filechooser.FileNameExtensionFilter;

import jprobe.services.data.Data;
import jprobe.services.data.DataReader;

public class TestDataReader implements DataReader{
	
	private static final FileNameExtensionFilter[] READ_FORMATS = TestData.FORMATS;

	@Override
	public FileNameExtensionFilter[] getValidReadFormats() {
		return READ_FORMATS;
	}

	@Override
	public Data read(FileNameExtensionFilter format, Scanner s) throws Exception {
		boolean validFormat = false;
		for(FileNameExtensionFilter f : READ_FORMATS){
			if(format == f){
				validFormat = true;
				break;
			}
		}
		if(!validFormat){
			throw new Exception(format.getDescription()+ " is not a valid read format");
		}
		String string = null;
		Integer integer = null;
		Double d = null;
		if(s.hasNext()){
			string = s.next();
		}
		if(s.hasNextInt()){
			integer = s.nextInt();
		}
		if(s.hasNextDouble()){
			d = s.nextDouble();
		}
		if(string == null || integer == null || d == null){
			throw new Exception("Fields are missing from the read data");
		}
		return new TestData(string, integer, d);
	}

}
