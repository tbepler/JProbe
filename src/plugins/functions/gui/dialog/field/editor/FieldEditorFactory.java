package plugins.functions.gui.dialog.field.editor;

import jprobe.services.data.DoubleField;
import jprobe.services.data.IntegerField;
import jprobe.services.function.FieldParameter;

public class FieldEditorFactory {
	
	public static FieldEditor newFieldEditor(FieldParameter fieldParam){
		if(fieldParam.getType() instanceof IntegerField){
			IntegerField integer = (IntegerField) fieldParam.getType();
			return new IntegerFieldSpinner(fieldParam, integer);
		}
		if(fieldParam.getType() instanceof DoubleField){
			DoubleField doubleField = (DoubleField) fieldParam.getType();
			return new DoubleFieldSpinner(fieldParam, doubleField);
		}
		return new StringFieldEditor(fieldParam);
	}
	
}
