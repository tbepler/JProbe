package util.progress;

import util.progress.ProgressEvent.Type;

public class Test extends junit.framework.TestCase{
	
	private final PrintStreamProgressBar m_Bar = new PrintStreamProgressBar(System.out);
	
	public void testPrintProgressBar(){
		for(int i=0; i<=100; i++){
			m_Bar.update(new ProgressEvent(null, Type.UPDATE, i, 100, "Message and stuff "+i));
		}
	}
	
}
