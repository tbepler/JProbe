package util.progress;

import java.io.PrintStream;

public class PrintStreamProgressListener implements ProgressListener{
	
	protected final PrintStream m_Out;
	private final int m_CharLength;
	private final int m_PercentPerChar;
	
	private int m_PrevLen = 0;
	private String m_Message = null;
	private int m_Percent = -1;
	
	public PrintStreamProgressListener(PrintStream out, int charLength){
		m_Out = out;
		m_CharLength = charLength;
		m_PercentPerChar = 100 / charLength;
	}
	
	public PrintStreamProgressListener(PrintStream out){
		this(out, 20);
	}
	
	public void reset(){
		m_PrevLen = 0;
		m_Message = null;
		m_Percent = -1;
	}
	
	protected int printProgressBar(int percent){
		m_Out.format("%4s%%", percent);
		m_Out.print(" [");
		int barLen = Math.min(percent / m_PercentPerChar, m_CharLength);
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<barLen; i++){
			builder.append("#");
		}
		String format = "%-" + m_CharLength + "s";
		m_Out.format(format, builder.toString());
		m_Out.print("]  ");
		return 5 + 2 + m_CharLength + 3;
	}
	
	protected int printMessage(String message){
		m_Out.print(message);
		m_Out.print("     ");
		int printLen = message.length() + 5;
		return printLen;
	}
	
	protected void printOverRemaining(int printLen){
		while(m_PrevLen > printLen++){
			m_Out.print(" ");
		}
	}
	
	protected void printProgress(){
		int printLen = 0;
		if(m_PrevLen > 0){
			m_Out.print("\r");
		}
		if(!Progress.isIndeterminate(m_Percent)){
			printLen += this.printProgressBar(m_Percent);
		}
		if(m_Message != null){
			printLen += this.printMessage(m_Message);
		}
		this.printOverRemaining(printLen);
		m_PrevLen = printLen;
	}
	
	protected void update(int percent, String message){
		m_Percent = percent;
		m_Message = message;
		this.printProgress();
	}
	
	protected void finished(String message){
		if(message != null){
			m_Out.print("\r");
			m_Out.print(message);
			this.printOverRemaining(message.length());
			m_Out.println();
		}else if(m_PrevLen > 0){
			m_Out.println();
		}
		this.reset();
	}
	
	protected void error(Throwable t){
		if(m_PrevLen > 0){
			m_Out.print("\r");
		}
		m_Out.print("Error: ");
		m_Out.println(t.getMessage());
		m_PrevLen = 0;
	}
	
	protected void info(String message){
		if(m_PrevLen > 0){
			m_Out.print("\r");
		}
		m_Out.println(message);
		m_PrevLen = 0;
	}

	@Override
	public void onStart(String message) {
		if(message != null){
			this.info(message);
		}
	}

	@Override
	public void onError(Throwable t) {
		if(t != null){
			this.error(t);
		}
	}

	@Override
	public void onCompletion(String message) {
		this.finished(message);
	}

	@Override
	public void progressUpdate(int percent, String message) {
		this.update(percent, message);
	}
	
}
