package plugins.jprobe.gui.services;

import java.awt.LayoutManager;

import javax.swing.JPanel;

public abstract class PreferencesPanel extends JPanel{
	
	public PreferencesPanel() {
		super();
	}

	public PreferencesPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public PreferencesPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public PreferencesPanel(LayoutManager layout) {
		super(layout);
	}

	private static final long serialVersionUID = 1L;
	
	public abstract void apply();
	public abstract void close();

}
