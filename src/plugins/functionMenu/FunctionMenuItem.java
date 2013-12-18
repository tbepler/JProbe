package plugins.functionMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import jprobe.services.Function;

public class FunctionMenuItem extends JMenuItem implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private Function function;
	
	public FunctionMenuItem(Function function){
		super(function.getName());
		this.function = function;
		this.setEnabled(true);
		this.setVisible(true);
		this.setToolTipText(function.getDescription());
		this.addActionListener(this);
	}
	
	private void doFunction(){
		//code for executing function here
		System.out.println(function.getName()+" clicked");
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		this.doFunction();
	}
	
}
