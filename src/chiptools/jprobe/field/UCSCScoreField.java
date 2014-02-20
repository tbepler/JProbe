package chiptools.jprobe.field;

import util.MathUtils;
import chiptools.Constants;
import jprobe.services.data.IntegerField;

public class UCSCScoreField extends IntegerField{
	private static final long serialVersionUID = 1L;
	
	
	
	private final short m_Score;
	
	public UCSCScoreField(short score){
		score = MathUtils.clamp(score, Constants.UCSC_SCORE_MIN, Constants.UCSC_SCORE_MAX);
		if(!this.isValid(score)){
			throw new RuntimeException("Error: score out of bounds. Score="+score+", max="+Constants.UCSC_SCORE_MAX+", min="+Constants.UCSC_SCORE_MIN);
		}
		m_Score = score;
	}
	
	@Override
	public String getTooltip() {
		return Constants.UCSC_SCORE_FIELD_TOOLTIP;
	}

	@Override
	public int getValue() {
		return m_Score;
	}

	@Override
	public boolean isValid(int value) {
		return value >= Constants.UCSC_SCORE_MIN && value <= Constants.UCSC_SCORE_MAX;
	}

	@Override
	public int getMin() {
		return Constants.UCSC_SCORE_MIN;
	}

	@Override
	public int getMax() {
		return Constants.UCSC_SCORE_MAX;
	}

	@Override
	public int getIncrement() {
		return 1;
	}

	@Override
	public IntegerField parseInt(int value) throws Exception {
		try{
			return new UCSCScoreField((short) value);
		} catch (Exception e){
			throw e;
		}
	}

}
