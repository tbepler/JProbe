package chiptools.jprobe.field;

import chiptools.Constants;
import util.genome.Chromosome;
import jprobe.services.data.Field;

public class ChromosomeField implements Field{
	private static final long serialVersionUID = 1L;
	
	private final Chromosome m_Chrom;
	
	public ChromosomeField(Chromosome chrom){
		m_Chrom = chrom;
	}
	
	@Override
	public String asString() {
		return m_Chrom.toString();
	}

	@Override
	public Field parseString(String s) throws Exception {
		try{
			if(m_Chrom.hasReferenceGenome()){
				if(!m_Chrom.getGenomicContext().hasChr(s)){
					throw new Exception("Error: genome "+m_Chrom.getGenomicContext()+" does not contain a chromosome named "+s);
				}
				return new ChromosomeField(m_Chrom.getGenomicContext().getChr(s));
			}
			return new ChromosomeField(new Chromosome(s));
		} catch(Exception e){
			throw e;
		}
	}

	@Override
	public boolean isValid(String s) {
		if(m_Chrom.hasReferenceGenome()){
			return m_Chrom.getGenomicContext().hasChr(s);
		}
		return true;
	}

	@Override
	public boolean isCharacterAllowed(char c) {
		return true;
	}

	@Override
	public String getTooltip() {
		return Constants.CHROM_FIELD_TOOLTIP;
	}

}
