package plugins.functions.gui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import plugins.functions.gui.Constants;
import util.Observer;
import util.Subject;
import util.gui.ValidLabel;
import jprobe.services.function.Argument;
import jprobe.services.function.ArgumentListener;

public class ArgumentPanel<T> extends JPanel implements Subject<Boolean>, ArgumentListener, ActionListener{
	private static final long serialVersionUID = 1L;
	
	private final Collection<Observer<Boolean>> m_Obs = new HashSet<Observer<Boolean>>();
	
	private final Argument<? super T> m_Arg;
	private final AbstractButton m_Optional;
	
	public ArgumentPanel(Argument<? super T> arg){
		super();
		m_Arg = arg;
		this.add(this.nameComponent(arg));
		m_Optional = this.optionalButton(arg);
		m_Optional.addActionListener(this);
		this.add(m_Optional);
		this.add(this.validComponent(arg));
		this.add(this.argComponent(arg));
		this.setBorder(this.makeBorder());
	}
	
	public boolean isArgValid(){
		return m_Arg.isValid() || !m_Optional.isSelected();
	}
	
	public void process(T params){
		if(m_Optional.isSelected()){
			m_Arg.process(params);
		}
	}
	
	protected Border makeBorder(){
		return BorderFactory.createBevelBorder(BevelBorder.LOWERED);
	}
	
	protected JComponent nameComponent(Argument<? super T> arg){
		JLabel name = new JLabel(arg.getName());
		return name;
	}
	
	protected AbstractButton optionalButton(Argument<? super T> arg){
		AbstractButton button = new JCheckBox();
		if(!arg.isOptional()){
			button.setSelected(true);
			button.setEnabled(false);
		}
		return button;
	}
	
	protected JComponent validComponent(Argument<? super T> arg){
		ValidLabel label = new ValidLabel(this, this.isArgValid(), Constants.CHECK_ICON, Constants.X_ICON);
		return label;
	}
	
	protected JComponent argComponent(Argument<? super T> arg){
		return arg.getComponent();
	}

	@Override
	public void update(Argument<?> arg, boolean valid) {
		this.notifyObservers();
	}
	
	protected void notifyObservers(){
		for(Observer<Boolean> obs : m_Obs){
			obs.update(this, this.isArgValid());
		}
	}

	@Override
	public void register(Observer<Boolean> obs) {
		m_Obs.add(obs);
	}

	@Override
	public void unregister(Observer<Boolean> obs) {
		m_Obs.remove(obs);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_Optional){
			this.notifyObservers();
		}
	}


}
