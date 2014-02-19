package chiptools.jprobe.field;

import chiptools.Constants;
import util.genome.Chromosome;
import jprobe.services.data.Field;

public class ChromosomeBaseField implements Field{
	private static final long serialVersionUID = 1L;
	
	private final Chromosome m_Chrom;
	private final long m_Base;
	
	public ChromosomeBaseField(Chromosome chrom, long base){
		if(!this.baseLegal(chrom, base)){
			throw new RuntimeException("Error: cannot create a chromosome base field for a base index that is off the chromosome");
		}
		m_Chrom = chrom;
		m_Base = base;
	}
	
	@Override
	public String asString() {
		return String.valueOf(m_Base);
	}

	@Override
	public Field parseString(String s) throws Exception {
		try{
			long base = Long.parseLong(s);
			return new ChromosomeBaseField(m_Chrom, base);
		} catch (Exception e){
			throw e;
		}
	}
	
	private boolean baseLegal(long base){
		return this.baseLegal(m_Chrom, base);
	}
	
	private boolean baseLegal(Chromosome chrom, long base){
		return base >= 1 && (chrom.getSize() < 0 || chrom.getSize() >= base);
	}

	@Override
	public boolean isValid(String s) {
		try{
			long base = Long.parseLong(s);
			return this.baseLegal(base);
		} catch (Exception e){
			return false;
		}
	}

	@Override
	public boolean isCharacterAllowed(char c) {
		return String.valueOf(c).matches(Constants.POSITIVE_INT_REGEX);
	}

	@Override
	public String getTooltip() {
		return Constants.CHROM_BASE_FIELD_TOOLTIP;
	}

}
