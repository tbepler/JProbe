package util.progress;

public class Test extends junit.framework.TestCase{
	
	private final PrintStreamProgressListener m_Bar = new PrintStreamProgressListener(System.out);
	
	public void testPrintProgressBar(){
		m_Bar.onStart("Starting...");
		String message = null;
		for(int i=0; i<=100; i++){
			if(i%5 == 0){
				message = "Message and stuff "+i;
			}
			m_Bar.update(i, message);
		}
		m_Bar.onCompletion("Completed.");
	}
	
}
