package plugins.functions.gui.dialog.field;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;

import plugins.functions.gui.Constants;
import plugins.functions.gui.dialog.AbstractArgsPanel;
import plugins.functions.gui.dialog.field.editor.FieldEditor;
import plugins.functions.gui.dialog.field.editor.FieldEditorFactory;
import plugins.functions.gui.utils.StateListener;
import plugins.functions.gui.utils.StateNotifier;
import plugins.functions.gui.utils.ValidLabel;
import jprobe.services.data.Field;
import jprobe.services.function.FieldParameter;

public class FieldPanel extends AbstractArgsPanel<FieldParameter> implements StateListener{
	private static final long serialVersionUID = 1L;
	
	private FieldEditor[] m_FieldEditors;
	private Map<FieldParameter, FieldEditor> m_ParamToEditor;
	private boolean m_Valid;
	
	public FieldPanel(FieldParameter[] fieldParams){
		super(Constants.FIELDPANEL_HEADER);
		if(fieldParams == null){
			fieldParams = new FieldParameter[]{};
		}
		m_FieldEditors = new FieldEditor[fieldParams.length];
		m_ParamToEditor = new HashMap<FieldParameter, FieldEditor>();
		for(int i=0; i<m_FieldEditors.length; i++){
			m_FieldEditors[i] = FieldEditorFactory.newFieldEditor(fieldParams[i]);
			m_FieldEditors[i].addStateListener(this);
			m_ParamToEditor.put(fieldParams[i], m_FieldEditors[i]);
		}
		Collection<FieldParameter> args = new ArrayList<FieldParameter>();
		for(FieldParameter param : fieldParams){
			args.add(param);
		}
		this.setArgs(args);
		m_Valid = this.areEditorsValid();
	}
	
	public Field[] getFieldArgs(){
		Field[] args = new Field[m_FieldEditors.length];
		for(int i=0; i<args.length; i++){
			args[i] = m_FieldEditors[i].getValue();
		}
		return args;
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
		//get associated FieldEditor object from map
		FieldEditor editor = m_ParamToEditor.get(argument);
		//create valid icon label
		JLabel iconLabel = new ValidLabel(editor, Constants.CHECK_ICON, Constants.X_ICON);
		iconLabel.setToolTipText(argument.getDescription());
		row.add(iconLabel);
		//add FieldEditor component
		row.add(editor.getEditorComponent());
		//return the list
		return row;
	}

	@Override
	public boolean isStateValid() {
		return m_Valid;
	}
	
	private boolean areEditorsValid(){
		for(FieldEditor editor : m_FieldEditors){
			if(!editor.isStateValid()){
				return false;
			}
		}
		return true;
	}

	@Override
	public void update(StateNotifier source) {
		boolean newState = this.areEditorsValid();
		if(newState != m_Valid){
			m_Valid = newState;
			this.notifyListeners();
		}
	}

}
