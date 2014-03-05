package chiptools.jprobe.field;

import chiptools.Constants;
import util.genome.GenomicRegion;
import jprobe.services.data.Field;

public class GenomicRegionField implements Field{
	private static final long serialVersionUID = 1L;
	
	private final GenomicRegion m_Region;
	
	public GenomicRegionField(GenomicRegion region){
		m_Region = region;
	}
	
	@Override
	public String asString() {
		return m_Region.toString();
	}

	@Override
	public Field parseString(String s) throws Exception {
		GenomicRegion newRegion = GenomicRegion.parseString(s);
		return new GenomicRegionField(newRegion);
	}

	@Override
	public boolean isValid(String s) {
		try{
			GenomicRegion.parseString(s);
			return true;
		} catch (Exception e){
			return false;
		}
	}

	@Override
	public boolean isCharacterAllowed(char c) {
		return true;
	}

	@Override
	public String getTooltip() {
		return Constants.GENOMIC_REGION_FIELD_TOOLTIP;
	}

}
