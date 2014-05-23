package jprobe.services.function;

import javax.swing.JTextField;

import util.MathUtils;

public abstract class DoubleArgument<P> extends SpinnerArgument<P, Double>{
	
	protected static class DoubleModel implements SpinnerArgument.Spinner<Double>{
		
		private final double m_Min;
		private final double m_Max;
		private final double m_Increment;
		
		protected DoubleModel(double min, double max, double increment){
			m_Min = min;
			m_Max = max;
			m_Increment = increment;
		}
		
		private boolean legal(double val){
			return val >= m_Min && val <= m_Max;
		}
		
		@Override
		public Double next(Double cur) {
			double next = cur + m_Increment;
			if(this.legal(next)){
				return next;
			}else{
				return MathUtils.clamp(next, m_Min, m_Max);
			}
		}

		@Override
		public Double prev(Double cur) {
			double prev = cur - m_Increment;
			if(this.legal(prev)){
				return prev;
			}else{
				return MathUtils.clamp(prev, m_Min, m_Max);
			}
		}
		
	}
	
	private final double m_Min;
	private final double m_Max;
	
	protected DoubleArgument(
			String name,
			String tooltip,
			String category,
			Character shortFlag,
			String prototypeVal,
			boolean optional,
			double startValue,
			double min,
			double max,
			double increment) {
		
		super(
				name,
				tooltip,
				category,
				shortFlag,
				prototypeVal,
				optional,
				startValue,
				new DoubleModel(min, max, increment),
				JTextField.RIGHT
				);
		
		m_Min = min;
		m_Max = max;
	}

	@Override
	protected boolean isValid(Double value) {
		if(value == null) return false;
		return value >= m_Min && value <= m_Max;
	}

}
