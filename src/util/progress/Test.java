package util.progress;

public class Test extends junit.framework.TestCase{
	
	private final PrintStreamProgressListener m_Bar = new PrintStreamProgressListener(System.out);
	
	public void testPrintProgressBar(){
		for(int i=0; i<=100; i++){
			if(i%5 == 0){
				m_Bar.update(ProgressEvent.newMessageAndProgressUpdate(null, i, 100, "Message and stuff "+i));
			}else{
				m_Bar.update(ProgressEvent.newProgressUpdate(null, i, 100));
			}
		}
	}
	
}
