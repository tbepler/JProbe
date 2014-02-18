package plugins.genome.gui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import plugins.genome.Constants;
import util.gui.StateListener;
import util.gui.ValidStateNotifier;

public class GenomeFilePanel extends JPanel implements ValidStateNotifier, ActionListener{
	private static final long serialVersionUID = 1L;
	
	private final Collection<StateListener> m_Listeners = new HashSet<StateListener>();
	
	private final JFileChooser m_Chooser = new JFileChooser(Constants.GENOME_FILE_DIR_DEFAULT);
	private final JButton m_FileButton = new JButton(Constants.GENOME_FILE_BUTTON_TEXT);
	private final JLabel m_FileText = new JLabel();
	private final JLabel m_ValidIcon = new JLabel();
	private final JPanel m_InnerPanel = new JPanel();
	
	private File m_GenomeFile = null;
	
	public GenomeFilePanel(){
		super();
		this.setBorder(Constants.GENOME_FILE_PANEL_BORDER);
		m_InnerPanel.setLayout(new BoxLayout(m_InnerPanel, BoxLayout.X_AXIS));
		this.add(m_InnerPanel);
		m_InnerPanel.add(Box.createHorizontalGlue());
		m_InnerPanel.add(m_FileText);
		m_InnerPanel.add(m_ValidIcon);
		m_InnerPanel.add(m_FileButton);
		m_FileButton.addActionListener(this);
		this.updateFileText();
		this.updateValidLabel();
	}
	
	public File getGenomeFile(){
		return m_GenomeFile;
	}
	
	@Override
	public void addStateListener(StateListener l) {
		m_Listeners.add(l);
	}

	@Override
	public void removeStateListener(StateListener l) {
		m_Listeners.remove(l);
	}
	
	protected void notifyListeners(){
		for(StateListener l : m_Listeners){
			l.update(this);
		}
	}

	@Override
	public boolean isStateValid() {
		return m_GenomeFile != null && m_GenomeFile.canRead();
	}
	
	private void updateValidLabel(){
		ImageIcon icon = this.isStateValid() ? Constants.CHECK_ICON : Constants.X_ICON;
		m_ValidIcon.setIcon(icon);
		m_ValidIcon.revalidate();
	}
	
	private void updateFileText(){
		String text = m_GenomeFile != null ? m_GenomeFile.getName() : "";
		m_FileText.setText(text);
		m_FileText.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_FileButton){
			int result = m_Chooser.showOpenDialog(SwingUtilities.getWindowAncestor(this));
			if(result == JFileChooser.APPROVE_OPTION){
				m_GenomeFile = m_Chooser.getSelectedFile();
				this.updateValidLabel();
				this.updateFileText();
				this.notifyListeners();
			}
		}
	}

}
