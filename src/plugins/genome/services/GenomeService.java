package plugins.genome.services;

import org.osgi.framework.Bundle;

public interface GenomeService {
	
	public void addGenomeServiceListener(GenomeServiceListener l);
	public void removeGenomeServiceListener(GenomeServiceListener l);
	
	public void addGenomeFunctionPrototype(GenomeFunctionPrototype prototype, Bundle adder);
	public void removeGenomeFunctionPrototype(GenomeFunctionPrototype prototype, Bundle remover);
	public GenomeFunctionPrototype[] getAllGenomeFunctinoPrototypes();
	public GenomeFunctionPrototype[] getGenomeFunctionPrototypes(String name);
	public String[] getGenomeFunctionNames();
	
}
