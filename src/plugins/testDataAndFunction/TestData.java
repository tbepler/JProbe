package plugins.testDataAndFunction;

import java.util.*;

import jprobe.services.data.Data;
import jprobe.services.data.DataEvent;
import jprobe.services.data.Field;
import jprobe.services.data.DataListener;

public class TestData implements Data{
	private static final long serialVersionUID = 1L;

	public static final String TOOLTIP = "A test data object with three fields";
	
	private StringField m_String;
	private IntField m_Integer;
	private DecimalField m_Decimal;
	private Field[][] m_Table;
	private Collection<DataListener> m_Listeners = new HashSet<DataListener>();
	
	public TestData(String string, int integer, double decimal){
		this.m_String = new StringField(string);
		this.m_Integer = new IntField(integer);
		this.m_Decimal = new DecimalField(decimal);
		this.fillTable();
	}
	
	public TestData(){
		m_String = new StringField("String");
		m_Integer = new IntField(1);
		m_Decimal = new DecimalField(2.5);
		this.fillTable();
	}
	
	private void fillTable(){
		m_Table = new Field[1][3];
		m_Table[0][0] = m_String;
		m_Table[0][1] = m_Integer;
		m_Table[0][2] = m_Decimal;
	}
	
	public String getString(){
		return m_String.asString();
	}
	
	public int getInt(){
		return m_Integer.getValue();
	}
	
	public double getDouble(){
		return m_Decimal.getValue();
	}
	
	@Override
	public void addDataListener(DataListener listener) {
		m_Listeners.add(listener);
	}

	@Override
	public void removeDataListener(DataListener listener) {
		m_Listeners.remove(listener);
	}
	
	private void notifyListeners(DataEvent event){
		for(DataListener l : m_Listeners){
			l.update(event);
		}
	}

	@Override
	public boolean isModifiable(int row, int col) {
		return true;
	}

	@Override
	public Field[][] toTable() {
		return m_Table;
	}

	@Override
	public boolean setValue(int row, int col, Field value) {
		m_Table[row][col] = value;
		this.notifyListeners(new DataEvent(this, DataEvent.Type.FIELD_UPDATED, value, row, col));
		return true;
	}

	@Override
	public Field getValue(int row, int col) {
		return m_Table[row][col];
	}
	
	@Override
	public int getNumRows(){
		return m_Table.length;
	}
	
	@Override
	public int getNumCols(){
		return m_Table[0].length;
	}

	@Override
	public String getTooltip() {
		return TOOLTIP;
	}
	
}
