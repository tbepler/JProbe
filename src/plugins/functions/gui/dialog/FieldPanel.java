package plugins.functions.gui.dialog;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import plugins.functions.gui.utils.StateListener;
import plugins.functions.gui.utils.StateNotifier;
import jprobe.services.function.FieldParameter;

public class FieldPanel extends AbstractArgsPanel<FieldParameter> implements StateListener{
	private static final long serialVersionUID = 1L;
	
	private static final List<String> HEADER = generateHeader();
	
	private static List<String> generateHeader(){
		List<String> header = new ArrayList<String>();
		header.add("Name");
		header.add("Optional?");
		header.add("Valid");
		header.add("Field");
		return header;
	}
	
	@Override
	protected List<Component> generateRowComponents(FieldParameter argument) {
		List<Component> row = new ArrayList<Component>();
		//create name component
		JLabel name = new JLabel(argument.getName());
		name.setToolTipText(argument.getDescription());
		row.add(name);
		//create optional component
		JLabel optional = new JLabel(argument.isOptional() ? "Y" : "N");
		optional.setToolTipText(argument.getDescription());
		row.add(optional);
		
		
		return row;
	}

	@Override
	public boolean isStateValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(StateNotifier source) {
		// TODO Auto-generated method stub
		
	}

}
