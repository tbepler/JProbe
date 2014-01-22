package plugins.functions.gui.dialog.field.editor;

import java.awt.Component;

import jprobe.services.data.Field;
import plugins.functions.gui.utils.ValidStateNotifier;

public interface FieldEditor extends ValidStateNotifier{
	
	public Component getEditorComponent();
	public Field getValue();
	
}	
