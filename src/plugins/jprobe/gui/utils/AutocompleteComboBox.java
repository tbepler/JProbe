package plugins.jprobe.gui.utils;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Collection;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicComboBoxEditor;

public class AutocompleteComboBox<E> extends JComboBox<E> implements DocumentListener, FocusListener, PopupMenuListener{
	private static final long serialVersionUID = 1L;

	public class AutocompleteEditor extends BasicComboBoxEditor{
		@Override
		protected JTextField createEditorComponent(){
			return m_TextField;
		}
	}
	
	private JTextField m_TextField;
	private Searchable<E> m_Items;
	
	public AutocompleteComboBox(Searchable<E> items) {
		super();
		m_Items = items;
		this.init();
	}
	
	private void init(){
		this.setEditable(true);
		m_TextField = new JTextField();
		this.setEditor(new AutocompleteEditor());
		m_TextField.getDocument().addDocumentListener(this);
		m_TextField.addFocusListener(this);
		this.addPopupMenuListener(this);
		for(E element : m_Items.search("")){
			this.addItem(element);
		}
	}
	
	protected void update(){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				setPopupVisible(true);
			}
		});
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		//do nothing
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		this.update();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		this.update();
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		if(m_TextField.getText().length() > 0){
			this.setPopupVisible(true);
		}
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		//do nothing
	}

	@Override
	public void popupMenuCanceled(PopupMenuEvent arg0) {
		//do nothing
	}

	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
		//do nothing
	}

	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
		Collection<E> results = m_Items.search(m_TextField.getText());
		Collection<E> sorted = m_Items.sort(results);
		setEditable(false);
		removeAllItems();
		for(E element : sorted){
			addItem(element);
		}
		setEditable(true);
	}
	
}
