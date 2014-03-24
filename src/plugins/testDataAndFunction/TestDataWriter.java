package plugins.testDataAndFunction;

import java.io.BufferedWriter;

import javax.swing.filechooser.FileNameExtensionFilter;

import jprobe.services.data.Data;
import jprobe.services.data.DataWriter;

public class TestDataWriter implements DataWriter{
	
	public static final FileNameExtensionFilter[] WRITE_FORMATS = TestData.FORMATS;
	
	@Override
	public FileNameExtensionFilter[] getValidWriteFormats() {
		return WRITE_FORMATS;
	}

	@Override
	public void write(Data data, FileNameExtensionFilter format, BufferedWriter out) throws Exception {
		boolean formatValid = false;
		for(FileNameExtensionFilter f : WRITE_FORMATS){
			if(f == format){
				formatValid = true;
				break;
			}
		}
		if(!formatValid){
			throw new Exception(format.getDescription()+" is not a valid write format");
		}
		if(data instanceof TestData){
			TestData write = (TestData) data;
			out.write(write.getString()+"\t"+write.getInt()+"\t"+write.getDouble());
			return;
		}
		throw new Exception("Could not write data "+data);
	}

	@Override
	public Class<? extends Data> getWriteClass() {
		return TestData.class;
	}

}
