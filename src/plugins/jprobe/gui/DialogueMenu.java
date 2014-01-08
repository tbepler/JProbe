package plugins.jprobe.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class DialogueMenu extends JMenu implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private JDialog dialog;
	private JMenuItem button;
	
	public DialogueMenu(String name, JDialog dialog){
		super(name);
		this.dialog = dialog;
		button = new JMenuItem(name);
		button.addActionListener(this);
		button.setEnabled(true);
		button.setVisible(true);
		this.add(button);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dialog.setVisible(true);
	}
	
}
