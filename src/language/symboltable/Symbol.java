package language.symboltable;
/**
 * 
 * @author Wei
 *
 */
public class Symbol {
	private Kinds kind;
	private String id;
	
	public Symbol(String id, Kinds kind){
		this.kind = kind;
		this.id = id;
	}
	
	public Kinds kind(){
		
		return this.kind;
	}
	
	public String identifier(){
		return this.id;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null){
			return false;
		}
		
		if (!(o instanceof Symbol)) {
			return false;
		}
		
		if(this.id == ((Symbol) o).identifier() && this.kind == ((Symbol)o).kind()){
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode(){
		int hash = 17;
		int prime = 37;
		
		hash = hash*prime + id.hashCode();
		hash = hash*prime + kind.hashCode();
		
	    return hash;
	}
}
