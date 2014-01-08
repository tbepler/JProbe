package plugins.functions.gui;

import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import jprobe.services.function.Function;

public class FunctionFrame extends JDialog{
	
	private Function function;
	
	public FunctionFrame(Window window, Function function){
		super(window, function.getName(), ModalityType.APPLICATION_MODAL);
		this.function = function;
		this.setAlwaysOnTop(true);
	}
	
	
}
