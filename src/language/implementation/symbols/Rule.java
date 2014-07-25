package language.implementation.symbols;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import language.compiler.grammar.Assoc;
import language.compiler.grammar.Production;
import language.compiler.grammar.Token;
import language.implementation.Visitor;

public abstract class Rule extends Token<Visitor> implements Production<Visitor> {
	private static final long serialVersionUID = 1L;
	
	private static final Map<Class<?>, Constructor<? extends Rule>> CONS_CACHE =
			new HashMap<Class<?>, Constructor<? extends Rule>>();
	
	private static final Map<Class<?>, List<Class<? extends Token<Visitor>>>> RHS_CACHE = 
			new HashMap<Class<?>, List<Class<? extends Token<Visitor>>>>();
	
	private static Constructor<? extends Rule> getCons(Class<? extends Rule> clazz){
		Constructor<? extends Rule> con;
		synchronized(Rule.class){
			con = CONS_CACHE.get(clazz);
			if(con == null){
				Constructor<? extends Rule>[] cons = (Constructor<? extends Rule>[]) clazz.getConstructors();
				assert(cons.length == 1);
				con = cons[0];
				CONS_CACHE.put(clazz, con);
			}
		}
		return con;
	}
	
	private static List<Class<? extends Token<Visitor>>> getRHS(Class<? extends Rule> clazz){
		List<Class<? extends Token<Visitor>>> list;
		synchronized(Rule.class){
			list = RHS_CACHE.get(clazz);
			if(list == null){
				list = new ArrayList<Class<? extends Token<Visitor>>>();
				Constructor<? extends Rule> con = getCons(clazz);
				for(Class<?> param : con.getParameterTypes()){
					assert(Token.class.isAssignableFrom(param));
					list.add((Class<? extends Token<Visitor>>) param);
				}
				list = Collections.unmodifiableList(list);
				RHS_CACHE.put(clazz, list);
			}
		}
		return list;
	}

	@Override
	public List<Class<? extends Token<Visitor>>> rightHandSide() {
		return getRHS(this.getClass());
	}

	@Override
	public Token<Visitor> reduce(List<Token<Visitor>> symbols) {
		Constructor<? extends Rule> con = getCons(this.getClass());
		try {
			return con.newInstance(symbols.toArray());
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public int getPriority(){
		return Constants.DEFAULT_PRIORITY;
	}
	
	@Override
	public Assoc getAssoc(){
		return Constants.DEFAULT_ASSOC;
	}
	
	@Override
	public String toString(){
		return this.getClass().getSimpleName();
	}

}
