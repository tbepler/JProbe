package jprobe;

import java.io.File;
import java.io.FileFilter;

import util.FileUtil;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;
import jprobe.services.Log;

public class AutosaveThread extends Thread{
	
	private static final FileFilter FILTER = new FileFilter(){

		@Override
		public boolean accept(File f) {
			return f.getName().endsWith(Constants.WORKSPACE_FILE_EXTENSION);
		}
		
	};
	
	private final JProbeCore m_Core;
	private final String m_Dir; 
	private final long m_Millis;
	private final int m_MaxSaves;
	private volatile boolean m_Terminate = false;
	
	public AutosaveThread(JProbeCore core, String dir, double freq, int maxSaves){
		super();
		m_Core = core;
		m_Dir = dir;
		//init dir if it doesn't exist
		File d = new File(m_Dir);
		if(!d.exists()) d.mkdirs();
		m_Millis = (long) freq * 60 * 1000;
		m_MaxSaves = maxSaves;
	}
	
	@Override
	public void run(){
		while(!isTerminated()){
			try {
				Thread.sleep(m_Millis);
			} catch (InterruptedException e) {
				ErrorHandler.getInstance().handleException(e, null);
			}
			if(isTerminated()){
				break;
			}
			save();
		}
	}
	
	public void terminate(){
		m_Terminate = true;
	}
	
	public boolean isTerminated(){
		return m_Terminate;
	}
	
	private void save(){
		File dir = new File(m_Dir);
		int fileCount = FileUtil.countFiles(dir, FILTER);
		while(fileCount >= m_MaxSaves){
			File oldest = FileUtil.getOldestFile(dir, FILTER);
			if(!oldest.delete()){
				ErrorHandler.getInstance().handleWarning("Autosave: unable to remove old file "+oldest +". Aborting autosave.", null);
				return;
			}
			fileCount = FileUtil.countFiles(dir, FILTER);
		}
		String name = m_Dir + File.separator + Constants.AUTOSAVE_NAME;
		String ext = "." + Constants.WORKSPACE_FILE_EXTENSION;
		File save = new File(name + ext);
		int index = 1;
		while(save.exists()){
			save = new File(name + index + ext);
			++index;
		}
		m_Core.save(save);
		Log.getInstance().write(null, "Autosaved to "+save);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
