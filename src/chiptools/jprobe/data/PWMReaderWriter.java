package chiptools.jprobe.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import jprobe.services.data.Data;
import jprobe.services.data.DataReader;
import jprobe.services.data.DataWriter;

public class PWMReaderWriter implements DataReader, DataWriter{

	@Override
	public FileNameExtensionFilter[] getValidWriteFormats() {
		return new FileNameExtensionFilter[]{
			new FileNameExtensionFilter("PWM file", "txt", "*")	
		};
	}

	@Override
	public void write(Data data, FileNameExtensionFilter format, BufferedWriter out) throws Exception {
		out.write(data.toString());
	}

	@Override
	public FileFilter[] getValidReadFormats() {
		return new FileFilter[]{
			new FileFilter(){

				@Override
				public boolean accept(File arg0) {
					return true;
				}

				@Override
				public String getDescription() {
					return "PWM file";
				}
				
			}
		};
	}

	@Override
	public Data read(FileFilter format, InputStream in) throws Exception {
		util.genome.pwm.PWM pwm = util.genome.pwm.PWM.readPWM(in);
		return new PWM(pwm);
	}

	@Override
	public Class<? extends Data> getWriteClass() {
		return PWM.class;
	}

	@Override
	public Class<? extends Data> getReadClass() {
		return PWM.class;
	}

}
