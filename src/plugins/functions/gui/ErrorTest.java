package plugins.functions.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import org.osgi.framework.Bundle;

import jprobe.services.ErrorHandler;

public class ErrorTest extends JMenuItem implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private Bundle bundle;
	
	public ErrorTest(Bundle bundle){
		super("Error");
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
			ErrorHandler.getInstance().handleException(e1, bundle);
		}
	}

}
