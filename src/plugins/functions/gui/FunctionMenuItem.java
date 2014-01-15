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
import jprobe.services.function.FunctionPrototype;

public class FunctionMenuItem extends JMenuItem implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private FunctionPrototype m_FunctionPrototype;
	private DataManager m_DataManager;
	private Bundle m_Bundle;
	private FunctionDialogHandler m_FunctionDialog;
	private FunctionPanel m_FunctionPanel;
	
	public FunctionMenuItem(DataManager dataManager, Bundle bundle, FunctionPrototype functionPrototype, FunctionDialogHandler dialogWindow){
		super(functionPrototype.getFunctionName());
		m_DataManager = dataManager;
		m_Bundle = bundle;
		m_FunctionPrototype = functionPrototype;
		m_FunctionDialog = dialogWindow;
		m_FunctionPanel = new FunctionPanel(functionPrototype, dataManager, bundle);
		this.setEnabled(true);
		this.setVisible(true);
		this.setToolTipText(functionPrototype.getFunctionDescription());
		this.addActionListener(this);
	}
	
	private void doFunction(){
		//code for executing function here
		System.out.println(m_FunctionPrototype.getFunctionName()+" clicked");
		try {
			m_FunctionDialog.display(m_FunctionPanel);
			
			//FunctionExecutor ex = new SwingFunctionExecutor(function, new FunctionParam(null, null, null, null), dataManager, bundle);
			//ex.execute();
			
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
