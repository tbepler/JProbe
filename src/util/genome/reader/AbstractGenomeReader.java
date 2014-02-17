package util.genome.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;

import plugins.genome.GenomeActivator;
import util.genome.Genome;
import util.progress.ProgressEvent;
import util.progress.ProgressListener;
import jprobe.services.ErrorHandler;

public abstract class AbstractGenomeReader implements GenomeReader{
	
	private Collection<ProgressListener> m_Listeners = new HashSet<ProgressListener>();
	
	protected Genome prereadGenome(File genomeFile){
		String key = genomeFile.getAbsolutePath();
		synchronized(GENOME_HASH){	
			if(GENOME_HASH.containsKey(key)){
				return GENOME_HASH.get(key);
			}
		}
		try {
			this.notifyListeners(new ProgressEvent(this, ProgressEvent.Type.UPDATE, 0, "Prereading genome file: "+genomeFile.getPath(), true));
			Genome genome = new Genome(genomeFile.getName(), new Scanner(genomeFile));
			synchronized(GENOME_HASH){
				GENOME_HASH.put(key, genome);
			}
			return genome;
		} catch (FileNotFoundException e) {
			ErrorHandler.getInstance().handleException(e, GenomeActivator.getBundle());
		}
		return null;
	}

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
