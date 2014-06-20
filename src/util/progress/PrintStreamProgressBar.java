package util.progress;

import java.io.PrintStream;

public class PrintStreamProgressBar {
	
	protected final PrintStream m_Out;
	private final int m_CharLength;
	private final int m_PercentPerChar;
	private int m_PrevLen = 0;
	
	public PrintStreamProgressBar(PrintStream out, int charLength){
		m_Out = out;
		m_CharLength = charLength;
		m_PercentPerChar = 100 / charLength;
	}
	
	public PrintStreamProgressBar(PrintStream out){
		this(out, 20);
	}
	
	protected int printProgressBar(int progress, int maxProgress){
		int percent = progress * 100 / maxProgress;
		m_Out.format("%4s%%", percent);
		m_Out.print(" [");
		int barLen = percent / m_PercentPerChar;
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<barLen; i++){
			builder.append("#");
		}
		String format = "%-" + m_CharLength + "s";
		m_Out.format(format, builder.toString());
		m_Out.print("]");
		return 5 + 2 + m_CharLength + 1;
	}
	
	public void update(ProgressEvent e){
		int printLen = 0;
		String message = e.getMessage();
		boolean printMessage = message != null && !message.equals("");
		switch(e.getType()){
		case UPDATE:
			boolean printProgress = !e.isIndeterminant() && e.getMaxProgress() > 0;
			if(printMessage || printProgress){
				m_Out.print("\r");
			}
			if(printProgress){
				printLen += this.printProgressBar(e.getProgress(), e.getMaxProgress());
			}
			if(printProgress && printMessage){
				m_Out.print("  ");
				printLen += 2;
			}
			if(printMessage){
				m_Out.print(message);
				m_Out.print("     ");
				printLen += message.length() + 5;
			}
			if(printMessage || printProgress){
				while(m_PrevLen > printLen){
					m_Out.print(" ");
					--m_PrevLen;
				}
			}
			break;
		default:
			if(printMessage){
				m_Out.print("\r");
				m_Out.print(message);
				while(m_PrevLen > message.length()){
					m_Out.print(" ");
					--m_PrevLen;
				}
			}
			m_Out.println();
			break;
		}
		m_PrevLen = printLen;
	}
	
}
