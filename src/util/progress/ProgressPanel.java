package util.progress;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

public abstract class ProgressPanel extends JPanel implements ProgressListener{
	private static final long serialVersionUID = 1L;
	
	private static final Insets LABEL_INSETS = new Insets(25, 50, 5, 50);
	private static final Insets BAR_INSETS = new Insets(5, 50, 15, 50);
	private static final Dimension LABEL_DEFAULT_MIN = new Dimension(30, 20);
	
	private static final int MIN_BAR_WIDTH = 200;
	
	private JProgressBar m_ProgressBar;
	private JLabel m_Label;
	
	public ProgressPanel(){
		super(new GridBagLayout());
		m_ProgressBar = this.createProgressBar();
		m_Label = this.createLabel();
		this.layout(m_ProgressBar, m_Label);
		this.revalidate();
	}
	
	protected void layout(JProgressBar progressBar, JLabel label){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = LABEL_INSETS;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(label, gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.insets = BAR_INSETS;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(progressBar, gbc);
	}
	
	protected abstract void onUpdate(Object source, int progress, int maxProgress, String message, boolean indeterminant);
	protected abstract void onCanceled(Object source);
	protected abstract void onCompleted(Object source);
	
	@Override
	public void update(ProgressEvent e){
		switch(e.getType()){
		case UPDATE:
			if(e.getProgress() >= 0){
				this.setValue(e.getProgress());
				this.setProgressText(String.valueOf(e.getProgress())+"//"+String.valueOf(this.getMax()));
			}
			if(e.getMaxProgress() >= 0){
				this.setMax(e.getMaxProgress());
			}
			this.setIndeterminant(e.isIndeterminant());
			this.setProgressTextPainted(!this.isIndeterminant());
			if(e.getMessage() != null){
				this.setLabelText(e.getMessage());
			}
			this.onUpdate(e.getSource(), e.getProgress(), e.getMaxProgress(), e.getMessage(), e.isIndeterminant());
			m_ProgressBar.revalidate();
			m_Label.setMinimumSize(m_Label.getPreferredSize());
			m_Label.revalidate();
			break;
		case CANCELED:
			this.onCanceled(e.getSource());
			break;
		case COMPLETED:
			this.onCompleted(e.getSource());
			break;
		}
	}
	
	protected JProgressBar createProgressBar(){
		JProgressBar bar = new JProgressBar();
		bar.setStringPainted(true);
		Dimension min = bar.getMinimumSize();
		min.setSize(MIN_BAR_WIDTH, bar.getMinimumSize().getHeight());
		bar.setMinimumSize(min);
		return bar;
	}
	
	protected JLabel createLabel(){
		JLabel l = new JLabel();
		l.setHorizontalAlignment(SwingConstants.LEFT);
		l.setMinimumSize(LABEL_DEFAULT_MIN);
		return l;
	}
	
	protected JProgressBar getProgressBar(){
		return m_ProgressBar;
	}
	
	protected JLabel getLabel(){
		return m_Label;
	}
	
	protected void setLabelText(String text){
		m_Label.setText(text);
	}
	
	protected String getLabelText(){
		return m_Label.getText();
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
	
	protected void setIndeterminant(boolean indeterminant){
		m_ProgressBar.setIndeterminate(indeterminant);
	}
	
	protected boolean isIndeterminant(){
		return m_ProgressBar.isIndeterminate();
	}
	
	protected void setValue(int value){
		m_ProgressBar.setValue(value);
	}
	
	protected int getValue(){
		return m_ProgressBar.getValue();
	}
	
	protected void setMax(int max){
		m_ProgressBar.setMaximum(max);
	}
	
	protected int getMax(){
		return m_ProgressBar.getMaximum();
	}
	
	protected void setMin(int min){
		m_ProgressBar.setMinimum(min);
	}
	
	protected int getMin(){
		return m_ProgressBar.getMinimum();
	}
	
	
	
	
	
	
	
	
	
}
