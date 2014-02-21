package plugins.jprobe.gui.services;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.osgi.framework.Bundle;

import jprobe.services.ErrorManager;

public class GUIErrorManager implements ErrorManager{
	
	private Component parent;
	
	public GUIErrorManager(Component parent){
		this.parent = parent;
	}

	@Override
	public void handleException(Exception e, Bundle thrower) {

		JOptionPane.showMessageDialog(parent, e.getMessage(), thrower.getSymbolicName(), JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void handleWarning(String warning, Bundle thrower) {
		JOptionPane.showMessageDialog(parent, warning, thrower.getSymbolicName(), JOptionPane.WARNING_MESSAGE);
	}

}
