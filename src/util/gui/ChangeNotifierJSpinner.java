package util.gui;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

public class ChangeNotifierJSpinner extends JSpinner implements ChangeNotifier{
	private static final long serialVersionUID = 1L;

	public ChangeNotifierJSpinner() {
		super();
	}

	public ChangeNotifierJSpinner(SpinnerModel model) {
		super(model);
	}

}
