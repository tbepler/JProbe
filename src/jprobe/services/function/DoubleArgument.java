package jprobe.services.function;

import javax.swing.JTextField;

import util.math.MathUtils;

public abstract class DoubleArgument<P> extends SpinnerArgument<P, Double>{
	
	protected static class DoubleModel extends SpinnerArgument.Model<Double>{
		private static final long serialVersionUID = 1L;
		
		private final double m_Min;
		private final double m_Max;
		private final double m_Increment;
		
		protected DoubleModel(double startVal, double min, double max, double increment){
			super(startVal);
			m_Min = min;
			m_Max = max;
			m_Increment = increment;
		}
		
		private boolean legal(double val){
			return val >= m_Min && val <= m_Max;
		}

		@Override
		public Double getNextValue() {
			double next = this.getValue() + m_Increment;
			if(this.legal(next)){
				return next;
			}else{
				return MathUtils.clamp(next, m_Min, m_Max);
			}
		}

		@Override
		public Double getPreviousValue() {
			double prev = this.getValue() - m_Increment;
			if(this.legal(prev)){
				return prev;
			}else{
				return MathUtils.clamp(prev, m_Min, m_Max);
			}
		}
		
	}
	
	private final double m_Min;
	private final double m_Max;
	private final double m_Start;
	private final double m_Increment;
	
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
				JTextField.RIGHT
				);
		
		m_Min = min;
		m_Max = max;
		m_Start = startValue;
		m_Increment = increment;
	}

	@Override
	protected boolean isValid(Double value) {
		if(value == null) return false;
		return value >= m_Min && value <= m_Max;
	}
	
	@Override
	protected Model<Double> createModel(){
		return new DoubleModel(m_Start, m_Min, m_Max, m_Increment);
	}
	
	@Override
	protected Double parse(String arg){
		return Double.valueOf(arg);
	}

}
