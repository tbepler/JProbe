package plugins.functions.gui.dialog.data;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;

import plugins.functions.gui.dialog.AbstractArgsPanel;
import plugins.functions.gui.utils.StateListener;
import plugins.functions.gui.utils.StateNotifier;
import plugins.functions.gui.utils.ValidLabel;
import plugins.functions.gui.Constants;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.function.DataParameter;

public class DataPanel extends AbstractArgsPanel<DataParameter> implements StateListener{
	private static final long serialVersionUID = 1L;
	
	private static final List<String> HEADER = generateHeader();
	
	private DataComboBox[] m_DataBoxes;
	private Map<DataParameter, DataComboBox> m_ParamToBox;
	
	public DataPanel(DataParameter[] dataParams, JProbeCore core){
		super(HEADER);
		if(dataParams == null){
			dataParams = new DataParameter[]{};
		}
		this.setArgs(init(dataParams, core));
	}
	
	private Collection<DataParameter> init(DataParameter[] dataParams, JProbeCore core){
		m_DataBoxes = new DataComboBox[dataParams.length];
		m_ParamToBox = new HashMap<DataParameter, DataComboBox>();
		for(int i=0; i<dataParams.length; i++){
			m_DataBoxes[i] = new DataComboBox(dataParams[i], core);
			m_DataBoxes[i].addStateListener(this);
			m_ParamToBox.put(dataParams[i], m_DataBoxes[i]);
		}
		Collection<DataParameter> args = new ArrayList<DataParameter>();
		for(DataParameter param : dataParams){
			args.add(param);
		}
		return args;
	}
	
	@Override
	public boolean isStateValid(){
		for(DataComboBox box : m_DataBoxes){
			if(!box.isStateValid()) return false;
		}
		return true;
	}
	
	public Data[] getDataArgs(){
		Data[] selectedData = new Data[m_DataBoxes.length];
		for(int i=0; i<selectedData.length; i++){
			selectedData[i] = m_DataBoxes[i].getSelectedData();
		}
		return selectedData;
	}
	
	private static List<String> generateHeader(){
		List<String> header = new ArrayList<String>();
		header.add("Name");
		header.add("Optional?");
		header.add("Valid");
		header.add("Data");
		return header;
	}
	
	@Override
	protected List<Component> generateRowComponents(DataParameter argument) {
		List<Component> row = new ArrayList<Component>();
		//create name component
		JLabel nameTag = new JLabel(argument.getName());
		nameTag.setToolTipText(argument.getDescription());
		row.add(nameTag);
		//create optional label component
		JLabel optionalTag = new JLabel(argument.isOptional() ? "Y" : "N");
		optionalTag.setToolTipText(argument.getDescription());
		row.add(optionalTag);
		//create label to show whether selection is valid or not
		JLabel validIconLabel = new ValidLabel(m_ParamToBox.get(argument), Constants.CHECK_ICON, Constants.X_ICON);
		validIconLabel.setToolTipText(argument.getDescription());
		row.add(validIconLabel);
		//add DataComboBox
		DataComboBox dataSelector = m_ParamToBox.get(argument);
		dataSelector.setToolTipText(argument.getDescription());
		row.add(dataSelector);
		//return list
		return row;
	}

	@Override
	public void update(StateNotifier source) {
		this.notifyListeners();
	}
	
	
	
}
