package plugins.functions.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import org.osgi.framework.Bundle;

import jprobe.services.Data;
import jprobe.services.Function;
import jprobe.services.JProbeCore;

public class FunctionMenuItem extends JMenuItem implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private Function function;
	private JProbeCore core;
	private Bundle bundle;
	
	public FunctionMenuItem(JProbeCore core, Bundle bundle, Function function){
		super(function.getName());
		this.core = core;
		this.bundle = bundle;
		this.function = function;
		this.setEnabled(true);
		this.setVisible(true);
		this.setToolTipText(function.getDescription());
		this.addActionListener(this);
	}
	
	private void doFunction(){
		//code for executing function here
		System.out.println(function.getName()+" clicked");
		try {
			Data d = function.run(null, null, null, null);
			core.addData(d, bundle);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		this.doFunction();
	}
	
}
