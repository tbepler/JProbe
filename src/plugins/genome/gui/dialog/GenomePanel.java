package plugins.genome.gui.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;

import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.data.Field;
import plugins.functions.gui.dialog.ParameterPanel;
import plugins.genome.services.GenomeFunction;
import util.gui.DoNothingOnPress;
import util.gui.OnPress;
import util.gui.StateListener;
import util.gui.StateNotifier;

public class GenomePanel extends JPanel implements ActionListener, StateListener{
	private static final long serialVersionUID = 1L;
	
	private GenomeFunction m_Function;
	private JProbeCore m_Core;
	private GenomeFilePanel m_FilePanel;
	private ParameterPanel m_ParamPanel;
	private JButton m_Run;
	private JButton m_Cancel;
	private OnPress m_RunAction = new DoNothingOnPress();
	private OnPress m_CancelAction = new DoNothingOnPress();
	
	public GenomePanel(GenomeFunction function, GenomeFilePanel filePanel, JProbeCore core){
		super(new GridBagLayout());
		this.initPanels(filePanel);
	}
	
	private void initPanels(GenomeFilePanel filePanel){
		m_FilePanel = filePanel;
		m_FilePanel.addStateListener(this);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.7;
		gbc.gridx = 0;
		this.add(m_FilePanel, gbc);
		m_ParamPanel = new ParameterPanel(m_Function.getDataParameters(), m_Function.getFieldParameters(), m_Core);
		m_ParamPanel.addStateListener(this);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 0.7;
		this.add(m_ParamPanel, gbc);
	}
	
	public void setRunAction(OnPress runAction){
		m_RunAction = runAction;
	}
	
	public void setCancelAction(OnPress cancelAction){
		m_CancelAction = cancelAction;
	}
	
	private File getGenomeFile(){
		return m_FilePanel.getGenomeFile();
	}
	
	private Data[] getDataArgs(){
		return m_ParamPanel.getDataArgs();
	}
	
	private Field[] getFieldArgs(){
		return m_ParamPanel.getFieldArgs();
	}
	
	private boolean isStateValid(){
		return m_FilePanel.isStateValid() && m_ParamPanel.isStateValid();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_Run){
			//do run stuff
			
			//finally
			m_RunAction.act();
		}
		if(e.getSource() == m_Cancel){
			m_CancelAction.act();
		}
	}
	
	@Override
	public void update(StateNotifier source) {
		m_Run.setEnabled(this.isStateValid());
	}

}
