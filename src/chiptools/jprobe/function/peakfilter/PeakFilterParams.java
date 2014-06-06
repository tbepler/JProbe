package chiptools.jprobe.function.peakfilter;

import java.util.ArrayList;
import java.util.List;

import chiptools.jprobe.data.Peaks;
import chiptools.jprobe.function.params.PeaksParam;
import util.genome.peak.Peak;
import util.genome.peak.PeakUtils.Filter;

public class PeakFilterParams implements Filter, PeaksParam{
	
	private Peaks m_Peaks = null;
	
	private final List<Filter> m_Filters = new ArrayList<Filter>();
	
	public void addFilter(Filter f){
		m_Filters.add(f);
	}
	
	@Override
	public boolean keep(Peak p) {
		for(Filter f : m_Filters){
			if(!f.keep(p)) return false;
		}
		return true;
	}

	@Override
	public void setPeaks(Peaks p) {
		m_Peaks = p;
	}

	@Override
	public Peaks getPeaks() {
		return m_Peaks;
	}

}
