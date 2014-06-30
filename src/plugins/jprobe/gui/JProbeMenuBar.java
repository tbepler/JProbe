package plugins.jprobe.gui;

import java.util.Comparator;

import javax.swing.JMenu;
import util.gui.SortedJMenuBar;

public class JProbeMenuBar extends SortedJMenuBar{
	private static final long serialVersionUID = 1L;
	
	protected static class MenuComparator implements Comparator<JMenu>{
		
		private final JMenu m_PreferencesMenu;
		private final JMenu m_HelpMenu;
		private final JMenu m_FileMenu;
		
		protected MenuComparator(JMenu preferences, JMenu help, JMenu file){
			m_PreferencesMenu = preferences;
			m_HelpMenu = help;
			m_FileMenu = file;
		}
		
		@Override
		public int compare(JMenu m1, JMenu m2) {
			if(m1 == m2) return 0;
			if(m1 == null) return 1;
			if(m2 == null) return -1;
			if(m1 == m_FileMenu) return -1;
			if(m2 == m_FileMenu) return 1;
			if(m1 == m_HelpMenu) return 1;
			if(m2 == m_HelpMenu) return -1;
			if(m1 == m_PreferencesMenu) return 1;
			if(m2 == m_PreferencesMenu) return -1;
			return m1.getText().compareTo(m2.getText());
		}
		
	}
	
	public JProbeMenuBar(JMenu preferences, JMenu help, JMenu file){
		super(new MenuComparator(preferences, help, file));
		if(preferences != null){
			this.add(preferences);
		}
		if(help != null){
			this.add(help);
		}
		if(file != null){
			this.add(file);
		}
	}

}
