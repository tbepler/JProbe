package plugins.functions.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import org.osgi.framework.Bundle;

import plugins.functions.gui.dialog.FunctionDialogHandler;
import plugins.functions.gui.dialog.FunctionPanel;
import jprobe.services.Debug;
import jprobe.services.JProbeCore;
import jprobe.services.Log;
import jprobe.services.function.FunctionPrototype;

public class FunctionMenuItem extends JMenuItem implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private Bundle m_Bundle;
	private FunctionDialogHandler m_FunctionDialog;
	private FunctionPanel m_FunctionPanel;
	
	public FunctionMenuItem(JProbeCore core, Bundle bundle, FunctionPrototype functionPrototype, FunctionDialogHandler dialogWindow){
		super(functionPrototype.getFunctionName());
		m_Bundle = bundle;
		m_FunctionDialog = dialogWindow;
		m_FunctionPanel = new FunctionPanel(functionPrototype, core, bundle);
		this.setEnabled(true);
		this.setVisible(true);
		this.setToolTipText(functionPrototype.getFunctionDescription());
		this.addActionListener(this);
	}
	
	private void doFunction(){
		//code for executing function here
		if(Debug.getLevel() == Debug.LOG || Debug.getLevel() == Debug.FULL){
			Log.getInstance().write(m_Bundle, this.getName()+" clicked");
		}
		if(Debug.getLevel() == Debug.FULL){
			System.out.println(this.getName()+" clicked");
		}
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
