package jprobe.services.function;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import util.progress.ProgressListener;

public abstract class FileArgument<P> extends AbstractArgument<P> implements ActionListener{
	
	public static final String DEFAULT_BUTTON_TEXT = "Browse";
	public static final String PROTOTYPE_TEXT = "File name here";
	public static final String PROTOTYPE_VAL = "FILE";
	
	//instantiate the swing components lazily
	private JButton m_Button = null;
	private JTextField m_Text = null;
	private File m_Selected;

	protected FileArgument(String name, String tooltip, String category, Character shortFlag, boolean optional) {
		super(name, tooltip, category, shortFlag, PROTOTYPE_VAL, optional);
		//swing components instantiated lazily
	}
	
	private void initComponents(){
		m_Button = new JButton(this.getButtonText());
		m_Button.addActionListener(this);
		m_Text = new JTextField();
		//this part sets the size of the label using the prototype text
		m_Text.setText(this.getLablePrototypeText());
		Dimension d = m_Text.getPreferredSize();
		m_Text.setText("");
		m_Text.setMinimumSize(d);
		m_Text.setPreferredSize(d);
		//
		m_Text.setHorizontalAlignment(JTextField.RIGHT);
		//since the text field is just used to show the file path to the user, disable editing
		m_Text.setEditable(false);
	}
	
	protected abstract JFileChooser getJFileChooser();
	protected abstract boolean isValid(File f);
	protected abstract void process(P params, File f);
	protected abstract Frame getParentFrame();
	
	protected String getButtonText(){
		return DEFAULT_BUTTON_TEXT;
	}
	
	protected String getLablePrototypeText(){
		return PROTOTYPE_TEXT;
	}
	
	@Override
	public void parse(ProgressListener l, P params, String[] args){
		if(args.length < 1 || args.length > 1){
			throw new RuntimeException(this.getName()+" requires 1 argument. Received "+args.length);
		}
		File f = new File(args[0]);
		if(!this.isValid(f)){
			throw new RuntimeException("File \""+args[0]+"\" is not valid");
		}
		this.process(params, f);
	}

	protected void setFile(File f){
		m_Selected = f;
		if(m_Text != null){
			m_Text.setText(m_Selected.getPath());
		}
		this.notifyListeners();
	}

	@Override
	public boolean isValid() {
		return isValid(m_Selected);
	}

	@Override
	public JComponent getComponent() {
		if(m_Text == null || m_Button == null){
			this.initComponents();
		}
		JPanel comp = new JPanel(new GridBagLayout()){
			private static final long serialVersionUID = 1L;
			@Override
			public void setEnabled(boolean enabled){
				for(Component c : this.getComponents()){
					c.setEnabled(enabled);
				}
				super.setEnabled(enabled);
			}
		};
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 0, 2);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		comp.add(m_Text, gbc);
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 2, 0, 0);
		comp.add(m_Button, gbc);
		return comp;
	}

	@Override
	public void process(P params) {
		process(params, m_Selected);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_Button){
			JFileChooser chooser = this.getJFileChooser();
			int result = chooser.showOpenDialog(this.getParentFrame());
			if(result == JFileChooser.APPROVE_OPTION){
				this.setFile(chooser.getSelectedFile());
			}
		}
	}

}
