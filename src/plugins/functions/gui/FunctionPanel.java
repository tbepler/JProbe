package plugins.functions.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.osgi.framework.Bundle;

import jprobe.services.DataManager;
import jprobe.services.function.Function;
import jprobe.services.function.FunctionExecutor;
import jprobe.services.function.FunctionParam;

public class FunctionPanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private Function function;
	private DataManager dataManager;
	private Bundle bundle;
	private JButton cancelButton;
	private JButton runButton;
	private OnPress cancelAction = new DoNothingOnPress();
	private OnPress runAction = new DoNothingOnPress();
	
	public FunctionPanel(Function function, DataManager dataManager, Bundle bundle){
		super(new GridBagLayout());
		this.function = function;
		this.dataManager = dataManager;
		this.bundle = bundle;
		this.cancelButton = new JButton("Cancel");
		this.cancelButton.addActionListener(this);
		this.runButton = new JButton("Run");
		this.runButton.addActionListener(this);
		GridBagConstraints gbc = new GridBagConstraints();
		this.add(runButton, gbc);
		gbc.gridx = 2;
		this.add(cancelButton, gbc);
	}
	
	public String getTitle(){
		return function.getName();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == runButton){
			FunctionExecutor ex = new SwingFunctionExecutor(function, new FunctionParam(null, null, null, null), dataManager, bundle);
			ex.execute();
			runAction.act();
		}
		if(e.getSource() == cancelButton){
			cancelAction.act();
		}
	}
	
	public void setCancelAction(OnPress cancel){
		this.cancelAction = cancel;
	}
	
	public void setRunAction(OnPress run){
		this.runAction = run;
	}
	
}
