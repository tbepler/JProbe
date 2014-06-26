package chiptools.jprobe.function;

import java.awt.GridBagConstraints;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import chiptools.Constants;
import chiptools.jprobe.ChiptoolsActivator;
import jprobe.services.ErrorHandler;
import jprobe.services.function.Function;

public abstract class SequencesArg<P> extends ChiptoolsFileArg<P>{
	
	private final JPanel m_Panel = new JPanel();

	@SuppressWarnings("rawtypes")
	protected SequencesArg(Class<? extends Function> funcClass, Class<? extends SequencesArg> clazz, boolean optional) {
		super(funcClass, clazz, optional);
		m_Panel.setLayout(new BoxLayout(m_Panel, BoxLayout.Y_AXIS));
	}
	
	abstract protected void process(P params, List<String> seqs);
	
	@Override
	protected void setFile(File f){
		m_Panel.removeAll();
		int count = 0;
		for(String s : readFile(f)){
			if(count >= Constants.SEQS_ARG_MAX_DISPLAY){
				m_Panel.add(new JLabel("..."));
				break;
			}
			m_Panel.add(new JLabel(s));
			++count;
		}
		super.setFile(f);
		SwingUtilities.getWindowAncestor(m_Panel).pack();
	}
	
	protected List<String> readFile(File f){
		List<String> seqs = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			try{
				String line;
				while((line = reader.readLine()) != null){
					if(!line.startsWith(">")){
						seqs.add(line);
					}
				}
			}catch(Exception e){
				ErrorHandler.getInstance().handleException(e, ChiptoolsActivator.getBundle());
			}
			reader.close();
		} catch (Exception e1) {
			ErrorHandler.getInstance().handleException(e1, ChiptoolsActivator.getBundle());
		}
		return seqs;
	}
	
	@Override
	public JComponent getComponent(){
		JComponent comp = super.getComponent();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		comp.add(m_Panel, gbc);
		return comp;
	}

	@Override
	protected boolean isValid(File f) {
		return f != null && f.exists() && f.canRead();
	}

	@Override
	protected void process(P params, File f) {
		List<String> seqs = this.readFile(f);
		this.process(params, seqs);
	}

}
