package chiptools.jprobe.function.mutationprofiler;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import jprobe.services.ErrorHandler;
import jprobe.services.function.Function;
import chiptools.jprobe.ChiptoolsActivator;
import chiptools.jprobe.function.ChiptoolsFileArg;

public class CompareSeqsArg extends ChiptoolsFileArg<MutationProfilerParams>{
	
	private final int m_NumSeqs;
	private final JPanel m_Panel = new JPanel(new GridBagLayout());
	
	public CompareSeqsArg(Function<?> parent, int numSeqs, boolean optional) {
		super(parent.getClass(), CompareSeqsArg.class, optional);
		m_NumSeqs = numSeqs;
	}
	
	@Override
	protected void setFile(File f){
		m_Panel.removeAll();
		m_Panel.setPreferredSize(null);
		Dimension dim = m_Panel.getParent().getPreferredSize();
		int count = 0;
		Map<String, String> seqs = this.parse(f, m_NumSeqs);
		for(String s : seqs.keySet()){
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridy = count;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.gridx = 0;
			m_Panel.add(new JLabel(s + ":"), gbc);
			gbc.gridx = 1;
			gbc.weightx = 0.7;
			m_Panel.add(new JLabel(seqs.get(s)), gbc);
			++count;
		}
		m_Panel.setPreferredSize(new Dimension(dim.width, m_Panel.getPreferredSize().height));
		super.setFile(f);
		SwingUtilities.getWindowAncestor(m_Panel).pack();
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
		return parse(f, m_NumSeqs).size() == m_NumSeqs;
	}
	
	protected Map<String, String> parse(File f, int max){
		Map<String, String> seqMap = new LinkedHashMap<String, String>();
		if(f == null){
			return seqMap;
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String name = null;
			String seq = null;
			String line;
			try {
				while((line = reader.readLine()) != null && seqMap.size() < max){
					if(line.startsWith(">")){ //this is a fasta format name entry
						if(seq != null){ //there is previous sequence, so put it into the map
							seqMap.put(name, seq);
						}
						name = line.substring(1);
						seq = null;
					}else{ //read sequence
						if(name == null){ //no name specified, so use a default name for this line
							seqMap.put("seq"+seqMap.size(), line);
						}else{ //there is a fasta name for this seq, so append this line to the sequence
							if(seq != null){
								seq += line;
							}else{
								seq = line;
							}
						}
					}
				}
				//write the last sequence into the map if the limit is not exceeded
				if(seq != null && name != null && seqMap.size() < max){
					seqMap.put(name, seq);
				}
				reader.close();
			} catch (IOException e) {
				ErrorHandler.getInstance().handleException(e, ChiptoolsActivator.getBundle());
			}
		} catch (FileNotFoundException e) {
			ErrorHandler.getInstance().handleException(e, ChiptoolsActivator.getBundle());
		}
		return seqMap;
	}

	@Override
	protected void process(MutationProfilerParams params, File f) {
		Map<String, String> seqs = parse(f,m_NumSeqs);
		int count = 0;
		for(String name : seqs.keySet()){
			switch(count){
			case 0:
				params.seq1Name = name;
				params.seq1 = seqs.get(name);
				break;
			case 1:
				params.seq2Name = name;
				params.seq2 = seqs.get(name);
				break;
			}
			++count;
		}
	}

}
