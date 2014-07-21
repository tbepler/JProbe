package jprobe.framework.model.compiler.grammar.implementation.symbols;

import jprobe.framework.model.compiler.grammar.Symbol;
import jprobe.framework.model.compiler.grammar.Tokenizer;
import jprobe.framework.model.compiler.grammar.implementation.SabreVisitor;

public abstract class Terminal extends Symbol<SabreVisitor> implements Tokenizer<SabreVisitor>{
	private static final long serialVersionUID = 1L;

}
