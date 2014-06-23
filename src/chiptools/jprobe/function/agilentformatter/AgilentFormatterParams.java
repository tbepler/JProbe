package chiptools.jprobe.function.agilentformatter;

import java.util.List;

import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.params.PrimerParam;

public class AgilentFormatterParams implements PrimerParam{
	
	public List<DataCategory<Probes>> PROBE_CATEGORIES = null;
	
	public String ARRAY_NAME = null;
	
	private String m_Primer = "";
	
	public int FWD_REPS = 3;
	
	public int RVS_REPS = 3;

	@Override
	public void setPrimer(String sequence) {
		m_Primer = sequence;
	}

	@Override
	public String getPrimer() {
		return m_Primer;
	}
	
}
