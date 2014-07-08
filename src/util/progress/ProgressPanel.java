package util.progress;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

public class ProgressPanel extends JPanel implements ProgressListener{
	private static final long serialVersionUID = 1L;
	
	private static final Insets LABEL_INSETS = new Insets(25, 50, 5, 50);
	private static final Insets BAR_INSETS = new Insets(5, 50, 15, 50);
	private static final Dimension LABEL_DEFAULT_MIN = new Dimension(30, 20);
	
	private static final int MIN_BAR_WIDTH = 200;
	
	private JProgressBar m_ProgressBar;
	private JTextArea m_TextArea;
	
	public ProgressPanel(){
		super(new GridBagLayout());
		m_ProgressBar = this.createProgressBar();
		m_TextArea = this.createTextArea();
		this.layout(m_ProgressBar, m_TextArea);
		this.invalidate();
		this.revalidate();
	}
	
	protected void layout(JProgressBar progressBar, JTextArea textArea){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = LABEL_INSETS;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		Dimension max = textArea.getMaximumSize();
		max.width = progressBar.getPreferredSize().width;
		textArea.setMaximumSize(max);
		this.add(textArea, gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.insets = BAR_INSETS;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(progressBar, gbc);
	}
	
	/*
	@Override
	public void update(ProgressEvent e){
		switch(e.getType()){
		case CANCELED:
			this.onCanceled(e.getSource());
			break;
		case COMPLETED:
			this.onCompleted(e.getSource());
			break;
		case ERROR:
			this.setText("Error: "+e.getThrowable().getMessage());
			break;
		case INDETERMINANT_UPDATE:
			this.setIndeterminant(e.isIndeterminant());
			this.onUpdate(e.getSource(), this.getValue(), this.getMax(), this.getText(), e.isIndeterminant());
			break;
		case INFO:
			this.setText(e.getMessage());
			break;
		case MESSAGE_UPDATE:
			this.setText(e.getMessage());
			this.onUpdate(e.getSource(), this.getValue(), this.getMax(), e.getMessage(), this.isIndeterminant());
			break;
		case PROGRESS_UPDATE:
			this.setValue(e.getProgress());
			this.setMax(e.getMaxProgress());
			this.setIndeterminant(false);
			this.onUpdate(e.getSource(), e.getProgress(), e.getMaxProgress(), this.getText(), this.isIndeterminant());
			m_ProgressBar.invalidate();
			m_ProgressBar.revalidate();
			break;
		case MESSAGE_AND_PROGRESS_UPDATE:
			this.setText(e.getMessage());
			this.setValue(e.getProgress());
			this.setMax(e.getMaxProgress());
			this.setIndeterminant(false);
			this.onUpdate(e.getSource(), e.getProgress(), e.getMaxProgress(), e.getMessage(), this.isIndeterminant());
			m_ProgressBar.invalidate();
			m_ProgressBar.revalidate();
			break;
		default:
			break;
		}
	}
	
	*/
	
	protected JProgressBar createProgressBar(){
		JProgressBar bar = new JProgressBar();
		Dimension min = bar.getPreferredSize();
		min.width = MIN_BAR_WIDTH;
		bar.setMinimumSize(min);
		bar.setMaximum(Progress.MAX_PERCENT);
		bar.setMinimum(Progress.MIN_PERCENT);
		bar.setIndeterminate(true);
		return bar;
	}
	
	protected JTextArea createTextArea(){
		JTextArea l = new JTextArea();
		l.setMinimumSize(LABEL_DEFAULT_MIN);
		l.setLineWrap(true);
		l.setWrapStyleWord(true);
		l.setEditable(false);
		l.setOpaque(false);
		l.setBorder(null);
		l.setFocusable(false);
		return l;
	}
	
	protected JProgressBar getProgressBar(){
		return m_ProgressBar;
	}
	
	protected JTextArea getTextArea(){
		return m_TextArea;
	}
	
	protected void setText(String text){
		m_TextArea.setText(text);
		m_TextArea.invalidate();
		m_TextArea.revalidate();
	}
	
	protected String getText(){
		return m_TextArea.getText();
	}
	
	protected void setProgressText(String text){
		m_ProgressBar.setString(text);
	}
	
	protected String getProgressText(){
		return m_ProgressBar.getString();
	}
	
	protected void setProgressTextPainted(boolean painted){
		m_ProgressBar.setStringPainted(painted);
	}
	
	protected boolean isProgressTextPainted(){
		return m_ProgressBar.isStringPainted();
	}
	
	protected void setIndeterminate(boolean indeterminant){
		this.setProgressTextPainted(!indeterminant);
		m_ProgressBar.setIndeterminate(indeterminant);
	}

	protected boolean isIndeterminate(){
		return m_ProgressBar.isIndeterminate();
	}
	
	protected void setValue(int value){
		m_ProgressBar.setValue(value);
	}
	
	protected int getValue(){
		return m_ProgressBar.getValue();
	}
	
	protected void setMin(int min){
		m_ProgressBar.setMinimum(min);
	}
	
	protected int getMin(){
		return m_ProgressBar.getMinimum();
	}

	@Override
	public void onStart(String message) {
		if(message != null){
			this.setText(message);
		}
	}

	@Override
	public void onCompletion(String message) {
		if(message != null){
			this.setText(message);
		}
	}
	
	@Override
	public void onError(Throwable t){
		if(t != null){
			this.setText("Error: "+t.getMessage());
		}
	}

	@Override
	public void progressUpdate(int percent, String message) {
		if(Progress.isIndeterminate(percent)){
			this.setIndeterminate(true);
		}else{
			this.setIndeterminate(false);
			this.setValue(percent);
		}
		if(message != null){
			this.setText(message);
		}
		m_ProgressBar.invalidate();
		m_ProgressBar.revalidate();
	}
	
	
	
	
	
	
	
	
	
}
