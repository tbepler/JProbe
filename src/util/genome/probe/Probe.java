package util.genome.probe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import util.genome.Chromosome;
import util.genome.GenomicCoordinate;
import util.genome.GenomicRegion;
import util.genome.GenomicSequence;
import util.genome.ParsingException;
import util.genome.Strand;

public class Probe implements Serializable, Comparable<Probe>{
	private static final long serialVersionUID = 1L;

	private static final String BINDING_SITE_SEP = ";";
	private static final String MUTATIONS_SEP = "&";
	private static final String NUMBER_SEP = "-";
	
	private static final String REGION_REGEX = GenomicRegion.GENOMIC_REGION_REGEX;
	private static final String COORD_REGEX = GenomicCoordinate.COORD_REGEX;
	private static final String BINDING_SITES_REGEX = BINDING_SITE_SEP + "(" + REGION_REGEX + BINDING_SITE_SEP + ")*";
	private static final String MUTATIONS_REGEX = MUTATIONS_SEP + "(" + COORD_REGEX + MUTATIONS_SEP + ")*";
	private static final String NUMBERED_NAME_REGEX = ".*"+NUMBER_SEP+"\\d+";
	private static final String STRAND_REGEX = Strand.STRAND_REGEX;
	private static final String MUT_REGEX = "([Mm][Uu][Tt][Aa][Nn][Tt])|([Mm])";
	private static final String MUT_TYPE_REGEX = MUT_REGEX+"|([Ww][Ii][Ll][Dd][Tt][Yy][Pp][Ee])|([Ww])";
	
	public static Probe parseProbe(String s) throws ParsingException{
		try{
			String[] tokens = s.split("\\s");
			String seq = tokens[0];
			GenomicRegion region = null;
			String name = null;
			Strand strand = Strand.UNKNOWN;
			boolean mutant = false;
			GenomicRegion[] bindingSites = null;
			List<GenomicCoordinate> mutations = new ArrayList<GenomicCoordinate>();
			
			for(int i=1; i<tokens.length; i++){
				String cur = tokens[i];
				if(cur.matches(REGION_REGEX) && region == null){
					region = GenomicRegion.parseString(cur);
				}else if(cur.matches(BINDING_SITES_REGEX) && bindingSites == null){
					bindingSites = parseBindingSites(cur);
				}else if (cur.matches(MUTATIONS_REGEX) && mutations.isEmpty()){
					mutations = parseMutations(cur);
				}else if(cur.matches(STRAND_REGEX) && strand == Strand.UNKNOWN){
					strand = Strand.parseStrand(cur);
				}else if(cur.matches(MUT_TYPE_REGEX)){
					mutant = parseMutant(cur);
				}else if(name == null){
					name = parseName(cur);
				}
			}
			return new Probe(seq, region, bindingSites, name, strand, mutant);
			
		} catch (ParsingException e){
			throw e;
		} catch (Exception e){
			throw new ParsingException("Could not parse Probe from string: "+s);
		}
	}
	
	private static boolean parseMutant(String s){
		return s.matches(MUT_REGEX);
	}
	
	private static String parseName(String s){
		if(s.matches(NUMBERED_NAME_REGEX)){
			return s.substring(0, s.lastIndexOf(NUMBER_SEP));
		}
		return s;
	}
	
	private static GenomicRegion[] parseBindingSites(String s){
		String[] tokens = s.split(BINDING_SITE_SEP);
		List<GenomicRegion> bindingSites = new ArrayList<GenomicRegion>();
		for(int i=0; i<tokens.length; i++){
			try {
				if(tokens[i].matches(REGION_REGEX)){
					bindingSites.add(GenomicRegion.parseString(tokens[i]));
				}
			} catch (ParsingException e) {
				throw new RuntimeException("Error while parsing binding sites \"" + s + "\" on token \""+tokens[i]+"\". " +e.getMessage());
			}
		}
		return bindingSites.toArray(new GenomicRegion[bindingSites.size()]);
	}
	
	private static List<GenomicCoordinate> parseMutations(String s){
		String[] tokens = s.split(MUTATIONS_SEP);
		List<GenomicCoordinate> mutations = new ArrayList<GenomicCoordinate>();
		for(int i=0; i<tokens.length; i++){
			try{
				if(tokens[i].matches(COORD_REGEX)){
					mutations.add(GenomicCoordinate.parseString(tokens[i]));
				}
			} catch (ParsingException e){
				throw new RuntimeException("Error while parsing mutations \""+ s + "\" on token \""+tokens[i]+"\". "+e.getMessage());
			}
		}
		return mutations;
	}
	
	private static String bindingSitesToString(GenomicRegion[] bindingSites){
		String s = BINDING_SITE_SEP;
		for(GenomicRegion site : bindingSites){
			s += site.toString() + BINDING_SITE_SEP;
		}
		return s;
	}
	
