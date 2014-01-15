package jprobe.services.data;


public class DataEvent {
	
	
	public enum Type{
		FIELD_UPDATED,
		ROW_INSERTED,
		ROW_DELETED,
		COLUMN_INSERTED,
		COLUMN_DELETED;
	}
	
	private Type type;
	private Data source;
	private Field changed = null;
	private int row = -1;
	private int col = -1;
	
	public DataEvent(Data source, Type type){
		this.type = type;
		this.source = source;
	}
	
	public DataEvent(Data source, Type type, int row){
		this(source, type);
		this.row = row;
	}
	
	public DataEvent(Data source, Type type, Field changed, int row, int col){
		this(source, type);
		this.changed = changed;
		this.row = row;
		this.col = col;
	}
	
	public Type getType(){
		return type;
	}
	
	public Data getSource(){
		return source;
	}
	
	public Field getFieldChanged(){
		return changed;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getColumn(){
		return col;
	}
	
	
}
