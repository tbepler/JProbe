package plugins.functions.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import util.gui.OnPress;

public class ProgressWindow implements ActionListener{
	
	public static final String PROTOTYPE_TEXT = "Some long update text goes here.";
	
	public static final int MILLISEC_BEFORE_DISPLAY = 500;
	
	private JFrame m_Frame;
	private JLabel m_TextLabel;
	private JProgressBar m_ProgressBar;
	private JButton m_CancelButton;
	private OnPress m_OnCancel;
	private boolean m_Visible;
	
	public ProgressWindow(String title, int min, int max, boolean indeterminant, final OnPress onCancel){
		this.m_OnCancel = onCancel;
		m_Frame = new JFrame(title);
		m_Frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent event){
				onCancel.act();
				m_Frame.dispose();
			}
		});
		m_Frame.getContentPane().setLayout(new GridBagLayout());
		
		m_TextLabel = new JLabel();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(25, 50, 5, 50);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.SOUTH;
		m_Frame.add(m_TextLabel, gbc);
		
		m_ProgressBar = new JProgressBar(min, max);
		m_ProgressBar.setStringPainted(!indeterminant);
		m_ProgressBar.setIndeterminate(indeterminant);
		gbc.insets = new Insets(0, 50, 15, 50);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		m_Frame.add(m_ProgressBar, gbc);
		
		m_TextLabel.setText(PROTOTYPE_TEXT);
		Dimension size = m_TextLabel.getPreferredSize();
		m_TextLabel.setText("");
		m_TextLabel.setPreferredSize(size);
		m_TextLabel.setMinimumSize(size);
		m_ProgressBar.setMinimumSize(size);
		
		m_CancelButton = new JButton("Cancel");
		m_CancelButton.addActionListener(this);
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.SOUTHEAST;
		gbc.gridx = GridBagConstraints.RELATIVE;
		gbc.gridy = 2;
		m_Frame.add(m_CancelButton, gbc);
		
		m_Frame.pack();
		m_Frame.setLocationRelativeTo(null);
		
		m_Visible = false;
		
		Timer t = new Timer(MILLISEC_BEFORE_DISPLAY, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SwingUtilities.invokeLater(new Runnable(){
					@Override
					public void run() {
						setVisible(true);
					}
				});
			}
		});
		t.setRepeats(false);
		t.start();
	}
	
	public void dispose(){
		m_Frame.dispose();
		m_Frame = null;
	}
	
	private void setVisible(boolean visible){
		if(m_Frame != null){
			m_Frame.setVisible(visible);
			this.m_Visible = visible;
		}
	}
	
	private long prevTime = -1;
	
	public void setValue(int value){
		if(!m_Visible){
			if(prevTime < 0){
				prevTime = System.currentTimeMillis();
			}else{
				long curTime = System.currentTimeMillis();
				if(value - m_ProgressBar.getValue() == 0){
					this.setVisible(true);
					return;
				}
				long approxTimeRequired = (m_ProgressBar.getMaximum()-m_ProgressBar.getValue())/(value-m_ProgressBar.getValue())
						*(curTime - prevTime);
				prevTime = curTime;
				if(approxTimeRequired > MILLISEC_BEFORE_DISPLAY){
					this.setVisible(true);
				}
			}
		}
		m_ProgressBar.setValue(value);
	}
	
	public void setMaxValue(int value){
		m_ProgressBar.setMaximum(value);
	}
	
	public void setText(String text){
		m_TextLabel.setText(text);
	}
	
	public void setIndeterminate(boolean indeterminate){
		m_ProgressBar.setStringPainted(!indeterminate);
		m_ProgressBar.setIndeterminate(indeterminate);
	}
	
	public int getValue(){
		return m_ProgressBar.getValue();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		m_OnCancel.act();
		this.dispose();
	}
	
}
