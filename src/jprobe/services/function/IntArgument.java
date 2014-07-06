package jprobe.services.function;

import javax.swing.JTextField;

import util.math.MathUtils;

public abstract class IntArgument<P> extends SpinnerArgument<P,Integer>{
	
	protected static class IntModel extends SpinnerArgument.Model<Integer>{
		private static final long serialVersionUID = 1L;
		
		private final int m_Min;
		private final int m_Max;
		private final int m_Increment;
		
		protected IntModel(int start, int min, int max, int increment){
			super(start);
			m_Min = min;
			m_Max = max;
			m_Increment = increment;
		}
		
		private boolean legal(int val){
			return val >= m_Min && val <= m_Max;
		}
		
		@Override
		public Integer getNextValue() {
			int next = this.getValue() + m_Increment;
			if(this.legal(next)){
				return next;
			}else{
				return MathUtils.clamp(next, m_Min, m_Max);
			}
		}

		@Override
		public Integer getPreviousValue() {
			int prev = this.getValue() - m_Increment;
			if(this.legal(prev)){
				return prev;
			}else{
				return MathUtils.clamp(prev, m_Min, m_Max);
			}
		}
		
	}
	
	private final int m_Min;
	private final int m_Max;
	private final int m_Start;
	private final int m_Inc;

	protected IntArgument(
			String name,
			String tooltip,
			String category,
			Character shortFlag,
			String prototypeVal,
			boolean optional,
			int startValue,
			int min,
			int max,
			int increment) {
		
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
		m_Inc = increment;
	}

	@Override
	protected boolean isValid(Integer value) {
		if(value == null) return false;
		return value >= m_Min && value <= m_Max;
	}
	
	@Override
	protected Model<Integer> createModel(){
		return new IntModel(m_Start, m_Min, m_Max, m_Inc);
	}
	
	@Override
	protected Integer parse(String arg){
		return Integer.parseInt(arg);
	}

}
