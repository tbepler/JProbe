package plugins.genome.services.utils.tests;

import java.util.Comparator;

import plugins.genome.services.utils.Chromosome;
import plugins.genome.services.utils.GenomicLocation;
import plugins.genome.services.utils.GenomicRegion;
import plugins.genome.services.utils.GenomicSequence;

public class GenomicSequenceTest extends junit.framework.TestCase{
	
	private Comparator<GenomicLocation> m_LocComp = new Comparator<GenomicLocation>(){

		@Override
		public int compare(GenomicLocation o1, GenomicLocation o2) {
			return o1.compareTo(o2);
		}
		
	};
	
	public void testAppend(){
		Chromosome chr1 = new Chromosome("1");
		Chromosome chr2 = new Chromosome("2");
		Chromosome chr3 = new Chromosome("3");
		Chromosome chr4 = new Chromosome("4");
		String chr1Seq = "ASDFASDFASDLASKNTAWKERLKWN;LKAJBO;IAAKER;KANSAC.,VCVCVNASLKNFLQKFNK";
		String chr2Seq = "LNALKKWNQQNT;QLWNEGKDSNLKDNGGGGGGGGGGGGGGGGGTTTTTASLDFA";
		String chr3Seq = "AGTCTGAGTCGTGTGTGGTACCTGAGAGTC";
		String chr4Seq = "PPQOWEAEROPAWEMRKNEWRRRRRRRRRRRAD;JSA;PSAVOJA;ELFSDNKFDJFBDFJDBIFAUDBIBKJDFKCNVLKCNVHDDFGASDFGXZYCVASGUZYXVCUAYVUAHFAHFBK";
		GenomicRegion reg1 = new GenomicRegion(new GenomicLocation(chr1, 10), new GenomicLocation(chr2, 22));
		GenomicRegion reg2 = new GenomicRegion(new GenomicLocation(chr2, 10), new GenomicLocation(chr3, 5));
		GenomicRegion reg3 = new GenomicRegion(new GenomicLocation(chr3, 6), new GenomicLocation(chr3, chr3Seq.length()-3));
		
		GenomicSequence seq1 = new GenomicSequence(chr1Seq.substring(9)+chr2Seq.substring(0, 22), reg1);
		GenomicSequence seq2 = new GenomicSequence(chr2Seq.substring(9)+chr3Seq.substring(0, 5), reg2);
		GenomicSequence seq3 = new GenomicSequence(chr3Seq.substring(5, chr3Seq.length()-3), reg3);
		
		GenomicSequence test = seq1.append(seq2, m_LocComp);
		assertEquals(chr1Seq.substring(9)+chr2Seq+chr3Seq.substring(0,5), test.getSequence());
		assertEquals(reg1.union(reg2, m_LocComp), test.getRegion());
		GenomicLocation testLoc = new GenomicLocation(chr3, 2);
		assertTrue(test.contains(testLoc, m_LocComp));
		assertFalse(seq1.contains(testLoc, m_LocComp));
		assertTrue(seq2.contains(testLoc, m_LocComp));
		assertEquals(chr3Seq.charAt(1), test.getBaseAt(testLoc));
		testLoc = new GenomicLocation(chr2, 2);
		assertTrue(test.contains(testLoc, m_LocComp));
		assertTrue(seq1.contains(testLoc, m_LocComp));
		assertFalse(seq2.contains(testLoc, m_LocComp));
		assertEquals(chr2Seq.charAt(1), test.getBaseAt(testLoc));
		
	}
	
	public void testContainsAndBaseAt(){
		GenomicLocation start = new GenomicLocation(new Chromosome("1"), 250);
		GenomicLocation end = new GenomicLocation(new Chromosome("2"), 27);
		String s = "AAAAAAAAAAAAAAAAAATTTTTTTTTTTTTTTTTTTTTTTGGGGGGGGGGGGGGGGGGGGGCCCCCCCCCCCCCCCCCCCCCCCTATAGCTCGTCTAGTCAGTAAAAAAAAAGGGGGGGGGTTTTTTTTTTTTTTTTTT"
				+ "GGGGGGGGGGGTTTAGACTGTAGTAGTAGTAGTAGTAGTAGTAGTAGTAGTAGTAG";
		GenomicSequence seq = new GenomicSequence(s, new GenomicRegion(start, end));

		assertTrue(seq.contains(new GenomicLocation(new Chromosome("1"), 270), m_LocComp));
		assertEquals(s.charAt(270-250), seq.getBaseAt(new GenomicLocation(new Chromosome("1"), 270)));
		assertTrue(seq.contains(new GenomicLocation(new Chromosome("2"), 15), m_LocComp));
		assertEquals(s.charAt(s.length() - 12 -1), seq.getBaseAt(new GenomicLocation(new Chromosome("2"), 15)));
		Chromosome chr1 = new Chromosome("1");
		Chromosome chr2 = new Chromosome("2");
		GenomicLocation test = new GenomicLocation(chr2, 53);
		assertFalse(seq.contains(test, m_LocComp));
		boolean error = false;
		try{
			System.out.println(seq.getBaseAt(test));
		} catch (Exception e){
			error = true;
		}
		assertTrue(error);
		test = new GenomicLocation(chr1, 176);
		assertFalse(seq.contains(test, m_LocComp));
		error = false;
		try{
			System.out.println(seq.getBaseAt(test));
		} catch (Exception e){
			error = true;
		}
		assertTrue(error);
		test = new GenomicLocation(new Chromosome("3"), 12);
		assertFalse(seq.contains(test, m_LocComp));
		error = false;
		try{
			System.out.println(seq.getBaseAt(test));
		} catch (Exception e){
			error = true;
		}
		assertTrue(error);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}