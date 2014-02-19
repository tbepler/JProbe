package chiptools.jprobe.field;

import chiptools.Constants;
import util.genome.Strand;
import jprobe.services.data.Field;

public class StrandField implements Field{
	private static final long serialVersionUID = 1L;
	
	private final Strand m_Strand;
	
	public StrandField(Strand s){
		m_Strand = s;
	}
	
	@Override
	public String asString() {
		return m_Strand.toString();
	}

	@Override
	public Field parseString(String s) throws Exception {
		try{
			return new StrandField(Strand.parseStrand(s));
		} catch (Exception e){
			throw e;
		}
	}

	@Override
	public boolean isValid(String s) {
		if(s.length() != 1){
			return false;
		}
		char c = s.charAt(0);
		return Strand.getStrandChars().contains(c);
	}

	@Override
	public boolean isCharacterAllowed(char c) {
		return Strand.getStrandChars().contains(c);
	}

	@Override
	public String getTooltip() {
		return Constants.STRAND_FIELD_TOOLTIP;
	}

}
