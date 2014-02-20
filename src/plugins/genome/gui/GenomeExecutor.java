package plugins.genome.gui;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.SwingWorker;

import plugins.genome.GenomeActivator;
import plugins.genome.services.GenomeFunction;
import util.genome.reader.BasicGenomeReader;
import util.genome.reader.GenomeReader;
import util.progress.ProgressListener;
import util.progress.ProgressPanel;
import util.progress.ProgressWindow;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.data.Field;

public class GenomeExecutor extends SwingWorker<Data, Object>{
	
	
	
	private JProbeCore m_Core;
	private GenomeFunction m_Function;
	private File m_GenomeFile;
	private Data[] m_DataArgs;
	private Field[] m_FieldArgs;
	
	public GenomeExecutor(JProbeCore core, GenomeFunction function, File genomeFile, Data[] dataArgs, Field[] fieldArgs){
		m_Core = core;
		m_Function = function;
		m_GenomeFile = genomeFile;
		m_DataArgs = dataArgs;
		m_FieldArgs = fieldArgs;
	}
	
	@Override
	protected Data doInBackground() throws Exception {
		try{
		ProgressWindow monitor = new ProgressWindow(m_Function.getName()){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onCancel(){
				GenomeExecutor.this.cancel(true);
				super.onCancel();
			}
			@Override
			protected void onClose(){
				this.onCancel();
			}
			public void complete(){
				this.onComplete();
			}
			public void cancel(){
				super.onCancel();
			}
			@Override
			protected ProgressPanel createProgressPanel(){
				return new ProgressPanel(){
					private static final long serialVersionUID = 1L;
					@Override
					protected void onUpdate(Object source, int progress, int maxProgress, String message, boolean indeterminant) {
						this.setProgressText(String.valueOf(progress)+"%");
					}
					@Override
					protected void onCanceled(Object source) {
						cancel();
					}
					@Override
					protected void onCompleted(Object source) {
						complete();
					}
				};
			}
		};
		Collection<ProgressListener> l = new HashSet<ProgressListener>();
		l.add(monitor);
		GenomeReader r = new BasicGenomeReader(m_GenomeFile, l);
		return m_Function.run(r, m_DataArgs, m_FieldArgs);
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	protected void done(){
		try {
			m_Core.getDataManager().addData(this.get(), GenomeActivator.getBundle());
		} catch (Exception e){
			ErrorHandler.getInstance().handleException(e, GenomeActivator.getBundle());
		}
	}
	
	public void executeFunction(){
		try {
			this.execute();
		} catch (Exception e) {
			ErrorHandler.getInstance().handleException(e, GenomeActivator.getBundle());
		}
	}

}
