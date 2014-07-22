package jprobe.framework.model.compiler.parser;

import java.util.List;
import java.util.Set;

import jprobe.framework.model.compiler.grammar.Production;
import jprobe.framework.model.compiler.grammar.Symbol;

public class DefaultFactory implements Factory {

	@Override
	public <V> Action<V> newReduceAction(Production<V> prod) {
		return new Action<V>(Actions.REDUCE, prod);
	}

	@Override
	public <V> Action<V> newGotoAction(State<V> next) {
		return new Action<V>(Actions.GOTO, next);
	}

	@Override
	public <V> Action<V> newShiftAction(State<V> next) {
		return new Action<V>(Actions.SHIFT, next);
	}

	@Override
	public <V> Action<V> newAcceptAction() {
		return new Action<V>(Actions.ACCEPT);
	}

	@Override
	public <V> Item<V> newItem(Production<V> production,
			List<Class<? extends Symbol<V>>> lookahead) {
		return new Item<V>(production, lookahead);
	}

	@Override
	public <V> Item<V> incrementItem(Item<V> item) {
		return item.increment();
	}

	@Override
	public <V> State<V> newState(Set<Item<V>> items) {
		return new State<V>(items);
	}

	

}
