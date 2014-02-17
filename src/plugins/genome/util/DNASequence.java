package plugins.genome.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DNASequence implements Sequence<DNA, Integer>{
	private static final long serialVersionUID = 1L;
	
	private final List<DNA> m_DNA;
	
	public DNASequence(){
		m_DNA = new ArrayList<DNA>();
	}
	
	public DNASequence(Sequence<DNA, Integer> dna){
		m_DNA = new ArrayList<DNA>();
		for(DNA d : dna){
			m_DNA.add(d);
		}
	}
	
	public DNASequence(List<DNA> dna){
		m_DNA = new ArrayList<DNA>();
		for(DNA d : dna){
			m_DNA.add(d);
		}
	}

	@Override
	public int compareTo(Sequence<DNA, Integer> other) {
		for(int i=0; i<this.size() && i<other.size(); i++){
			int mIndex = this.getStartIndex() + i;
			int oIndex = other.getStartIndex() + i;
			if(this.getElementAt(mIndex).compareTo(other.getElementAt(oIndex)) != 0){
				return this.getElementAt(mIndex).compareTo(other.getElementAt(oIndex));
			}
		}
		return this.size() - other.size();
	}

	@Override
	public Iterator<DNA> iterator() {
		return m_DNA.iterator();
	}

	@Override
	public DNA getElementAt(Integer index) {
		return m_DNA.get(index - 1);
	}

	@Override
	public Sequence<DNA, Integer> subsequence(Integer startIndex) {
		return this.subsequence(startIndex, this.getEndIndex());
	}

	@Override
	public Sequence<DNA, Integer> subsequence(Integer startIndex, Integer endIndex) {
		return new DNASequence(m_DNA.subList(startIndex - 1, endIndex));
	}

	@Override
	public Sequence<DNA, Integer> append(Sequence<DNA, Integer> other) {
		List<DNA> combined = new ArrayList<DNA>(m_DNA);
		for(DNA d : other){
			combined.add(d);
		}
		return new DNASequence(combined);
	}

	@Override
	public Sequence<DNA, Integer> insert(Sequence<DNA, Integer> other, Integer index) {
		List<DNA> combined = new ArrayList<DNA>();
		for(int i=this.getStartIndex(); i<=this.getEndIndex(); i++){
			if(combined.size() == index){
				for(DNA d : other){
					combined.add(d);
				}
			}
			combined.add(this.getElementAt(i));
		}
		return new DNASequence(combined);
	}

	@Override
	public Sequence<DNA, Integer> insert(DNA element, Integer index) {
		List<DNA> combined = new ArrayList<DNA>();
		for(int i=this.getStartIndex(); i<=this.getEndIndex(); i++){
			if(combined.size() == index){
				combined.add(element);
			}
			combined.add(this.getElementAt(i));
		}
		return new DNASequence(combined);
	}

	@Override
	public Sequence<DNA, Integer> removeElementAt(Integer index) {
		List<DNA> trimmed = new ArrayList<DNA>(m_DNA);
		trimmed.remove(index-1);
		return new DNASequence(trimmed);
	}

	@Override
	public Sequence<DNA, Integer> remove(DNA element) {
		List<DNA> trimmed = new ArrayList<DNA>(m_DNA);
		trimmed.remove(element);
		return new DNASequence(trimmed);
	}

	@Override
	public Sequence<DNA, Integer> remove(Sequence<DNA, Integer> other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean matches(Sequence<DNA, Integer> other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(Sequence<DNA, Integer> other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(DNA element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Integer getStartIndex() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getEndIndex() {
		// TODO Auto-generated method stub
		return null;
	}

}
