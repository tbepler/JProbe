package plugins.dataviewer.gui.datalist;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.Scrollable;

public class VerticleScrollPanel extends JPanel implements Scrollable{
	private static final long serialVersionUID = 1L;

	public VerticleScrollPanel() {
		super();
	}

	public VerticleScrollPanel(boolean arg0) {
		super(arg0);
	}

	public VerticleScrollPanel(LayoutManager arg0, boolean arg1) {
		super(arg0, arg1);
	}

	public VerticleScrollPanel(LayoutManager arg0) {
		super(arg0);
	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return this.getPreferredSize();
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle arg0, int orientation, int direction) {
		return direction;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return true;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int direction) {
		return direction;
	}

}
