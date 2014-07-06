package util.file;

import java.io.File;
import java.io.FileFilter;

public class FileUtil {
	
	public static File createUniqueFile(String name){
		File f = new File(name);
		if(f.exists()){
			int count = 0;
			f = new File(name + count);
			while(f.exists()){
				f = new File(name + (++count));
			}
		}
		return f;
	}
	
	public static File createUniqueFile(File parent, String name){
		File f = new File(parent, name);
		if(f.exists()){
			int count = 0;
			f = new File(name + count);
			while(f.exists()){
				f = new File(parent, name + (++count));
			}
		}
		return f;
	}
	
	public static boolean canWriteDirectory(File dir){
		String testName = ".testFile";
		File test = createUniqueFile(dir, testName);
		try{
			test.createNewFile();
			test.delete();
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public static File getOldestFile(String dir, FileFilter filter){
		return getOldestFile(new File(dir), filter);
	}
	
	public static File getOldestFile(File dir, FileFilter filter){
		long mod = Long.MAX_VALUE;
		File oldest = null;
		for(File f : dir.listFiles(filter)){
			if(f.lastModified() < mod){
				oldest = f;
				mod = f.lastModified();
			}
		}
		return oldest;
	}
	
	public static File getMostRecentFile(String dir, FileFilter filter){
		return getMostRecentFile(new File(dir), filter);
	}
	
	public static File getMostRecentFile(File dir, FileFilter filter){
		long mod = Long.MIN_VALUE;
		File recent = null;
		for(File f : dir.listFiles(filter)){
			if(f.lastModified() > mod){
				recent = f;
				mod = f.lastModified();
			}
		}
		return recent;
	}
	
	public static int countFiles(String dir, FileFilter filter){
		return countFiles(new File(dir), filter);
	}
	
	public static int countFiles(File dir, FileFilter filter){
		return dir.listFiles(filter).length;
	}
	
}
