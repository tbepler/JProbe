package plugins.functions.gui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import jprobe.services.function.DataParameter;
import jprobe.services.function.FieldParameter;

public class ParameterPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private DataParameter[] m_DataParams;
	private FieldParameter[] m_FieldParams;
	
	public ParameterPanel(DataParameter[] dataParams, FieldParameter[] fieldParams){
		m_DataParams = dataParams;
		m_FieldParams = fieldParams;
	}
	
	FieldParameter[] getFieldParameters(){
		return m_FieldParams;
	}
	
	DataParameter[] getDataParameters(){
		return m_DataParams;
	}
	
}
