package util.genome.reader;

import java.util.Collection;
import java.util.HashSet;
import util.progress.ProgressEvent;
import util.progress.ProgressListener;

public abstract class AbstractGenomeReader implements GenomeReader{
	
	private Collection<ProgressListener> m_Listeners = new HashSet<ProgressListener>();
	private UpdateMode m_Mode = UpdateMode.FULL;
	
	protected AbstractGenomeReader(){
		//do nothing
	}
	
	protected AbstractGenomeReader(Collection<ProgressListener> listeners){
		m_Listeners.addAll(listeners);
	}
	
	@Override
	public void setUpdateMode(UpdateMode mode){
		m_Mode = mode;
	}
	
	protected UpdateMode getUpdateMode(){
		return m_Mode;
	}

	protected void notifyListeners(ProgressEvent event){
		if(m_Mode == UpdateMode.NONE){
			return;
		}
		for(ProgressListener l : m_Listeners){
			l.update(event);
		}
	}
	
	@Override
	public void addProgressListener(ProgressListener listener) {
		m_Listeners.add(listener);
	}

	@Override
	public void removeProgressListener(ProgressListener listener) {
		m_Listeners.remove(listener);
	}

}
