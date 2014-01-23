package plugins.functions.gui.dialog.field.editor;

import jprobe.services.data.IntegerField;
import jprobe.services.function.FieldParameter;

public class IntegerFieldSpinner extends AbstractFieldSpinner{
	private static final long serialVersionUID = 1L;
	
	public IntegerFieldSpinner(FieldParameter fieldParam, IntegerField intField){
		super(fieldParam, intField);
		this.setToolTipText(intField.getTooltip());
	}

}