	private static String mutationsToString(List<GenomicCoordinate> mutations){
		String s = MUTATIONS_SEP;
		for(GenomicCoordinate coord : mutations){
			s += coord.toString() + MUTATIONS_SEP;
		}
		return s;
	}
	
	private static String mutantToString(boolean mutant){
		if(mutant){
			return "mutant";
		}
		return "wildtype";
	}
	
	private static String strandToString(Strand s){
		return s.toString();
	}
	
	private final GenomicSequence m_Seq;
	private final GenomicRegion[] m_BindingSites;
	private final List<GenomicCoordinate> m_Mutations;
	private final String m_Name;
	private final boolean m_Mutant;
	private final Strand m_Strand;
	private final int m_Hash;
	
	public Probe(String seq){
		this(seq, null, null);
	}
	
	public Probe(Probe base, String seq, List<GenomicCoordinate> mutations, boolean mutant){
		this(seq, base.getRegion(), base.getBindingSites(), mutations, base.getName(), base.getStrand(), mutant);
	}
	
	public Probe(String seq, GenomicRegion region, GenomicRegion[] bindingSites){
		this(seq, region, bindingSites, null);
	}
	
	public Probe(String seq, String name){
		this(seq, null, new GenomicRegion[]{}, name);
	}

	public Probe(String seq, GenomicRegion region, GenomicRegion[] bindingSites, boolean mutant){
		this(seq, region, bindingSites, null, Strand.UNKNOWN, mutant);
	}
	
	public Probe(String seq, GenomicRegion region, GenomicRegion[] bindingSites, String name){
		this(seq, region, bindingSites, name, Strand.UNKNOWN, false);
	}
	
	public Probe(GenomicSequence seq, GenomicRegion ... bindingSites){
		this(seq, bindingSites, null);
	}
	
	public Probe(GenomicSequence seq, GenomicRegion[] bindingSites, String name){
		this(seq, bindingSites, name, false);
	}
	
	public Probe(GenomicSequence seq, GenomicRegion[] bindingSites, String name, Strand strand){
		this(seq, bindingSites, name, strand, false);
	}
	
	public Probe(GenomicSequence seq, GenomicRegion[] bindingSites, String name, boolean mutant){
		this(seq.getSequence(), seq.getRegion(), bindingSites, name, Strand.UNKNOWN, mutant);
	}
	
	public Probe(GenomicSequence seq, GenomicRegion[] bindingSites, String name, Strand strand, boolean mutant){
		this(seq.getSequence(), seq.getRegion(), bindingSites, name, strand, mutant);
	}
	
	public Probe(String seq, GenomicRegion region, GenomicRegion[] bindingSites, String name, Strand strand){
		this(seq, region, bindingSites, name, strand, false);
	}
	
	public Probe(String seq, GenomicRegion region, GenomicRegion[] bindingSites, String name, boolean mutant){
		this(seq, region, bindingSites, name, Strand.UNKNOWN, mutant);
	}
	
	public Probe(String seq, GenomicRegion region, GenomicRegion[] bindingSites, String name, Strand strand, boolean mutant){
		this(seq, region, bindingSites, new ArrayList<GenomicCoordinate>(), name, strand, mutant);
	}
	
	public Probe(String seq, GenomicRegion region, GenomicRegion[] bindingSites, List<GenomicCoordinate> mutations, String name, Strand strand, boolean mutant){
		GenomicRegion r = region != null ? region : new GenomicRegion(new Chromosome("0"), 1, seq.length());
		m_Seq = new GenomicSequence(seq, r);
		m_BindingSites = bindingSites == null ? new GenomicRegion[]{} : Arrays.copyOf(bindingSites, bindingSites.length);
		m_Mutations = new ArrayList<GenomicCoordinate>(mutations);
		m_Name = name!=null && !name.equals("") ? name : "Probe";
		m_Mutant = mutant;
		m_Strand = strand;
		HashCodeBuilder hashBuilder = new HashCodeBuilder(859, 911).append(m_Seq);
		for(GenomicRegion site : m_BindingSites){
			hashBuilder.append(site);
		}
		m_Hash = hashBuilder.toHashCode();
	}
	
	public int getLength(){
		return m_Seq.length();
	}
	
	public GenomicSequence asGenomicSequence(){
		return m_Seq;
	}
	
	public GenomicRegion[] getBindingSites(){
		return Arrays.copyOf(m_BindingSites, m_BindingSites.length);
	}
	
	public int numBindingSites(){
		return m_BindingSites.length;
	}
	
	public boolean isMutant(){
		return m_Mutant;
	}
	
