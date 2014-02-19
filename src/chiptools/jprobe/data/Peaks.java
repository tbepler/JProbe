package chiptools.jprobe.data;

import jprobe.services.data.Data;
import jprobe.services.data.DataListener;
import jprobe.services.data.Field;

public class Peaks implements Data{

	@Override
	public void addDataListener(DataListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDataListener(DataListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isModifiable(int row, int col) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Field[][] toTable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setValue(int row, int col, Field value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Field getValue(int row, int col) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumRows() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumCols() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getTooltip() {
		// TODO Auto-generated method stub
		return null;
	}

}
