package util.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Document;

public class ChangeNotifierTextField extends JTextField implements ChangeNotifier, ActionListener{
	private static final long serialVersionUID = 1L;
	
	private final Collection<ChangeListener> m_Listeners = new HashSet<ChangeListener>();

	public ChangeNotifierTextField() {
		super();
		this.addActionListener(this);
	}

	public ChangeNotifierTextField(Document doc, String text, int columns) {
		super(doc, text, columns);
		this.addActionListener(this);
	}

	public ChangeNotifierTextField(int columns) {
		super(columns);
		this.addActionListener(this);
	}

	public ChangeNotifierTextField(String text, int columns) {
		super(text, columns);
		this.addActionListener(this);
	}

	public ChangeNotifierTextField(String text) {
		super(text);
		this.addActionListener(this);
	}
	
	protected void fireChanged(){
		ChangeEvent event = new ChangeEvent(this);
		for(ChangeListener l : m_Listeners){
			l.stateChanged(event);
		}
	}

	@Override
	public void addChangeListener(ChangeListener l) {
		m_Listeners.add(l);
	}

	@Override
	public void removeChangeListener(ChangeListener l) {
		m_Listeners.remove(l);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this){
			this.fireChanged();
		}
	}

}
