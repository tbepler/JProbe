package plugins.jprobe.gui.services;

import java.awt.Frame;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.osgi.framework.Bundle;

import jprobe.services.ErrorManager;

public class GUIErrorManager implements ErrorManager{
	
	private Frame m_Parent;
	
	public GUIErrorManager(Frame parent){
		m_Parent = parent;
	}

	@Override
	public void handleException(final Exception e, final Bundle thrower) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				JOptionPane.showMessageDialog(m_Parent, e.getMessage(), thrower.getSymbolicName(), JOptionPane.ERROR_MESSAGE);
			}
			
		});
	}

	@Override
	public void handleWarning(final String warning, final Bundle thrower) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				JOptionPane.showMessageDialog(m_Parent, warning, thrower.getSymbolicName(), JOptionPane.WARNING_MESSAGE);
			}
			
		});
	}

}
