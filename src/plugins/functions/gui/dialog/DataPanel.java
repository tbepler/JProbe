package plugins.functions.gui.dialog;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JLabel;

import plugins.functions.gui.utils.StateNotifier;
import jprobe.services.function.DataParameter;

public class DataPanel extends AbstractArgsPanel<DataParameter> implements StateNotifier{
	private static final long serialVersionUID = 1L;
	
	private static final List<String> HEADER = generateHeader();
	
	public DataPanel(DataParameter[] dataParams){
		super();
		this.setHeaders(HEADER);
		Collection<DataParameter> args = new ArrayList<DataParameter>();
		for(DataParameter param : dataParams){
			args.add(param);
		}
		this.setArgs(args);
	}
	
	private static List<String> generateHeader(){
		List<String> header = new ArrayList<String>();
		header.add("Name");
		header.add("Optional?");
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
		//create data selection component
		
		//return list
		return row;
	}
	
	
	
}
