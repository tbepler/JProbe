package plugins.testDataAndFunction;

import jprobe.services.Data;
import jprobe.services.DataField;
import jprobe.services.DataListener;

public class TestData implements Data{
	
	public static final String TOOLTIP = "A test data object with three fields";
	
	private StringField string;
	private IntField integer;
	private DecimalField decimal;
	private DataField[][] table;
	
	public TestData(){
		string = new StringField("String");
		integer = new IntField(1);
		decimal = new DecimalField(2.5);
		table = new DataField[1][3];
		table[1][1] = string;
		table[1][2] = integer;
		table[1][3] = decimal;
	}
	
	@Override
	public void addDataListener(DataListener listener) {
		//do nothing
	}

	@Override
	public void removeDataListener(DataListener listener) {
		//do nothing
	}

	@Override
	public boolean isModifiable(int row, int col) {
		return true;
	}

	@Override
	public DataField[][] toTable() {
		return table;
	}

	@Override
	public boolean setValue(int row, int col, DataField value) {
		table[row][col] = value;
		return true;
	}

	@Override
	public DataField getValue(int row, int col) {
		return table[row][col];
	}
	
	@Override
	public int getNumRows(){
		return table.length;
	}
	
	@Override
	public int getNumCols(){
		return table[0].length;
	}

	@Override
	public String getTooltip() {
		return TOOLTIP;
	}
	
}
