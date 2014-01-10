package plugins.functions.gui;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressWindow {
	
	private JFrame frame;
	private JProgressBar progressBar;
	private JPanel contentPane;
	private JButton cancelButton;
	private OnPress onCancel;
	
	public ProgressWindow(String title, int min, int max, OnPress onCancel){
		frame = new JFrame(title);
		progressBar = new JProgressBar(min, max);
		
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
	
}
