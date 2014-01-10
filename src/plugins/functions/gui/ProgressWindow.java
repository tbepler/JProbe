package plugins.functions.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ProgressWindow implements ActionListener{
	
	private JFrame frame;
	private JProgressBar progressBar;
	private JButton cancelButton;
	private OnPress onCancel;
	
	public ProgressWindow(String title, int min, int max, OnPress onCancel){
		this.onCancel = onCancel;
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
		progressBar = new JProgressBar(min, max);
		progressBar.setStringPainted(true);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
		progressBar.setAlignmentY(Component.CENTER_ALIGNMENT);
		cancelButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		cancelButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		frame.add(progressBar);
		frame.add(cancelButton);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
	
	public void setVisible(boolean visible){
		frame.setVisible(visible);
	}
	
	public void setMax(int max){
		progressBar.setMaximum(max);
	}
	
	public int getMax(){
		return progressBar.getMaximum();
	}
	
	public void setMin(int min){
		progressBar.setMinimum(min);
	}
	
	public int getMin(){
		return progressBar.getMinimum();
	}
	
	public void setValue(int value){
		progressBar.setValue(value);
	}
	
	public int getValue(){
		return progressBar.getValue();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		onCancel.act();
		frame.setVisible(false);
	}
	
}
