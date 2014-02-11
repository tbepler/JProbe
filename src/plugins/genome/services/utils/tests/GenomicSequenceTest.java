package plugins.genome.services.utils.tests;

import java.util.Comparator;

import plugins.genome.services.utils.Chromosome;
import plugins.genome.services.utils.GenomicCoordinate;
import plugins.genome.services.utils.GenomicRegion;
import plugins.genome.services.utils.GenomicSequence;

public class GenomicSequenceTest extends junit.framework.TestCase{
	
	private Comparator<GenomicCoordinate> m_LocComp = new Comparator<GenomicCoordinate>(){

		@Override
		public int compare(GenomicCoordinate o1, GenomicCoordinate o2) {
			return o1.compareTo(o2);
		}
		
	};
	
	public void testSplit(){
		Chromosome chr1 = new Chromosome("1");
		Chromosome chr2 = new Chromosome("2");
		String chr1Seq = "ATGATAGAGTATAGATACCCCGCCTCGCTGCTATCGACGCTGACTGATTCGATGATGACTGATCGATCGTAGATGATGTCGATGTTATATATAGGCGCGCTCGATCGACTGACTGACTATCTC";
		String chr2Seq = "CAGCTAGCTAGCGCGCGCTAATTAGACGATGAGCGCTAGCGTATATCGCGCCGCTCAGGCAAGACCAGAGCCATCAGTCAGCGCGCGTCAGCAGCAGTACTCAGACTACTACGCATAG"
				+ "GATCGACTACGATCAGCTAGCTAGAGCTAATTATAGATATTATAGCGCGCGTCGACGTCAGACGACGAT";
		GenomicRegion reg1 = new GenomicRegion(new GenomicCoordinate(chr1, 13), new GenomicCoordinate(chr1, chr1Seq.length()));
		GenomicRegion reg2 = new GenomicRegion(new GenomicCoordinate(chr2, 1), new GenomicCoordinate(chr2, 26));
		GenomicSequence seq = new GenomicSequence(chr1Seq.substring(12)+chr2Seq.substring(0, 26), reg1.union(reg2, m_LocComp));
		GenomicSequence left = new GenomicSequence(chr1Seq.substring(12), reg1);
		GenomicSequence right = new GenomicSequence(chr2Seq.substring(0, 26), reg2);
		GenomicSequence[] split = seq.split(new GenomicCoordinate(chr2, 1));
		assertEquals(left.getSequence(), split[0].getSequence());
		assertEquals(left.getRegion(), split[0].getRegion());
		assertEquals(left, split[0]);
		assertEquals(right.getSequence(), split[1].getSequence());
		assertEquals(right.getRegion(), split[1].getRegion());
		assertEquals(right, split[1]);
		assertFalse(right.equals(split[0]));
		assertFalse(left.equals(split[1]));
		
		boolean error = false;
		try{
			split = seq.split(new GenomicCoordinate(chr1, 5));
		} catch (Exception e){
			error = true;
		}
		assertTrue(error);
		
	}
	
