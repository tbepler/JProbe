package chiptools.jprobe.function.probefilter;

import java.util.ArrayList;
import java.util.List;

import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.params.ProbesParam;
import util.genome.probe.Probe;

public class ProbeFilterParam implements util.genome.probe.ProbeUtils.Filter, ProbesParam{
	
	private final List<util.genome.probe.ProbeUtils.Filter> m_Filters = new ArrayList<util.genome.probe.ProbeUtils.Filter>();
	private Probes m_Probes = null;
	
	public void addFilter(util.genome.probe.ProbeUtils.Filter f){
		m_Filters.add(f);
	}
	
	@Override
	public boolean keep(Probe p) {
		for(util.genome.probe.ProbeUtils.Filter f : m_Filters){
			if(!f.keep(p)) return false;
		}
		return true;
	}

	@Override
	public void setProbes(Probes p) {
		m_Probes = p;
	}

	@Override
	public Probes getProbes() {
		return m_Probes;
	}

}
