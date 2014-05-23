package jprobe.services.function;

import javax.swing.JTextField;

import util.MathUtils;

public abstract class IntArgument<P> extends SpinnerArgument<P,Integer>{
	
	protected static class IntModel implements SpinnerArgument.Spinner<Integer>{
		
		private final int m_Min;
		private final int m_Max;
		private final int m_Increment;
		
		protected IntModel(int min, int max, int increment){
			m_Min = min;
			m_Max = max;
			m_Increment = increment;
		}
		
		private boolean legal(int val){
			return val >= m_Min && val <= m_Max;
		}
		
		@Override
		public Integer next(Integer cur) {
			int next = cur + m_Increment;
			if(this.legal(next)){
				return next;
			}else{
				return MathUtils.clamp(next, m_Min, m_Max);
			}
		}

		@Override
		public Integer prev(Integer cur) {
			int prev = cur - m_Increment;
			if(this.legal(prev)){
				return prev;
			}else{
				return MathUtils.clamp(prev, m_Min, m_Max);
			}
		}
		
	}
	
	private final int m_Min;
	private final int m_Max;

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
				startValue,
				new IntModel(min, max, increment),
				JTextField.RIGHT
				);
		
		m_Min = min;
		m_Max = max;
	}

	@Override
	protected boolean isValid(Integer value) {
		if(value == null) return false;
		return value >= m_Min && value <= m_Max;
	}

}
