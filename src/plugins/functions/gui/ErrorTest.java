package plugins.functions.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import org.osgi.framework.Bundle;

import jprobe.services.error.ErrorHandler;

public class ErrorTest extends JMenuItem implements ActionListener{
	
	private ErrorHandler errorHandler;
	private Bundle bundle;
	
	public ErrorTest(Bundle bundle, ErrorHandler errorHandler){
		super("Error");
		this.errorHandler = errorHandler;
		this.bundle = bundle;
		this.setEnabled(true);
		this.setVisible(true);
		this.setToolTipText("This button throws a test error");
		this.addActionListener(this);
	}
	
	private void throwError() throws Exception{
		throw new Exception("This is a test exception");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			this.throwError();
		} catch (Exception e1) {
			errorHandler.handleException(bundle, e1);
		}
	}

}
