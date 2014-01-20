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
import plugins.functions.gui.utils.StateListener;
import plugins.functions.gui.utils.StateNotifier;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.data.Field;
import jprobe.services.function.DataParameter;
import jprobe.services.function.FieldParameter;
import jprobe.services.function.Function;
import jprobe.services.function.FunctionExecutor;
import jprobe.services.function.FunctionPrototype;
import jprobe.services.function.InvalidArgumentsException;

public class FunctionPanel extends JPanel implements ActionListener, StateListener{
	private static final long serialVersionUID = 1L;

	private FunctionPrototype m_FunctionPrototype;
	private JProbeCore m_Core;
	private Bundle m_Bundle;
	private ParameterPanel m_ParamPanel;
	private JButton m_CancelButton;
	private JButton m_RunButton;
	private OnPress m_CancelAction = new DoNothingOnPress();
	private OnPress m_RunAction = new DoNothingOnPress();
	
	public FunctionPanel(FunctionPrototype functionPrototype, JProbeCore core, Bundle bundle){
		super(new GridBagLayout());
		m_FunctionPrototype = functionPrototype;
		m_Core = core;
		m_Bundle = bundle;
		this.initParamPanel(functionPrototype.getDataParameters(), functionPrototype.getFieldParameters(), core);
		this.initButtons();
	}
	
	private void initParamPanel(DataParameter[] dataParams, FieldParameter[] fieldParams, JProbeCore core){
		m_ParamPanel = new ParameterPanel(dataParams, fieldParams, core);
		m_ParamPanel.addStateListener(this);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(m_ParamPanel, gbc);
	}
	
	private void initButtons(){
		m_CancelButton = new JButton("Cancel");
		m_CancelButton.addActionListener(this);
		m_RunButton = new JButton("Run");
		m_RunButton.addActionListener(this);
		m_RunButton.setEnabled(this.argumentsValid());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 2;
		gbc.gridx = 4;
		this.add(m_RunButton, gbc);
		gbc.gridx = 5;
		this.add(m_CancelButton, gbc);
	}
	
	private Data[] getDataArgs(){
		return m_ParamPanel.getDataArgs();
	}
	
	private Field[] getFieldArgs(){
		return m_ParamPanel.getFieldArgs();
	}
	
	private boolean argumentsValid(){
		return m_ParamPanel.isStateValid();
	}
	
	public String getTitle(){
		return m_FunctionPrototype.getFunctionName();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_RunButton){
			try{
				Function run = m_FunctionPrototype.newInstance(this.getDataArgs(), this.getFieldArgs());
				FunctionExecutor ex = new SwingFunctionExecutor(run, m_Core.getDataManager(), m_Bundle);
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

	@Override
	public void update(StateNotifier source) {
		m_RunButton.setEnabled(this.argumentsValid());
	}
	
}
