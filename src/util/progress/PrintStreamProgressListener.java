package util.progress;

import java.io.PrintStream;

public class PrintStreamProgressListener {
	
	protected final PrintStream m_Out;
	private final int m_CharLength;
	private final int m_PercentPerChar;
	
	private int m_PrevLen = 0;
	private String m_Message = null;
	private int m_Progress = 0;
	private int m_MaxProgress = 0;
	
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
		m_Progress = 0;
		m_MaxProgress = 0;
	}
	
	protected int printProgressBar(int progress, int maxProgress){
		int percent = progress * 100 / maxProgress;
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
		m_Out.print("\r");
		if(m_MaxProgress > 0 && m_Progress >= 0){
			printLen += this.printProgressBar(m_Progress, m_MaxProgress);
		}
		if(m_Message != null){
			printLen += this.printMessage(m_Message);
		}
		this.printOverRemaining(printLen);
		m_PrevLen = printLen;
	}
	
	protected void messageAndProgressUpdate(int progress, int maxProgress, String message){
		m_Progress = progress;
		m_MaxProgress = maxProgress;
		m_Message = message;
		this.printProgress();
	}
	
	protected void progressUpdate(int progress, int maxProgress){
		m_Progress = progress;
		m_MaxProgress = maxProgress;
		this.printProgress();
	}
	
	protected void messageUpdate(String message){
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
		m_Out.print("\rError: ");
		m_Out.println(t.getMessage());
		m_PrevLen = 0;
	}
	
	protected void info(String message){
		m_Out.print("\r");
		m_Out.println(message);
		m_PrevLen = 0;
	}
	
	public void update(ProgressEvent e){
		switch(e.getType()){
		case CANCELED:
			this.finished(e.getMessage());
			break;
		case COMPLETED:
			this.finished(e.getMessage());
			break;
		case ERROR:
			this.error(e.getThrowable());
			break;
		case INDETERMINANT_UPDATE:
			this.progressUpdate(0, 0);
			break;
		case INFO:
			this.info(e.getMessage());
			break;
		case MESSAGE_UPDATE:
			this.messageUpdate(e.getMessage());
			break;
		case PROGRESS_UPDATE:
			this.progressUpdate(e.getProgress(), e.getMaxProgress());
			break;
		case MESSAGE_AND_PROGRESS_UPDATE:
			this.messageAndProgressUpdate(e.getProgress(), e.getMaxProgress(), e.getMessage());
			break;
		default:
			break;
		
		}
	}
	
}
