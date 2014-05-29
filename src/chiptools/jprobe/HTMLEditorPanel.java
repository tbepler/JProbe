package chiptools.jprobe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import util.Observer;
import util.Subject;

public class HTMLEditorPanel extends JPanel implements ActionListener, Subject<HTMLEditorPanel.Event>{
	private static final long serialVersionUID = 1L;
	
	public enum Event{
		START,
		END;
	}
	
	private static final String SAMPLE_TEXT = "Example";
	
	private final Collection<Observer<Event>> m_Obs = new HashSet<Observer<Event>>();
	
	private final JTextField m_StartHTML = new JTextField();
	private final JTextField m_EndHTML = new JTextField();
	private final JLabel m_Sample = new JLabel(SAMPLE_TEXT);
	
	public HTMLEditorPanel(String name){
		super();
		this.add(new JLabel(name));
		this.add(m_StartHTML);
		m_StartHTML.addActionListener(this);
		this.add(m_EndHTML);
		m_EndHTML.addActionListener(this);
		this.add(m_Sample);
	}
	
	public String getStartHTML(){
		return m_StartHTML.getText();
	}
	
	public void setStartHTML(String html){
		m_StartHTML.setText(html);
		this.updateSampleText();
		this.notifyObs(Event.START);
	}
	
	public String getEndHTML(){
		return m_EndHTML.getText();
	}
	
	public void setEndHTML(String html){
		m_EndHTML.setText(html);
		this.updateSampleText();
		this.notifyObs(Event.END);
	}

	private void updateSampleText(){
		m_Sample.setText("<html>" + m_StartHTML.getText() + SAMPLE_TEXT + m_EndHTML.getText() + "</html>");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.updateSampleText();
		if(e.getSource() == m_StartHTML){
			this.notifyObs(Event.START);
		}
		if(e.getSource() == m_EndHTML){
			this.notifyObs(Event.END);
		}
	}
	
	protected void notifyObs(Event e){
		for(Observer<Event> obs : m_Obs){
			obs.update(this, e);
		}
	}

	@Override
	public void register(Observer<Event> obs) {
		m_Obs.add(obs);
	}

	@Override
	public void unregister(Observer<Event> obs) {
		m_Obs.remove(obs);
	}
	
}
