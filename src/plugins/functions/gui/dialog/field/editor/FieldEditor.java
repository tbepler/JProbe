package plugins.functions.gui.dialog.field.editor;

import java.awt.Component;

import util.gui.ValidStateNotifier;
import jprobe.services.data.Field;

public interface FieldEditor extends ValidStateNotifier{
	
	public Component getEditorComponent();
	public Field getValue();
	
}	
