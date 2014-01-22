package plugins.functions.gui.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.util.*;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;

import jprobe.services.ErrorHandler;
import jprobe.services.data.Field;
import jprobe.services.function.FieldParameter;
import plugins.functions.gui.Activator;
import plugins.functions.gui.utils.StateListener;

public class StringFieldEditor extends JTextField implements FieldEditor, DocumentListener{
	private static final long serialVersionUID = 1L;
	
	private static final Dimension PREF_SIZE = new Dimension(50, 10);
	private static final int NUM_COLS = 10;
	
	private FieldParameter m_FieldParam;
	private Field m_Value;
	private boolean m_Valid;
	private Collection<StateListener> m_Listeners = new HashSet<StateListener>();
	
	public StringFieldEditor(FieldParameter fieldParam){
		super();
		m_FieldParam = fieldParam;
		m_Value = m_FieldParam.getType();
		this.setText(m_Value.asString());
		m_Valid = m_FieldParam.isValid(m_Value);
		this.getDocument().addDocumentListener(this);
		this.setToolTipText(m_Value.getTooltip());
		this.setColumns(NUM_COLS);
		this.setHorizontalAlignment(JTextField.LEFT);
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
		return m_Valid;
	}
	
	@Override
	public Component getEditorComponent() {
		return this;
	}

	@Override
	public Field getValue() {
		return m_Value;
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
	
	private boolean isTextValid(){
		return m_FieldParam.getType().isValid(this.getText());
	}
	
	private void textChanged(){
		if(isTextValid()){
			boolean newState = m_Valid;
			try {
				m_Value = m_FieldParam.getType().parseString(this.getText());
				newState = m_FieldParam.isValid(m_Value);
			} catch (Exception e) {
				newState = false;
				ErrorHandler.getInstance().handleException(e, Activator.getBundle());
			}
			if(newState != m_Valid){
				m_Valid = newState;
				this.notifyListeners();
			}
		}
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		this.textChanged();
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		this.textChanged();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		this.textChanged();
	}



}
