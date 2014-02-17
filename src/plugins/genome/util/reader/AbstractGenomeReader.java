package plugins.genome.util.reader;

import java.util.Collection;
import java.util.HashSet;

import jprobe.services.function.ProgressEvent;
import jprobe.services.function.ProgressListener;

public abstract class AbstractGenomeReader implements GenomeReader{
	
	private Collection<ProgressListener> m_Listeners = new HashSet<ProgressListener>();

	protected void notifyListeners(ProgressEvent event){
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
