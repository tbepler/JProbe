package util;

import java.util.HashMap;
import java.util.Map;

public class Timer {
	
	private static final Map<Object, Long> m_StartTime = new HashMap<Object, Long>();
	private static final Map<Object, Long> m_TotalTime = new HashMap<Object, Long>();
	
	public static void start(Object id){
		m_StartTime.put(id, System.currentTimeMillis());
	}
	
	public static long stop(Object id){
		long time = System.currentTimeMillis();
		if(m_StartTime.containsKey(id)){
			long prevTime = m_StartTime.get(id);
			long timeTaken = time - prevTime;
			if(m_TotalTime.containsKey(id)){
				long total = m_TotalTime.get(id) + timeTaken;
				m_TotalTime.put(id, total);
			}else{
				m_TotalTime.put(id, timeTaken);
			}
			return timeTaken;
		}
		return -1;
	}
	
	public static long report(Object id){
		if(m_TotalTime.containsKey(id)){
			return m_TotalTime.get(id);
		}
		return -1;
	}
	
}
