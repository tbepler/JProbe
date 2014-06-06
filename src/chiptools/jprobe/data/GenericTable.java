package chiptools.jprobe.data;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import chiptools.jprobe.ChiptoolsActivator;
import jprobe.services.ErrorHandler;
import jprobe.services.data.AbstractFinalData;

public class GenericTable extends AbstractFinalData{
	
	public static final String DELIM = "\t";
	
	private static final long serialVersionUID = 1L;
	
	private final Map<String, Integer> m_RowIndexes = new HashMap<String, Integer>();
	private final Map<Integer, String> m_IndexRow = new HashMap<Integer, String>();
	
	private final Map<String, Integer> m_ColIndexes = new HashMap<String, Integer>();
	private final Map<Integer, String> m_IndexCol = new HashMap<Integer, String>();
	
	private final Map<Integer, Class<?>> m_ColClass = new HashMap<Integer, Class<?>>();
	
	private final Map<Integer, Map<Integer, Object>> m_Table = new HashMap<Integer, Map<Integer, Object>>();
	
	public GenericTable() {
		super(0, 0);
		this.appendCol("Row");
	}
	
	public void write(Writer w){
		//write header
		try {
			for(int col=0; col<this.getColumnCount(); ++col){
				w.write(m_IndexCol.get(col) + DELIM);
			}
			w.write("\n");
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, ChiptoolsActivator.getBundle());
		}
		
		//write rows
		for(int row=0; row<this.getRowCount(); ++row){
			this.writeRow(w, row);
		}
	}
	
	protected void writeRow(Writer w, int row){
		try {
			for(int col=0; col<this.getColumnCount(); ++col){ //write table entries
				Object entry = this.getValueAt(row, col);
				String val = entry == null ? "" : entry.toString(); //write empty string instead of null
				w.write(val + DELIM);
			}
			w.write("\n");
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, ChiptoolsActivator.getBundle());
		}
		
	}
	
	public boolean containsRow(String row){
		return m_RowIndexes.containsKey(row);
	}
	
	public boolean containsCol(String col){
		return m_ColIndexes.containsKey(col);
	}
	
	public void appendRow(String row){
		if(m_RowIndexes.containsKey(row)){
			throw new RuntimeException("Alread contains row "+row);
		}
		int index = m_RowIndexes.size();
		m_RowIndexes.put(row, index);
		m_IndexRow.put(index, row);
		this.put(0, index, row);
	}
	
	public void appendCol(String col){
		if(m_ColIndexes.containsKey(col)){
			throw new RuntimeException("Already contains col " + col); 
		}
		int index = m_ColIndexes.size();
		m_ColIndexes.put(col, index);
		m_IndexCol.put(index, col);
	}
	
	public void put(String col, String row, Object val){
		int colIndex = m_ColIndexes.get(col);
		int rowIndex = m_RowIndexes.get(row);
		this.put(colIndex, rowIndex, val);
	}
	
	public void put(int col, int row, Object val){
		if(!inBounds(col, row)){
			throw new RuntimeException("Index out of bounds. Col="+col+", row="+row);
		}
		Map<Integer, Object> rowMap;
		if(m_Table.containsKey(col)){
			rowMap = m_Table.get(col);
		}else{
			rowMap = new HashMap<Integer, Object>();
			m_Table.put(col, rowMap);
		}
		rowMap.put(row, val);
		if(m_ColClass.containsKey(col)){
			Class<?> clazz = m_ColClass.get(col);
			if(!val.getClass().equals(clazz)){
				m_ColClass.put(col, Object.class);
			}
		}else{
			m_ColClass.put(col, val.getClass());
		}
	}
	
	protected boolean inBounds(int col, int row){
		return col >= 0 && col < this.getColumnCount() && row >= 0 && row < this.getRowCount();
	}
	
	@Override
	public int getColumnCount(){
		return m_ColIndexes.size();
	}
	
	@Override
	public int getRowCount(){
		return m_RowIndexes.size();
	}

	@Override
	public void dispose() {
		//do nothing
	}

	@Override
	public Class<?> getColumnClass(int col) {
		if(m_ColClass.containsKey(col)){
			return m_ColClass.get(col);
		}
		return Object.class;
	}

	@Override
	public String getColumnName(int col) {
		return m_IndexCol.get(col);
	}

	@Override
	public Object getValueAt(int row, int col) {
		if(m_Table.containsKey(col)){
			return m_Table.get(col).get(row);
		}
		return null;
	}

}
