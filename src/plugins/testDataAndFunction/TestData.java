package plugins.testDataAndFunction;

import javax.swing.filechooser.FileNameExtensionFilter;

import jprobe.services.data.AbstractFinalData;

public class TestData extends AbstractFinalData{
	private static final long serialVersionUID = 1L;
	
	public static final FileNameExtensionFilter[] FORMATS = new FileNameExtensionFilter[]{new FileNameExtensionFilter("Text file", "txt")};
	
	private final String m_String;
	private final double m_Double;
	private final int m_Int;
	
	public TestData(String string, int integer, double decimal){
		super(3, 1);
		m_String = string;
		m_Double = decimal;
		m_Int = integer;
	}
	
	public TestData(){
		this("String", 5, 3.333333);
	}
	
	@Override
	public String toString(){
		return this.getString()+"\t"+this.getInt()+"\t"+this.getDouble();
	}
	
	public String getString(){
		return m_String;
	}
	
	public int getInt(){
		return m_Int;
	}
	
	public double getDouble(){
		return m_Double;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch(columnIndex){
		case 0: return String.class;
		case 1: return Double.class;
		case 2: return Integer.class;
		default: return null;
		}
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch(columnIndex){
		case 0: return "String";
		case 1: return "Double";
		case 2: return "Integer";
		default: return null;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex){
		case 0: return m_String;
		case 1: return m_Double;
		case 2: return m_Int;
		default: return null;
		}
	}

	@Override
	public void dispose() {
		//do nothing
	}
	
}
