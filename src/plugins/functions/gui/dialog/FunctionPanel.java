package plugins.functions.gui.dialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.osgi.framework.Bundle;

import plugins.functions.gui.SwingFunctionExecutor;
import util.gui.DoNothingOnPress;
import util.gui.OnPress;
import util.gui.StateListener;
import util.gui.StateNotifier;
import jprobe.services.ErrorHandler;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.data.Field;
import jprobe.services.function.DataParameter;
import jprobe.services.function.FieldParameter;
import jprobe.services.function.Function;
import jprobe.services.function.FunctionExecutor;

public class FunctionPanel extends JPanel implements ActionListener, StateListener{
	private static final long serialVersionUID = 1L;

	private static final Dimension BUTTON_SPACING = new Dimension(5,0);
	private static final Insets BUTTON_INSETS = new Insets(2,2,2,2);
	
	private Function m_Function;
	private JProbeCore m_Core;
	private Bundle m_Bundle;
	private ParameterPanel m_ParamPanel;
	private JButton m_CancelButton;
	private JButton m_RunButton;
	private OnPress m_CancelAction = new DoNothingOnPress();
	private OnPress m_RunAction = new DoNothingOnPress();
	
	public FunctionPanel(Function functionPrototype, JProbeCore core, Bundle bundle){
		super(new GridBagLayout());
		m_Function = functionPrototype;
		m_Core = core;
		m_Bundle = bundle;
		this.initParamPanel(functionPrototype.getDataParameters(), functionPrototype.getFieldParameters(), core);
		this.initButtons();
	}
	
	private void initParamPanel(DataParameter[] dataParams, FieldParameter[] fieldParams, JProbeCore core){
		m_ParamPanel = new ParameterPanel(dataParams, fieldParams, core);
		m_ParamPanel.addStateListener(this);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.7;
		gbc.weighty = 0.7;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(m_ParamPanel, gbc);
	}
	
	private void initButtons(){
		JPanel buttonPanel = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = BUTTON_INSETS;
		this.add(buttonPanel, gbc);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		m_RunButton = new JButton("Run");
		m_RunButton.addActionListener(this);
		m_RunButton.setEnabled(this.argumentsValid());
		buttonPanel.add(m_RunButton);
		buttonPanel.add(Box.createRigidArea(BUTTON_SPACING));
		m_CancelButton = new JButton("Cancel");
		m_CancelButton.addActionListener(this);
		buttonPanel.add(m_CancelButton);
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
		return m_Function.getName();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_RunButton){
			try{
				FunctionExecutor funEx = new SwingFunctionExecutor(m_Function, this.getDataArgs(), this.getFieldArgs(), m_Core.getDataManager(), m_Bundle);
				funEx.execute();
				m_RunAction.act();
			} catch (Exception ex){
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
	
	public JButton getRunButton(){
		return m_RunButton;
	}

	@Override
	public void update(StateNotifier source) {
		m_RunButton.setEnabled(this.argumentsValid());
	}
	
}
