package plugins.functions.gui.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.osgi.framework.Bundle;

import plugins.functions.gui.DoNothingOnPress;
import plugins.functions.gui.OnPress;
import plugins.functions.gui.SwingFunctionExecutor;
import jprobe.services.DataManager;
import jprobe.services.ErrorHandler;
import jprobe.services.function.Function;
import jprobe.services.function.FunctionExecutor;
import jprobe.services.function.FunctionPrototype;
import jprobe.services.function.InvalidArgumentsException;

public class FunctionPanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;

	private FunctionPrototype m_FunctionPrototype;
	private DataManager m_DataManager;
	private Bundle m_Bundle;
	private JButton m_CancelButton;
	private JButton m_RunButton;
	private OnPress m_CancelAction = new DoNothingOnPress();
	private OnPress m_RunAction = new DoNothingOnPress();
	private boolean m_Ready;
	
	public FunctionPanel(FunctionPrototype functionPrototype, DataManager dataManager, Bundle bundle){
		super(new GridBagLayout());
		m_Ready = false;
		m_FunctionPrototype = functionPrototype;
		m_DataManager = dataManager;
		m_Bundle = bundle;
		m_CancelButton = new JButton("Cancel");
		m_CancelButton.addActionListener(this);
		m_RunButton = new JButton("Run");
		m_RunButton.addActionListener(this);
		m_RunButton.setEnabled(false);
		GridBagConstraints gbc = new GridBagConstraints();
		this.add(m_RunButton, gbc);
		gbc.gridx = 2;
		this.add(m_CancelButton, gbc);
	}
	
	void setReady(boolean ready){
		m_Ready = ready;
		m_RunButton.setEnabled(m_Ready);
	}
	
	public String getTitle(){
		return m_FunctionPrototype.getFunctionName();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_RunButton && m_Ready){
			try{
				Function run = m_FunctionPrototype.newInstance(null, null);
				FunctionExecutor ex = new SwingFunctionExecutor(run, m_DataManager, m_Bundle);
				ex.execute();
				m_RunAction.act();
			} catch (InvalidArgumentsException ex){
				ErrorHandler.getInstance().handleException(ex, m_Bundle);
			}
		}
		if(e.getSource() == m_CancelButton){
			m_CancelAction.act();
		}
	}
	
	public void setCancelAction(OnPress cancel){
		this.m_CancelAction = cancel;
	}
	
	public void setRunAction(OnPress run){
		this.m_RunAction = run;
	}
	
}
