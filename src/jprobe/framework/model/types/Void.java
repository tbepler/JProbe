package jprobe.framework.model.types;

import java.util.Deque;

public class Void implements Type<Void>{
	private static final long serialVersionUID = 1L;
	
	public static final String VOID = "void";
	
	private static Void m_Instance = null;
	
	public static Void getInstance(){
		if(m_Instance == null){
			m_Instance = new Void();
		}
		return m_Instance;
	}

	private Void() {
	}

	@Override
	public Void cast(Object obj) {
		return this;
	}

	@Override
	public boolean isAssignableFrom(Type<?> other) {
		if(other == null) return false;
		return true;
	}

	@Override
	public boolean isInstance(Object obj) {
		return this.equals(obj);
	}
	
	@Override
	public int hashCode(){
		return 0;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof Void){
			return true;
		}
		return false;
	}
	
	@Override
	public String toString(){
		return VOID;
	}

	@Override
	public Void extract(Deque<Object> objs) {
		return this;
	}

	@Override
	public boolean isExtractableFrom(Deque<Type<?>> types) {
		return true;
	}

	@Override
	public boolean canExtract(Deque<Object> objs) {
		return true;
	}

	@Override
	public Void extract(Object obj) {
		return this;
	}

	@Override
	public boolean isExtractableFrom(Type<?> type) {
		return true;
	}

	@Override
	public boolean canExtract(Object obj) {
		return true;
	}

}
