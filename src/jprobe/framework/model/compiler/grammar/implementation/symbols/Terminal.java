package jprobe.framework.model.compiler.grammar.implementation.symbols;

import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.Visitor;
import jprobe.framework.model.compiler.lexer.Tokenizer;

public abstract class Terminal extends Symbol<Visitor> implements Tokenizer<Visitor>{
	private static final long serialVersionUID = 1L;

}
