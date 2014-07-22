package jprobe.framework.model.compiler.grammar.implementation.symbols;

import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.implementation.SabreVisitor;
import jprobe.framework.model.compiler.lexer.Tokenizer;

public abstract class Terminal extends Symbol<SabreVisitor> implements Tokenizer<SabreVisitor>{
	private static final long serialVersionUID = 1L;

}
