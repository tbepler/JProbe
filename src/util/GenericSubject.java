package util;

import java.util.Collection;
import java.util.HashSet;

public class GenericSubject<T> implements Subject<T> {

	private final Collection<Observer<T>> m_Obs = new HashSet<Observer<T>>();
	
	@Override
	public void register(Observer<T> obs) {
		m_Obs.add(obs);
	}

	@Override
	public void unregister(Observer<T> obs) {
		m_Obs.remove(obs);
	}
	
	protected void notifyObservers(T notification){
		for(Observer<T> obs : m_Obs){
			obs.update(this, notification);
		}
	}

}
