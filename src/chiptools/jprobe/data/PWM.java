package chiptools.jprobe.data;

import chiptools.jprobe.field.*;
import jprobe.services.data.Data;
import jprobe.services.data.DataListener;
import jprobe.services.data.Field;

public class PWM implements Data{
	private static final long serialVersionUID = 1L;
	
	private final util.genome.pwm.PWM m_PWM;
	private final Field[][] m_Table;
	
	public PWM(util.genome.pwm.PWM pwm){
		m_PWM = pwm;
		m_Table = new Field[4][pwm.length() + 1];
		m_Table[0][0] = new StringField("A:", "Base");
		m_Table[1][0] = new StringField("C:", "Base");
		m_Table[2][0] = new StringField("G:", "Base");
		m_Table[3][0] = new StringField("T:", "Base");
		for(char base : new char[]{'A','C','G','T'}){
			int row;
			switch(base){
			case 'A':
				row = 0;
				break;
			case 'C':
				row = 1;
				break;
			case 'G':
				row = 2;
				break;
			case 'T':
				row = 3;
				break;
			default:
				throw new RuntimeException("Something broke");
			}
			for(int col=1; col<m_Table[row].length; col++){
				double val = pwm.score(base, col-1);
				m_Table[row][col] = new DecimalField(val, "Score");
			}
		}
	}
	
	public util.genome.pwm.PWM getPWM(){
		return m_PWM;
	}

	@Override
	public void addDataListener(DataListener listener) {
		//final
	}

	@Override
	public void removeDataListener(DataListener listener) {
		//final
	}

	@Override
	public boolean isModifiable(int row, int col) {
		//final
		return false;
	}

	@Override
	public String[] getHeaders() {
		String[] header = new String[m_Table[0].length];
		header[0] = "Base";
		for(int i=1; i<header.length; i++){
			header[i] = String.valueOf(i);
		}
		return header;
	}
	
	@Override
	public String toString(){
		String s = "";
		for(int row=0; row<m_Table.length; row++){
			for(int col=0; col<m_Table[row].length; col++){
				s += m_Table[row][col]+"\t";
			}
			s+="\n";
		}
		return s;
	}

	@Override
	public Field[][] toTable() {
		return m_Table;
	}

	@Override
	public boolean setValue(int row, int col, Field value) {
		//final
		return false;
	}

	@Override
	public Field getValue(int row, int col) {
		return m_Table[row][col];
	}

	@Override
	public int getNumRows() {
		return m_Table.length;
	}

	@Override
	public int getNumCols() {
		return m_PWM.length() + 1;
	}

	@Override
	public String getTooltip() {
		return "This data structure represents a position weight matrix.";
	}

}
