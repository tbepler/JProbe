package chiptools.jprobe;

import javax.swing.JPanel;

public class Preferences extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private static final Preferences PREF = new Preferences();
	
	public static Preferences getInstance(){
		return PREF;
	}
	
	private Preferences(){ //singleton
		super();
	}
	
}
