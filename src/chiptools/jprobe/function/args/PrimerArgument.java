package chiptools.jprobe.function.args;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import chiptools.Constants;
import chiptools.jprobe.ChiptoolsActivator;
import chiptools.jprobe.function.params.PrimerParam;
import jprobe.services.function.FileArgument;

public class PrimerArgument extends FileArgument<PrimerParam>{
	
	private final JLabel m_PrimerLabel = new JLabel();
	
	public PrimerArgument(boolean optional) {
		super(
				Constants.getName(PrimerArgument.class),
				Constants.getDescription(PrimerArgument.class),
				Constants.getCategory(PrimerArgument.class),
				Constants.getFlag(PrimerArgument.class),
				optional
				);
	}
	
	protected String parsePrimer(File f){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line;
			String primer = "";
			while((line = reader.readLine()) != null){
				if(line.startsWith(">") && !primer.equals("")){
					reader.close();
					return primer;
				}else if(!line.startsWith(">")){
					primer += line;
				}
			}
			reader.close();
			return primer;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	protected void setFile(File f){
		String primer = this.parsePrimer(f);
		if(primer != null && !primer.equals("")){
			m_PrimerLabel.setText(primer);
			SwingUtilities.getWindowAncestor(m_PrimerLabel).pack();
			super.setFile(f);
		}
	}
	
	@Override
	public JComponent getComponent(){
		JComponent comp = super.getComponent();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		comp.add(m_PrimerLabel, gbc);
		return comp;
	}

	@Override
	protected boolean isValid(File f) {
		return true;
	}

	@Override
	protected void process(PrimerParam params, File f) {
		String primer = this.parsePrimer(f);
		params.setPrimer(primer);
	}

	@Override
	protected Frame getParentFrame() {
		return ChiptoolsActivator.getGUIFrame();
	}

	@Override
	protected JFileChooser getJFileChooser() {
		return Constants.getChiptoolsFileChooser();
	}

}