	public void testAppend(){
		Chromosome chr1 = new Chromosome("1");
		Chromosome chr2 = new Chromosome("2");
		Chromosome chr3 = new Chromosome("3");
		Chromosome chr4 = new Chromosome("4");
		String chr1Seq = "ASDFASDFASDLASKNTAWKERLKWN;LKAJBO;IAAKER;KANSAC.,VCVCVNASLKNFLQKFNK";
		String chr2Seq = "LNALKKWNQQNT;QLWNEGKDSNLKDNGGGGGGGGGGGGGGGGGTTTTTASLDFA";
		String chr3Seq = "AGTCTGAGTCGTGTGTGGTACCTGAGAGTC";
		String chr4Seq = "PPQOWEAEROPAWEMRKNEWRRRRRRRRRRRAD;JSA;PSAVOJA;ELFSDNKFDJFBDFJDBIFAUDBIBKJDFKCNVLKCNVHDDFGASDFGXZYCVASGUZYXVCUAYVUAHFAHFBK";
		GenomicRegion reg1 = new GenomicRegion(new GenomicCoordinate(chr1, 10), new GenomicCoordinate(chr2, 22));
		GenomicRegion reg2 = new GenomicRegion(new GenomicCoordinate(chr2, 10), new GenomicCoordinate(chr3, 5));
		GenomicRegion reg3 = new GenomicRegion(new GenomicCoordinate(chr3, 6), new GenomicCoordinate(chr3, chr3Seq.length()-3));
		
		GenomicSequence seq1 = new GenomicSequence(chr1Seq.substring(9)+chr2Seq.substring(0, 22), reg1);
		GenomicSequence seq2 = new GenomicSequence(chr2Seq.substring(9)+chr3Seq.substring(0, 5), reg2);
		GenomicSequence seq3 = new GenomicSequence(chr3Seq.substring(5, chr3Seq.length()-3), reg3);
		
		GenomicSequence test = seq1.append(seq2, m_LocComp);
		assertEquals(chr1Seq.substring(9)+chr2Seq+chr3Seq.substring(0,5), test.getSequence());
		assertEquals(reg1.union(reg2, m_LocComp), test.getRegion());
		GenomicCoordinate testLoc = new GenomicCoordinate(chr3, 2);
		assertTrue(test.contains(testLoc, m_LocComp));
		assertFalse(seq1.contains(testLoc, m_LocComp));
		assertTrue(seq2.contains(testLoc, m_LocComp));
		assertEquals(chr3Seq.charAt(1), test.getBaseAt(testLoc));
		testLoc = new GenomicCoordinate(chr2, 2);
		assertTrue(test.contains(testLoc, m_LocComp));
		assertTrue(seq1.contains(testLoc, m_LocComp));
		assertFalse(seq2.contains(testLoc, m_LocComp));
		assertEquals(chr2Seq.charAt(1), test.getBaseAt(testLoc));
		
		test = test.append(seq3, m_LocComp);
		assertEquals(chr1Seq.substring(9)+chr2Seq+chr3Seq.substring(0,chr3Seq.length()-3), test.getSequence());
		assertEquals(reg1.union(reg2.union(reg3, m_LocComp), m_LocComp), test.getRegion());
		testLoc = new GenomicCoordinate(chr3, 6);
		assertTrue(test.contains(testLoc, m_LocComp));
		assertEquals(chr3Seq.charAt(5), test.getBaseAt(testLoc));
		assertFalse(seq2.contains(testLoc, m_LocComp));
		testLoc = new GenomicCoordinate(chr3, 5);
		assertTrue(test.contains(testLoc, m_LocComp));
		assertTrue(seq2.contains(testLoc, m_LocComp));
		assertFalse(seq3.contains(testLoc, m_LocComp));
		
	}
	
	public void testContainsAndBaseAt(){
		GenomicCoordinate start = new GenomicCoordinate(new Chromosome("1"), 250);
		GenomicCoordinate end = new GenomicCoordinate(new Chromosome("2"), 27);
		String s = "AAAAAAAAAAAAAAAAAATTTTTTTTTTTTTTTTTTTTTTTGGGGGGGGGGGGGGGGGGGGGCCCCCCCCCCCCCCCCCCCCCCCTATAGCTCGTCTAGTCAGTAAAAAAAAAGGGGGGGGGTTTTTTTTTTTTTTTTTT"
				+ "GGGGGGGGGGGTTTAGACTGTAGTAGTAGTAGTAGTAGTAGTAGTAGTAGTAGTAG";
		GenomicSequence seq = new GenomicSequence(s, new GenomicRegion(start, end));

		assertTrue(seq.contains(new GenomicCoordinate(new Chromosome("1"), 270), m_LocComp));
		assertEquals(s.charAt(270-250), seq.getBaseAt(new GenomicCoordinate(new Chromosome("1"), 270)));
		assertTrue(seq.contains(new GenomicCoordinate(new Chromosome("2"), 15), m_LocComp));
		assertEquals(s.charAt(s.length() - 12 -1), seq.getBaseAt(new GenomicCoordinate(new Chromosome("2"), 15)));
		Chromosome chr1 = new Chromosome("1");
		Chromosome chr2 = new Chromosome("2");
		GenomicCoordinate test = new GenomicCoordinate(chr2, 53);
		assertFalse(seq.contains(test, m_LocComp));
		boolean error = false;
		try{
			System.out.println(seq.getBaseAt(test));
		} catch (Exception e){
			error = true;
		}
		assertTrue(error);
		test = new GenomicCoordinate(chr1, 176);
		assertFalse(seq.contains(test, m_LocComp));
		error = false;
		try{
			System.out.println(seq.getBaseAt(test));
		} catch (Exception e){
			error = true;
		}
		assertTrue(error);
		test = new GenomicCoordinate(new Chromosome("3"), 12);
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