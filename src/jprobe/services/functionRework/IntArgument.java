package jprobe.services.functionRework;

import javax.swing.JTextField;

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
				return cur;
			}
		}

		@Override
		public Integer prev(Integer cur) {
			int prev = cur - m_Increment;
			if(this.legal(prev)){
				return prev;
			}else{
				return cur;
			}
		}
		
	}
	
	private final int m_Min;
	private final int m_Max;

	protected IntArgument(
			String name,
			String tooltip,
			String category,
			boolean optional,
			int startValue,
			int min,
			int max,
			int increment) {
		
		super(
				name,
				tooltip,
				category,
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
		return value >= m_Min && value <= m_Max;
	}

}
