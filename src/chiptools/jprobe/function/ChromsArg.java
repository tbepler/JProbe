package chiptools.jprobe.function;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.util.Collection;
import java.util.LinkedHashSet;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import jprobe.services.function.Function;
import util.genome.Chromosome;

/**
 * This argument class reads a comma separated list of chromosomes entered by the user.
 * 
 * @author Tristan Bepler
 *
 * @param <P> - parameter class
 */
public abstract class ChromsArg<P> extends ChiptoolsTextArg<P> {
	
	private static final String SEP = ",";
	
	private final JPanel m_Panel = new JPanel(){
		private static final long serialVersionUID = 1L;

		@Override
		public void setEnabled(boolean enabled){
			for(Component c : this.getComponents()){
				c.setEnabled(enabled);
			}
			super.setEnabled(enabled);
		}
	};

	@SuppressWarnings("rawtypes")
	protected ChromsArg(Class<? extends Function> funcClass, Class<? extends ChromsArg> clazz, String defaultVal, boolean optional, String startValue) {
		super(funcClass, clazz, defaultVal, optional, startValue);
		m_Panel.setLayout(new BoxLayout(m_Panel, BoxLayout.Y_AXIS));
	}
	
	abstract protected void process(P params, Collection<Chromosome> chroms);
	
	@Override
	public JComponent getComponent(){
		JComponent comp = super.getComponent();
		JPanel parent = new JPanel(new GridBagLayout()){
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
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.7;
		parent.add(comp, gbc);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		parent.add(m_Panel, gbc);
		return parent;
	}
	
	protected Collection<Chromosome> parse(String s){
		Collection<Chromosome> chroms = new LinkedHashSet<Chromosome>();
		if(s == null){
			return chroms;
		}
		String[] tokens = s.split(SEP);
		for(String token : tokens){
			if(token != null && !token.equals("")){
				Chromosome chr = Chromosome.getInstance(token.trim());
				chroms.add(chr);
			}
		}
		return chroms;
	}
	
	@Override
	protected boolean isValid(String s) {
		Collection<Chromosome> chroms = this.parse(s);
		m_Panel.removeAll();
		for(Chromosome chr : chroms){
			JLabel l = new JLabel(chr.toString());
			l.setHorizontalAlignment(JLabel.LEFT);
			l.setAlignmentX(Component.LEFT_ALIGNMENT);
			m_Panel.add(l);
		}
		Window w = SwingUtilities.getWindowAncestor(m_Panel);
		if(w != null){
			w.pack();
		}
		return true;
	}

	@Override
	protected void process(P params, String s) {
		this.process(params, this.parse(s));
	}

}
