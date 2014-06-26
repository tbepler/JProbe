package chiptools.jprobe.function.args;

import java.awt.GridBagConstraints;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import chiptools.jprobe.function.ChiptoolsFileArg;
import chiptools.jprobe.function.params.PrimerParam;
import jprobe.services.function.Function;

public class PrimerArgument extends ChiptoolsFileArg<PrimerParam>{
	
	//instantiate lazily
	private JLabel m_PrimerLabel = null;
	
	public PrimerArgument(Function<?> parent, boolean optional) {
		super(
				parent.getClass(),
				PrimerArgument.class,
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
			if(m_PrimerLabel != null){
				m_PrimerLabel.setText(primer);
				SwingUtilities.getWindowAncestor(m_PrimerLabel).pack();
			}
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
		if(m_PrimerLabel == null){
			m_PrimerLabel = new JLabel();
		}
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

}
