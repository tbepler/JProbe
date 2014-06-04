package util.genome.probe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.*;

public class ProbeGroup implements Serializable, Iterable<Probe>{
	private static final long serialVersionUID = 1L;
	
	public static ProbeGroup readProbeGroup(InputStream in){
		List<Probe> probes = new ArrayList<Probe>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		try {
			while((line = reader.readLine()) != null){
				try{
					probes.add(Probe.parseProbe(line));
				} catch (Exception e){
					//do nothing
				}
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ProbeGroup(probes);
	}
	
	private final List<Probe> m_Probes = new ArrayList<Probe>();
	
	public ProbeGroup(Collection<Probe> probes){
		for(Probe p : probes){
			m_Probes.add(p);
		}
		Collections.sort(m_Probes);
	}
	
	public List<Probe> toList(){
		return new ArrayList<Probe>(m_Probes);
	}
	
	public int size(){
		return m_Probes.size();
	}
	
	public Probe getProbe(int index){
		return m_Probes.get(index);
	}
	
	@Override
	public Iterator<Probe> iterator() {
		return m_Probes.iterator();
	}
	
	@Override
	public String toString(){
		String s = "";
		for(int i=0; i<m_Probes.size(); i++){
			Probe p = m_Probes.get(i);
			s += p.toString(i+1) + "\n";
		}
		return s;
	}

}
