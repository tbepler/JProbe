package chiptools.jprobe.field;

import util.genome.Chromosome;
import jprobe.services.data.Field;

public class ChromosomeBaseField implements Field{
	
	private final Chromosome m_Chrom;
	private final long m_Base;
	
	public ChromosomeBaseField(Chromosome chrom, long base){
		
		m_Chrom = chrom;
		m_Base = base;
	}
	
	@Override
	public String asString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Field parseString(String s) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValid(String s) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCharacterAllowed(char c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getTooltip() {
		// TODO Auto-generated method stub
		return null;
	}

}
