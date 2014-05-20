package jprobe.services.function;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class FileArgument<P> extends AbstractArgument<P> implements ActionListener{
	
	public static final String DEFAULT_BUTTON_TEXT = "Choose File";
	public static final String PROTOTYPE_TEXT = "File name";
	
	private final JFileChooser m_FileChooser;
	private final JButton m_Button;
	private final JLabel m_Text;
	private File m_Selected;

	protected FileArgument(String name, String tooltip, String category, boolean optional, JFileChooser fileChooser) {
		super(name, tooltip, category, optional);
		m_FileChooser = fileChooser;
		m_Button = new JButton(this.getButtonText());
		m_Button.addActionListener(this);
		m_Text = new JLabel();
		//this part sets the size of the label using the prototype text
		m_Text.setText(this.getLablePrototypeText());
		Dimension d = m_Text.getPreferredSize();
		m_Text.setText("");
		m_Text.setMinimumSize(d);
		m_Text.setPreferredSize(d);
		//
		m_Text.setHorizontalAlignment(JLabel.TRAILING);
	}
	
	protected abstract boolean isValid(File f);
	protected abstract void process(P params, File f);
	
	protected String getButtonText(){
		return DEFAULT_BUTTON_TEXT;
	}
	
	protected String getLablePrototypeText(){
		return PROTOTYPE_TEXT;
	}
	

	private void setFile(File f){
		m_Selected = f;
		m_Text.setText(m_Selected.getPath());
		this.notifyListeners();
	}

	@Override
	public boolean isValid() {
		return isValid(m_Selected);
	}

	@Override
	public JComponent getComponent() {
		JPanel comp = new JPanel();
		comp.add(m_Text);
		comp.add(m_Button);
		return comp;
	}

	@Override
	public void process(P params) {
		process(params, m_Selected);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_Button){
			int result = m_FileChooser.showOpenDialog(null);
			if(result == JFileChooser.APPROVE_OPTION){
				this.setFile(m_FileChooser.getSelectedFile());
			}
		}
	}

}
