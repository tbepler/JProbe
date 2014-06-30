package util.gui;

import java.awt.Component;
import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class SortedJMenuBar extends JMenuBar{
	private static final long serialVersionUID = 1L;
	
	protected static class DefaultMenuComparator implements Comparator<JMenu>{
		@Override
		public int compare(JMenu m1, JMenu m2) {
			if(m1 == m2) return 0;
			if(m1 == null) return 1;
			if(m2 == null) return -1;
			return m1.getText().compareTo(m2.getText());
		}
	}
	
	private final Collection<JMenu> m_Menues;
	
	public SortedJMenuBar(){
		m_Menues = new PriorityQueue<JMenu>(10, new DefaultMenuComparator());
	}
	
	public SortedJMenuBar(Comparator<JMenu> comparator){
		m_Menues = new PriorityQueue<JMenu>(10, comparator);
	}
	
	private void allocateMenues(){
		super.removeAll();
		for(JMenu menu : m_Menues){
			super.add(menu);
		}
		this.invalidate();
		this.validate();
	}
	
	@Override
	public JMenu add(JMenu menu){
		m_Menues.add(menu);
		this.allocateMenues();
		return menu;
	}
	
	@Override
	public void remove(Component c){
		if(m_Menues.contains(c)){
			m_Menues.remove(c);
			this.allocateMenues();
		}else{
			super.remove(c);
		}
	}
	
	@Override
	public void removeAll(){
		m_Menues.clear();
		super.removeAll();
		this.invalidate();
		this.validate();
	}
	
}
