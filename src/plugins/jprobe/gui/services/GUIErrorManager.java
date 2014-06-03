package plugins.jprobe.gui.services;

import java.awt.Frame;

import javax.swing.JOptionPane;

import org.osgi.framework.Bundle;

import jprobe.services.ErrorManager;

public class GUIErrorManager implements ErrorManager{
	
	private Frame m_Parent;
	
	public GUIErrorManager(Frame parent){
		m_Parent = parent;
	}

	@Override
	public void handleException(Exception e, Bundle thrower) {
		JOptionPane.showMessageDialog(m_Parent, e.getMessage(), thrower.getSymbolicName(), JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void handleWarning(String warning, Bundle thrower) {
		JOptionPane.showMessageDialog(m_Parent, warning, thrower.getSymbolicName(), JOptionPane.WARNING_MESSAGE);
	}

}
