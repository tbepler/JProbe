package util.save;

public class SaveableEvent {
	
	public enum Type{
		CHANGED,
		SAVING,
		SAVED,
		LOADING,
		LOADED,
		IMPORTING,
		IMPORTED,
		FAILED;
	}
	
	public final Type type;
	public final String target;
	public final SaveableEvent start;
	
	public SaveableEvent(Type type, String target, SaveableEvent start){
		this.type = type;
		this.target = target;
		this.start = start;
	}
	
	public SaveableEvent(Type type, String target){
		this(type, target, null);
	}
	
	public SaveableEvent(Type type){
		this(type, null);
	}
	
}
