package chiptools.jprobe.data;

import jprobe.services.data.AbstractFinalData;

public class PWM extends AbstractFinalData{
	private static final long serialVersionUID = 1L;
	
	private static final int PWM_ROWS = 4;
	
	private static final int A = 0;
	private static final int C = 1;
	private static final int G = 2;
	private static final int T = 3;
	
	private static final int BASE_COL = 0;
	
	private static char getBase(int row){
		switch(row){
		case A: return 'A';
		case C: return 'C';
		case G: return 'G';
		case T: return 'T';
		default: return 'N';
		}
	}
	
	private static String getBaseStr(int row){
		switch(row){
		case A: return "A:";
		case C: return "C:";
		case G: return "G:";
		case T: return "T:";
		default: return null;
		}
	}
	
	private final util.genome.pwm.PWM m_PWM;
	
	public PWM(util.genome.pwm.PWM pwm){
		super(pwm.length() + 1, PWM_ROWS);
		m_PWM = pwm;
	}
	
	public util.genome.pwm.PWM getPWM(){
		return m_PWM;
	}
	
	@Override
	public String toString(){
		return m_PWM.toString();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch(columnIndex){
		case BASE_COL: return String.class;
		default: return Double.class;
		}
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch(columnIndex){
		case BASE_COL: return "Base";
		default: return String.valueOf(columnIndex);
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex){
		case BASE_COL: return getBaseStr(rowIndex);
		default: return m_PWM.score(getBase(rowIndex), columnIndex - 1);
		}
	}

	@Override
	public void dispose() {
		//do nothing
	}


}
