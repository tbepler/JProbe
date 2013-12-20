package plugins.dataviewer.gui.table;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.text.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.text.DocumentFilter;

import jprobe.services.DataField;

public class DataFieldFormatter extends AbstractFormatter{
	private static final long serialVersionUID = 1L;
	
	private DataField field;
	private Collection<Character> validChars;
	private DocumentFilter filter;
	
	public DataFieldFormatter(DataField field){
		super();
		this.field = field;
		validChars = new HashSet<Character>();
		for(char c : field.getValidChars()){
			validChars.add(c);
		}
		filter = new DocumentFilter(){
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException{
				if(isValidString(string)){
					super.insertString(fb, offset, string, attr);
				}
			}
			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException{
				if(isValidString(text)){
					super.replace(fb, offset, length, text, attrs);
				}
			}
		};
	}
	
	private boolean isValidString(String s){
		for(char c : s.toCharArray()){
			if(!validChars.contains(c)){
				return false;
			}
		}
		return true;
	}
	
	@Override
	protected DocumentFilter getDocumentFilter(){
		return filter;
	}
	
	@Override
	public Object stringToValue(String s) throws ParseException {
		if(field.isValid(s)){
			return field.parseString(s);
		}
		throw new ParseException("String "+s+" is not a valid format for DataField "+field.getClass(), 0);
	}

	@Override
	public String valueToString(Object o) throws ParseException {
		if(o instanceof DataField){
			DataField f = (DataField) o;
			return f.asString();
		}
		throw new ParseException("Unable to parse object "+o, 0);
	}

}
