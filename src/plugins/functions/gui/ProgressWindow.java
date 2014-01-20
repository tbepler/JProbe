package plugins.functions.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

import plugins.functions.gui.utils.OnPress;

public class ProgressWindow implements ActionListener{
	
	public static final long MILLISEC_BEFORE_DISPLAY = 2000;
	
	private JFrame frame;
	private JProgressBar progressBar;
	private JButton cancelButton;
	private OnPress onCancel;
	private boolean visible;
	
	public ProgressWindow(String title, int min, int max, final OnPress onCancel){
		this.onCancel = onCancel;
		frame = new JFrame(title);
		frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent event){
				onCancel.act();
				frame.dispose();
			}
		});
		frame.getContentPane().setLayout(new GridBagLayout());
		progressBar = new JProgressBar(min, max);
		progressBar.setStringPainted(true);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(25, 50, 15, 50);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		frame.add(progressBar, gbc);
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.SOUTHEAST;
		gbc.gridy = 2;
		frame.add(cancelButton, gbc);
		frame.pack();
		frame.setLocationRelativeTo(null);
		visible = false;
	}
	
	public void dispose(){
		frame.dispose();
	}
	
	private void setVisible(boolean visible){
		frame.setVisible(visible);
		this.visible = visible;
	}
	
	private long prevTime = -1;
	
	public void setValue(int value){
		if(!visible){
			if(prevTime < 0){
				prevTime = System.currentTimeMillis();
			}else{
				long curTime = System.currentTimeMillis();
				if(value - progressBar.getValue() == 0){
					return;
				}
				long approxTimeRequired = (progressBar.getMaximum()-progressBar.getValue())/(value-progressBar.getValue())
					*(curTime - prevTime);
				prevTime = curTime;
				if(approxTimeRequired > MILLISEC_BEFORE_DISPLAY){
					this.setVisible(true);
				}
			}
		}
		progressBar.setValue(value);
		if(value >= progressBar.getMaximum()){
			this.dispose();
			return;
		}
	}
	
	public int getValue(){
		return progressBar.getValue();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		onCancel.act();
		this.dispose();
	}
	
}
