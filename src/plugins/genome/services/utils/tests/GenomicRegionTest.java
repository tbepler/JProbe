package plugins.genome.services.utils.tests;

import plugins.genome.services.utils.Chromosome;
import plugins.genome.services.utils.GenomicCoordinate;
import plugins.genome.services.utils.GenomicRegion;

public class GenomicRegionTest extends junit.framework.TestCase{
	
	
	public void testUnion(){
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
		
		GenomicRegion reg12Union = new GenomicRegion(new GenomicCoordinate(chr1, 10), new GenomicCoordinate(chr3, 5));
		GenomicRegion reg23Union = new GenomicRegion(new GenomicCoordinate(chr2,10), new GenomicCoordinate(chr3, chr3Seq.length()-3));
		GenomicRegion reg123Union = new GenomicRegion(new GenomicCoordinate(chr1,10), new GenomicCoordinate(chr3, chr3Seq.length()-3));
		
		assertEquals(reg12Union, reg1.union(reg2));
		assertEquals(reg12Union, reg2.union(reg1));
		assertEquals(reg23Union, reg2.union(reg3));
		assertEquals(reg23Union, reg3.union(reg2));
		assertEquals(reg123Union, reg12Union.union(reg3));
		assertEquals(reg123Union, reg23Union.union(reg1));
		assertEquals(reg123Union, reg1.union(reg2.union(reg3)));
		
		assertFalse(reg23Union.equals(reg1.union(reg2)));
		assertFalse(reg12Union.equals(reg2.union(reg3)));
		assertFalse(reg123Union.equals(reg1));
	}
	
}
