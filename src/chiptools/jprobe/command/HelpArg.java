package chiptools.jprobe.command;

import chiptools.Constants;
import util.ArgsParser;

public class HelpArg extends ArgsParser.Argument{
	
	private boolean m_Helped = false;
	
	@Override
	public String getTag() {
		return Constants.HELP_TAG;
	}

	@Override
	public void parse(String[] args) {
		m_Helped = true;
	}
	
	public boolean helpRequested(){
		return m_Helped;
	}

}
