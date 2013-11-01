package datatypes;

public interface DataType<D extends DataType> {
	
	public D parse(String s);
	public String writeToString();
	public int getId();
	public void setId(int id);
	
}
