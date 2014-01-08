package plugins.jprobe.gui.services;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.osgi.framework.Bundle;

import jprobe.services.error.ErrorHandler;

public class GUIErrorHandler implements ErrorHandler{
	
	private Component parent;
	
	public GUIErrorHandler(Component parent){
		this.parent = parent;
	}

	@Override
	public void handleException(Bundle reporter, Exception e) {
		this.handleException(reporter, e.getMessage());
	}

	@Override
	public void handleException(Bundle reporter, String message) {
		JOptionPane.showMessageDialog(parent, message, reporter.getSymbolicName(), JOptionPane.ERROR_MESSAGE);
	}

}