	public List<GenomicCoordinate> getMutations(){
		return Collections.unmodifiableList(m_Mutations);
	}
	
	public int numMutations(){
		return m_Mutations.size();
	}
	
	public String getStrandAsString(){
		return m_Strand.toString();
	}
	
	public String getMutantAsString(){
		return mutantToString(m_Mutant);
	}
	
	public String getBindingSitesAsString(){
		return bindingSitesToString(m_BindingSites);
	}
	
	public String getSequence(){
		return m_Seq.getSequence();
	}
	
	public GenomicRegion getRegion(){
		return m_Seq.getRegion();
	}
	
	public Strand getStrand(){
		return m_Strand;
	}
	
	public String getName(){
		return m_Name;
	}
	
	public String getName(int number){
		return m_Name + NUMBER_SEP + number;
	}
	
	public boolean overlaps(Probe other){
		return m_Seq.overlaps(other.asGenomicSequence());
	}
	
	public boolean adjacentTo(Probe other){
		return m_Seq.adjacentTo(other.asGenomicSequence());
	}
	
	public Probe combine(Probe other, String combinedName){
		if(!this.overlaps(other) && !this.adjacentTo(other)){
			throw new RuntimeException("Cannot combine probe ("+this+") and probe ("+other+"). Probes are neither adjacent"
					+ " nor overlapping.");
		}
		boolean mutant = this.isMutant() || other.isMutant();
		Strand s = this.getStrand();
		if(other.getStrand() != this.getStrand() && other.getStrand() != Strand.UNKNOWN){
			if(this.getStrand() == Strand.UNKNOWN){
				s = other.getStrand();
			}else{
				other = ProbeUtils.reverseCompliment(other);
			}
		}
		GenomicSequence newSeq = this.asGenomicSequence().join(other.asGenomicSequence());
		Set<GenomicRegion> bindingSites = new TreeSet<GenomicRegion>();
		for(GenomicRegion site : m_BindingSites){
			bindingSites.add(site);
		}
		for(GenomicRegion site : other.m_BindingSites){
			bindingSites.add(site);
		}
		GenomicRegion[] combinedSites = bindingSites.toArray(new GenomicRegion[bindingSites.size()]);
		return new Probe(newSeq, combinedSites, combinedName, s, mutant);
	}
	
	public Probe subprobe(GenomicRegion subregion, String subname){
		return this.subprobe(subregion.getStart(), subregion.getEnd(), subname);
	}
	
	public Probe subprobe(GenomicCoordinate start, GenomicCoordinate end, String subName){
		GenomicSequence subseq = m_Seq.subsequence(start, end);
		GenomicRegion subregion = subseq.getRegion();
		Set<GenomicRegion> bindingSites = new TreeSet<GenomicRegion>();
		for(GenomicRegion site : m_BindingSites){
			if(subregion.contains(site)){
				bindingSites.add(site);
			}else if(subregion.overlaps(site)){
				bindingSites.add(subregion.union(site));
			}
		}
		return new Probe(subseq, bindingSites.toArray(new GenomicRegion[bindingSites.size()]), subName, this.getStrand(), this.isMutant());
	}
	
	@Override
	public String toString(){
		return m_Seq.getSequence() + "\t"
				+ m_Seq.getRegion() + "\t"
				+ this.getName() + "\t"
				+ strandToString(m_Strand) + "\t"
				+ mutantToString(m_Mutant) + "\t"
				+ bindingSitesToString(m_BindingSites) + "\t"
				+ mutationsToString(m_Mutations);
	}
	
	public String toString(int number){
		return m_Seq.getSequence() + "\t"
				+ m_Seq.getRegion() + "\t"
				+ this.getName(number) + "\t"
				+ strandToString(m_Strand) + "\t"
				+ mutantToString(m_Mutant) + "\t"
				+ bindingSitesToString(m_BindingSites) + "\t"
				+ mutationsToString(m_Mutations);
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}
	
	private int compareBindingSites(Probe other){
		for(int i=0; i<this.numBindingSites() && i<other.numBindingSites(); i++){
			GenomicRegion cur = m_BindingSites[i];
			GenomicRegion curOther = other.m_BindingSites[i];
			int comp = cur.compareTo(curOther);
			if(comp != 0){
				return comp;
			}
		}
		return this.numBindingSites() - other.numBindingSites();
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof Probe){
			Probe other = (Probe) o;
			return m_Seq.equals(other.m_Seq) && this.compareBindingSites(other) == 0;
		}
		return false;
	}
	
	@Override
	public int compareTo(Probe other){
		if(other == null){
			return -1;
		}
		int comp = m_Seq.compareTo(other.m_Seq);
		if(comp != 0){
			return comp;
		}
		comp = this.compareBindingSites(other);
		return comp;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
