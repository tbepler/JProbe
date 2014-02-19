package chiptools.jprobe.field;

import chiptools.Constants;
import util.genome.GenomicRegion;
import jprobe.services.data.IntegerField;

public class PointSourceField extends IntegerField{
	private static final long serialVersionUID = 1L;
	
	private final int m_Val;
	private final GenomicRegion m_Region;
	
	public PointSourceField(int value, GenomicRegion region){
		if(!this.isValid(value, region)){
			throw new RuntimeException("Error: "+value+" is not a valid point source for this region");
		}
		m_Val = value;
		m_Region = region;
	}

	@Override
	public String getTooltip() {
		return Constants.POINT_SOURCE_FIELD_TOOLTIP;
	}

	@Override
	public int getValue() {
		return m_Val;
	}
	
	protected boolean isValid(int value, GenomicRegion region){
		return value >= Constants.POINT_SOURCE_MIN && value < region.getSize();
	}

	@Override
	public boolean isValid(int value) {
		return this.isValid(value, m_Region);
	}

	@Override
	public int getMin() {
		return Constants.POINT_SOURCE_MIN;
	}

	@Override
	public int getMax() {
		return (int) (m_Region.getSize() - 1);
	}

	@Override
	public int getIncrement() {
		return 1;
	}

	@Override
	public IntegerField parseInt(int value) throws Exception {
		try{
			return new PointSourceField(value, m_Region);
		} catch (Exception e){
			throw e;
		}
	}

}
