package plugins.functions.gui.dialog;

import java.util.*;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;

import jprobe.services.data.Field;
import jprobe.services.function.FieldParameter;
import plugins.functions.gui.utils.StateListener;
import plugins.functions.gui.utils.ValidStateNotifier;

public class StringFieldEditor extends JTextField implements ValidStateNotifier, DocumentListener{
	private static final long serialVersionUID = 1L;
	
	private FieldParameter m_FieldParam;
	private Field m_Value;
	private boolean m_Valid;
	private Collection<StateListener> m_Listeners = new HashSet<StateListener>();
	
	public StringFieldEditor(FieldParameter fieldParam){
		super();
		m_FieldParam = fieldParam;
		m_Value = m_FieldParam.getType();
		this.setText(m_Value.asString());
	}
	
	@Override
	protected Document createDefaultModel(){
		return new PlainDocument(){
			private static final long serialVersionUID = 1L;
			@Override
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException{
				if(str != null){
					for(int i=0; i<str.length(); i++){
						if(!m_FieldParam.getType().isCharacterAllowed(str.charAt(i))){
							return;
						}
					}
					super.insertString(offs, str, a);
				}
			}
		};
	}

	@Override
	public boolean isStateValid() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private boolean isTextValid(){
		return m_FieldParam.getType().isValid(this.getText());
	}
	
	protected void notifyListeners(){
		for(StateListener l : m_Listeners){
			l.update(this);
		}
	}
	
	@Override
	public void addStateListener(StateListener l) {
		m_Listeners.add(l);
	}

	@Override
	public void removeStateListener(StateListener l) {
		m_Listeners.remove(l);
	}
	
	private void textChanged(){
		if(isTextValid()){
			
		}
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
