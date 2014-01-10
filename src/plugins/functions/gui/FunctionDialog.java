package plugins.functions.gui;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JPanel;

import jprobe.services.function.Function;

public class FunctionDialog{
	
	private JDialog dialog;
	private OnPress close = new OnPress(){
		@Override
		public void act() {
			hide();
		}
	};
	
	public FunctionDialog(Frame owner, String title, boolean modal){
		dialog = new JDialog(owner, title, modal);
		dialog.setLocationRelativeTo(null);
	}
	
	public void display(FunctionPanel panel){
		dialog.setContentPane(panel);
		panel.setCancelAction(close);
		panel.setRunAction(close);
		dialog.pack();
		dialog.setVisible(true);
	}
	
	public void hide(){
		dialog.setVisible(false);
	}
	
}
