package jprobe.services.function.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.gui.ChangeNotifier;

public class FileArgComponent extends JPanel implements ChangeNotifier, ActionListener{
	private static final long serialVersionUID = 1L;
	
	private final Collection<ChangeListener> m_Listeners = new HashSet<ChangeListener>();
	
	private final JFileChooser m_Chooser;
	private final JButton m_Button;
	private final JTextField m_Text;
	
	private File m_File = null;
	
	public FileArgComponent(JFileChooser fileChooser, String buttonText, String labelPrototypeText){
		super(new GridBagLayout());
		
		m_Chooser = fileChooser;
		m_Button = this.createButton(buttonText);
		m_Text = this.createTextField(labelPrototypeText);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 0, 2);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		this.add(m_Text, gbc);
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 2, 0, 0);
		this.add(m_Button, gbc);
	}
	
	protected JButton createButton(String text){
		JButton button = new JButton(text);
		button.addActionListener(this);
		return button;
	}
	
	protected JTextField createTextField(String labelPrototypeText){
		JTextField field = new JTextField();
		//this part sets the size of the label using the prototype text
		field.setText(labelPrototypeText);
		Dimension d = field.getPreferredSize();
		field.setText("");
		field.setMinimumSize(d);
		field.setPreferredSize(d);
		//
		field.setHorizontalAlignment(JTextField.RIGHT);
		//since the text field is just used to show the file path to the user, disable editing
		field.setEditable(false);
		return field;
	}

	protected void setFile(File f){
		m_File = f;
		m_Text.setText(m_File.getPath());
		this.fireChanged();
	}
	
	public File getFile(){
		return m_File;
	}
	
	@Override
	public void setEnabled(boolean enabled){
		for(Component c : this.getComponents()){
			c.setEnabled(enabled);
		}
		super.setEnabled(enabled);
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
		if(e.getSource() == m_Button){
			int result = m_Chooser.showOpenDialog(SwingUtilities.getWindowAncestor(this));
			if(result == JFileChooser.APPROVE_OPTION){
				this.setFile(m_Chooser.getSelectedFile());
			}
		}
	}
	
}
