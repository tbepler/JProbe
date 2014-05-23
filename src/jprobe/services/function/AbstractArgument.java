package jprobe.services.function;

import java.util.Collection;
import java.util.HashSet;

public abstract class AbstractArgument<P> implements Argument<P> {
	
	private final String m_Name;
	private final String m_Tooltip;
	private final String m_Category;
	private final Character m_Flag;
	private final String m_Prototype;
	private final boolean m_Optional;
	private final Collection<ArgumentListener> m_Listeners = new HashSet<ArgumentListener>();
	
	protected AbstractArgument(String name, String tooltip, String category, Character shortFlag, String prototypeValue, boolean optional){
		m_Name = name;
		m_Tooltip = tooltip;
		m_Category = category;
		m_Flag = shortFlag;
		m_Prototype = prototypeValue;
		m_Optional = optional;
	}

	@Override
	public String getName() { return m_Name; }

	@Override
	public String getTooltip() { return m_Tooltip; }

	@Override
	public String getCategory() { return m_Category; }
	
	@Override
	public Character shortFlag(){ return m_Flag; }
	
	@Override
	public String getPrototypeValue(){ return m_Prototype; }

	@Override
	public boolean isOptional() { return m_Optional; }

	@Override
	public void addListener(ArgumentListener l) { m_Listeners.add(l); }

	@Override
	public void removeListener(ArgumentListener l) { m_Listeners.remove(l); }
	
	protected void notifyListeners(){
		for(ArgumentListener l : m_Listeners){
			l.update(this, this.isValid());
		}
	}

}
