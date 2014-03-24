package plugins.testDataAndFunction;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.filechooser.FileFilter;
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
	public Data read(FileFilter format, InputStream s) throws Exception {
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
		BufferedReader reader = new BufferedReader(new InputStreamReader(s));
		String string = null;
		Integer integer = null;
		Double d = null;
		String[] split = reader.readLine().split("\\s");
		string = split[0];
		integer = Integer.parseInt(split[1]);
		d = Double.parseDouble(split[2]);
		if(string == null || integer == null || d == null){
			throw new Exception("Fields are missing from the read data");
		}
		return new TestData(string, integer, d);
	}

	@Override
	public Class<? extends Data> getReadClass() {
		return TestData.class;
	}

}
