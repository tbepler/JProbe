package chiptools.jprobe.function;

import java.awt.GridBagConstraints;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

public abstract class SequencesArg<P> extends ChiptoolsFileArg<P>{
	
	private final JPanel m_Panel = new JPanel();

	@SuppressWarnings("rawtypes")
	protected SequencesArg(Class<? extends SequencesArg> clazz, boolean optional) {
		super(clazz, optional);
		m_Panel.setLayout(new BoxLayout(m_Panel, BoxLayout.Y_AXIS));
	}
	
	abstract protected void process(P params, List<String> seqs);
	
	@Override
	protected void setFile(File f){
		
		super.setFile(f);
	}
	
	protected List<String> readFile(File f) throws Exception{
		List<String> seqs = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		try{
			String line;
			while((line = reader.readLine()) != null){
				if(!line.startsWith(">")){
					seqs.add(line);
				}
			}
			reader.close();
		}catch(Exception e){
			reader.close();
			throw e;
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
		return f.exists() && f.canRead();
	}

	@Override
	protected void process(P params, File f) {
		// TODO Auto-generated method stub
		
	}

}
