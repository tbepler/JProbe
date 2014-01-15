package plugins.functions.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import jprobe.services.function.DataParameter;
import jprobe.services.function.FieldParameter;

public class ParameterPanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private DataParameter[] m_DataParams;
	private FieldParameter[] m_FieldParams;
	private FunctionPanel m_FunctionPanel;
	
	public ParameterPanel(DataParameter[] dataParams, FieldParameter[] fieldParams, FunctionPanel functionPanel){
		m_DataParams = dataParams;
		m_FieldParams = fieldParams;
		m_FunctionPanel = functionPanel;
	}
	
	FieldParameter[] getFieldParameters(){
		return m_FieldParams;
	}
	
	DataParameter[] getDataParameters(){
		return m_DataParams;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
