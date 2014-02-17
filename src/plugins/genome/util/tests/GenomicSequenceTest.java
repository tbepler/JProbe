package plugins.genome.util.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import plugins.genome.util.Genome;
import plugins.genome.util.GenomicCoordinate;
import plugins.genome.util.GenomicRegion;
import plugins.genome.util.GenomicSequence;

public class GenomicSequenceTest extends junit.framework.TestCase{
	
	private Genome m_Genome;
	private List<String> chrNames;
	private Map<String, String> chrSeqs;
	
	protected void setUp(){
		chrNames = new ArrayList<String>();
		chrSeqs = new HashMap<String, String>();
		chrNames.add("1");
		chrSeqs.put("1", "abcdefghijklmnopqrstuvwxyz");
		chrNames.add("2");
		chrSeqs.put("2", "ACTCGACTGATCGACTGACTGCGCAGGACGCGCGCGCATATATATATATTTTTGCGCGCGACGACGGACGCCCGCGCTCGATGCATGTCATCGATCGATCGATCGACTGACGACATCACGATGACGCGATCGCGTATCGCT");
		chrNames.add("3");
		chrSeqs.put("3", "ieoa'nb'abawkj33991385bnasdba9313ba91s01395u1035bnckbjva;snq;wetnsbajdfb0135u10935u19n ascjvn9359	8293y98t");
		List<Integer> chrSizes = new ArrayList<Integer>();
		for(String chr : chrNames){
			chrSizes.add(chrSeqs.get(chr).length());
		}
		m_Genome = new Genome("TestGenome", chrNames, chrSizes);
	}
	
	public void testInstantiation(){
		GenomicRegion r1 = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("1", 6), m_Genome.newGenomicCoordinate("2", 23));
		String s1 = chrSeqs.get("1").substring(5)+chrSeqs.get("2").substring(0, 23);
		GenomicSequence test = new GenomicSequence(s1, r1);
		assertEquals(r1.getSize(), test.length());
		assertEquals(s1.length(), test.length());
		
		boolean error = false;
		try{
			test = new GenomicSequence("wrong length string", r1);
		} catch (Exception e){
			error = true;
		}
		assertTrue(error);
	}
	
	public void testBaseAt(){
		GenomicRegion r1 = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("1", 6), m_Genome.newGenomicCoordinate("2", 23));
		String s1 = chrSeqs.get("1").substring(5)+chrSeqs.get("2").substring(0, 23);
		GenomicSequence test = new GenomicSequence(s1, r1);
		assertEquals(s1.charAt(0), test.getBaseAt(m_Genome.newGenomicCoordinate("1", 6)));
		assertEquals(s1.charAt(s1.length()-1), test.getBaseAt(m_Genome.newGenomicCoordinate("2", 23)));
		GenomicCoordinate cTest = m_Genome.newGenomicCoordinate("2", 3);
		assertEquals(chrSeqs.get("2").charAt(2), test.getBaseAt(cTest));
		cTest = m_Genome.newGenomicCoordinate("1", 25);
		assertEquals(chrSeqs.get("1").charAt(24), test.getBaseAt(cTest));
		
		boolean error = false;
		cTest = m_Genome.newGenomicCoordinate("3", 5);
		try{
			System.out.println(test.getBaseAt(cTest));
		} catch (Exception e){
			error = true;
		}
		assertTrue(error);
	}

	public void testJoin(){
		GenomicRegion r1 = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("1", 6), m_Genome.newGenomicCoordinate("2", 23));
		String s1 = chrSeqs.get("1").substring(5)+chrSeqs.get("2").substring(0, 23);
		GenomicSequence t1 = new GenomicSequence(s1, r1);
		GenomicRegion r2 = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("2", 7), m_Genome.newGenomicCoordinate("3", 17));
		String s2 = chrSeqs.get("2").substring(6) + chrSeqs.get("3").substring(0, 17);
		GenomicSequence t2 = new GenomicSequence(s2, r2);
		
		assertEquals(t1.join(t2), t2.join(t1));
		GenomicSequence joined = t1.join(t2);
		assertEquals(r1.union(r2), joined.getRegion());
		assertEquals(chrSeqs.get("1").substring(5)+chrSeqs.get("2")+chrSeqs.get("3").substring(0,17), joined.getSequence());
		
		GenomicRegion r3 = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("2", 32), m_Genome.newGenomicCoordinate("3", 17));
		String s3 = chrSeqs.get("2").substring(31) + chrSeqs.get("3").substring(0,17);
		GenomicSequence t3 = new GenomicSequence(s3, r3);
		boolean error = false;
		try{
			t1.join(t3);
		} catch (Exception e){
			error = true;
		}
		assertTrue(error);
	}
	
	public void testSplit(){
		GenomicRegion r1 = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("1", 6), m_Genome.newGenomicCoordinate("2", 23));
		String s1 = chrSeqs.get("1").substring(5)+chrSeqs.get("2").substring(0, 23);
		GenomicSequence test = new GenomicSequence(s1, r1);
		
		GenomicCoordinate splitCoord = m_Genome.newGenomicCoordinate("1", 23);
		String leftSeq = chrSeqs.get("1").substring(5, 22);
		String rightSeq = chrSeqs.get("1").substring(22)+chrSeqs.get("2").substring(0,23);
		GenomicRegion[] regionSplit = r1.split(splitCoord);
		
		GenomicSequence[] seqSplit = test.split(splitCoord);
		assertEquals(regionSplit[0], seqSplit[0].getRegion());
		assertEquals(regionSplit[1], seqSplit[1].getRegion());
		assertEquals(leftSeq, seqSplit[0].getSequence());
		assertEquals(rightSeq, seqSplit[1].getSequence());
	}
	
	public void testSubsequence(){
		GenomicRegion r1 = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("1", 6), m_Genome.newGenomicCoordinate("2", 23));
		String s1 = chrSeqs.get("1").substring(5)+chrSeqs.get("2").substring(0, 23);
		GenomicSequence test = new GenomicSequence(s1, r1);
		
		GenomicCoordinate splitCoord = m_Genome.newGenomicCoordinate("1", 23);
		String leftSeq = chrSeqs.get("1").substring(5, 22);
		String rightSeq = chrSeqs.get("1").substring(22)+chrSeqs.get("2").substring(0,23);
		GenomicRegion[] regionSplit = r1.split(splitCoord);
		
		GenomicSequence left = test.subsequence(test.getStart(), splitCoord.decrement(1));
		assertEquals(regionSplit[0], left.getRegion());
		assertEquals(leftSeq, left.getSequence());
		
		GenomicSequence right = test.subsequence(splitCoord);
		assertEquals(regionSplit[1], right.getRegion());
		assertEquals(rightSeq, right.getSequence());
	}
	
	
	
	
	
	
}