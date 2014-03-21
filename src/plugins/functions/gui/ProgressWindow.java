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
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import util.gui.OnPress;

public class ProgressWindow implements ActionListener{
	
	public static final int MILLISEC_BEFORE_DISPLAY = 500;
	
	private JFrame frame;
	private JProgressBar progressBar;
	private JButton cancelButton;
	private OnPress onCancel;
	private boolean visible;
	
	public ProgressWindow(String title, int min, int max, boolean indeterminant, final OnPress onCancel){
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
		progressBar.setStringPainted(!indeterminant);
		progressBar.setIndeterminate(indeterminant);
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
			if(progressBar.isIndeterminate()){
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
			}else{
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
