package plugins.functions.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import org.osgi.framework.Bundle;

import jprobe.services.DataManager;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.function.Function;
import jprobe.services.function.FunctionExecutor;
import jprobe.services.function.FunctionParam;

public class FunctionMenuItem extends JMenuItem implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private Function function;
	private DataManager dataManager;
	private Bundle bundle;
	
	public FunctionMenuItem(DataManager dataManager, Bundle bundle, Function function){
		super(function.getName());
		this.dataManager = dataManager;
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
			FunctionExecutor ex = new SwingFunctionExecutor(function, new FunctionParam(null, null, null, null), dataManager, bundle);
			ex.execute();
			
			//Data d = function.run(new FunctionParam(null, null, null, null));
			//dataManager.addData(d, bundle);
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
