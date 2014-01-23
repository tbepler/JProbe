package plugins.functions.gui.dialog.field.editor;

import jprobe.services.data.DoubleField;
import jprobe.services.function.FieldParameter;

public class DoubleFieldSpinner extends AbstractFieldSpinner{
	private static final long serialVersionUID = 1L;
	
	public DoubleFieldSpinner(FieldParameter fieldParam, DoubleField doubleField){
		super(fieldParam, doubleField);
		this.setToolTipText(doubleField.getTooltip());
	}

}
