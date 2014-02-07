package plugins.genome.services;

import org.osgi.framework.Bundle;

public interface GenomeService {
	
	public void addGenomeServiceListener(GenomeServiceListener l);
	public void removeGenomeServiceListener(GenomeServiceListener l);
	
	public void addGenomeFunction(GenomeFunction function, Bundle adder);
	public void removeGenomeFunction(GenomeFunction function, Bundle remover);
	public GenomeFunction[] getAllGenomeFunctions();
	public GenomeFunction[] getGenomeFunctions(String name);
	public String[] getGenomeFunctionNames();
	
}
