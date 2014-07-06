package jprobe.save;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Map;

import jprobe.system.Constants;

import org.apache.commons.io.input.BoundedInputStream;

import util.ByteCounterOutputStream;
import util.save.ImportException;
import util.save.LoadException;
import util.save.SaveException;
import util.save.Saveable;

public class SaveUtil {
	
	public static long save(ObjectOutputStream out, String outName, Map<String,Saveable> saveables) throws SaveException{
		ByteCounterOutputStream counter = new ByteCounterOutputStream(out);
		try {
			ObjectOutputStream oout = new ObjectOutputStream(counter);
			String tempName = Constants.USER_JPROBE_DIR + File.separator + ".temp."+outName;
			File temp = new File(tempName);
			int count = 0;
			while(temp.exists()){
				temp = new File(tempName+count);
				++count;
			}
			temp.createNewFile();
			for(String tag : saveables.keySet()){
				if(temp.canWrite() && temp.canRead()){
					OutputStream tempOut = new BufferedOutputStream( new FileOutputStream(temp));
					long bytes = saveables.get(tag).saveTo(tempOut, outName);
					tempOut.close();
					
					Tag header = new Tag(tag, bytes);
					oout.writeObject(header);
					BufferedInputStream tempIn = new BufferedInputStream( new FileInputStream(temp) );
					int read;
					while((read = tempIn.read()) != -1){
						oout.writeByte(read);
					}
					tempIn.close();
				}else{
					ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
					long bytes = saveables.get(tag).saveTo(tempOut, outName);
					Tag header = new Tag(tag, bytes);
					oout.writeObject(header);
					
					tempOut.writeTo(oout);
					tempOut.close();
				}
			}
			temp.delete();
			out.close();
		} catch (FileNotFoundException e) {
			throw new SaveException(e);
		} catch (IOException e) {
			throw new SaveException(e);
		}
		return counter.bytesWritten();
	}
	
	public static void load(ObjectInputStream in, String inName, Map<String, Saveable> saveables) throws LoadException{
		try {
			boolean finished = false;
			while(!finished){
				try {
					//read a block of saved data
					Tag header = (Tag) in.readObject();
					String id = header.getId();
					long size = header.getNumBytes();
					if(saveables.containsKey(id)){
						//link the input stream to a BoundedInputStream of appropriate size
						BoundedInputStream tempIn = new BoundedInputStream(in, size);
						//pass the new inputstream to the saveable for loading
						try{
							saveables.get(id).loadFrom(tempIn, inName);
						} catch (Exception e){
							//an error occurred in the saveable while loading, ignore it and move on
						}
						tempIn.close();
					}else{
						//there is no saveable loaded to read this data, so skip it
						for(int i=0; i<size; i++){
							in.read();
						}
					}
				} catch (Exception e) {
					//an exception occurred, so loading is finished
					finished = true;
				}
			}
			in.close();
		} catch (FileNotFoundException e) {
			throw new LoadException(e);
		} catch (IOException e) {
			throw new LoadException(e);
		}
	}
	
	public static void importFrom(ObjectInputStream in, String inName, Map<String, Saveable> saveables) throws ImportException{
		try {
			boolean finished = false;
			while(!finished){
				try {
					//read a block of saved data
					Tag header = (Tag) in.readObject();
					String id = header.getId();
					long size = header.getNumBytes();
					if(saveables.containsKey(id)){
						//link the input stream to a BoundedInputStream of appropriate size
						BoundedInputStream tempIn = new BoundedInputStream(in, size);
						//pass the new inputstream to the saveable for importing
						try{
							saveables.get(id).importFrom(tempIn, inName);
						} catch (Exception e){
							//an error occurred in the saveable while importing, ignore it and move on
						}
						tempIn.close();
					}else{
						//there is no saveable loaded to read this data, so skip it
						for(int i=0; i<size; i++){
							in.read();
						}
					}
				} catch (Exception e) {
					//an exception occurred, so importing is finished
					finished = true;
				}
			}
			in.close();
		} catch (FileNotFoundException e) {
			throw new ImportException(e);
		} catch (IOException e) {
			throw new ImportException(e);
		}
	}
	
}
