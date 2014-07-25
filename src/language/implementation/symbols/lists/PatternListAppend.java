package language.implementation.symbols.lists;

import language.implementation.symbols.patterns.Pattern;

public class PatternListAppend extends ListAppendRule<Pattern>{
	private static final long serialVersionUID = 1L;

	public PatternListAppend() {
		super(PatternList.class, Pattern.class);
	}

}
