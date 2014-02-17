package plugins.genome.util.tests;

import java.util.ArrayList;
import java.util.List;

import plugins.genome.util.Genome;
import plugins.genome.util.GenomicCoordinate;
import plugins.genome.util.GenomicRegion;

public class GenomicRegionTest extends junit.framework.TestCase{
	
	protected Genome m_Genome;
	protected Genome m_OtherGenome;
	
	protected void setUp(){
		List<String> chrNames = genChrNames();
		List<Integer> chrSizes = genChrSizes();
		m_Genome = new Genome("TestGenome", chrNames, chrSizes);
		chrNames.remove(3);
		chrSizes.remove(3);
		m_OtherGenome = new Genome("DifferentTestGenome", chrNames, chrSizes);
	}
	
	protected List<String> genChrNames(){
		List<String> names = new ArrayList<String>();
		names.add("1");
		names.add("2");
		names.add("3");
		names.add("4");
		return names;
	}
	
	protected List<Integer> genChrSizes(){
		List<Integer> sizes = new ArrayList<Integer>();
		sizes.add(100);
		sizes.add(290);
		sizes.add(45);
		sizes.add(130);
		return sizes;
	}
	
	public void testInstantiation(){
		GenomicRegion test = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("2", 1), m_Genome.newGenomicCoordinate("2", 215));
		assertEquals(215-1+1, test.getSize());
		
		test = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("1", 20), m_Genome.newGenomicCoordinate("2", 250));
		assertEquals(100-20+1 + 250, test.getSize());
		assertEquals(m_Genome.newGenomicCoordinate("1", 20), test.getStart());
		assertEquals(m_Genome.newGenomicCoordinate("2", 250), test.getEnd());
		assertFalse(m_OtherGenome.newGenomicCoordinate("1", 20).equals(test.getStart()));
		assertTrue(test.getStart().compareTo(test.getEnd()) < 0);
		
		test = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("2", 250), m_Genome.newGenomicCoordinate("1", 20));
		assertEquals(100-20+1 + 250, test.getSize());
		assertEquals(m_Genome.newGenomicCoordinate("1", 20), test.getStart());
		assertEquals(m_Genome.newGenomicCoordinate("2", 250), test.getEnd());
		assertFalse(m_OtherGenome.newGenomicCoordinate("1", 20).equals(test.getStart()));
		assertTrue(test.getStart().compareTo(test.getEnd()) < 0);
		
		test = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("2", 36), m_Genome.newGenomicCoordinate("4", 65));
		assertEquals(290-36+1 + 45 + 65, test.getSize());
	
	}
		
	public void testIntersection(){
		GenomicRegion test1 = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("1", 20), m_Genome.newGenomicCoordinate("3", 25));
		assertTrue(test1.contains(test1));
		assertTrue(test1.overlaps(test1));
		assertEquals(test1.getSize(), test1.getOverlap(test1));
		assertEquals(test1, test1.intersection(test1));
		
		GenomicRegion test2 = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("1", 76), m_Genome.newGenomicCoordinate("2", 90));
		assertTrue(test1.contains(test2));
		assertFalse(test2.contains(test1));
		assertTrue(test1.overlaps(test2));
		assertTrue(test2.overlaps(test1));
		assertEquals(test2.getSize(), test1.getOverlap(test2));
		assertEquals(test1.getOverlap(test2), test2.getOverlap(test1));
		assertEquals(test2, test1.intersection(test2));
		assertEquals(test2, test2.intersection(test1));
		
		test2 = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("2", 246), m_Genome.newGenomicCoordinate("4", 117));
		assertFalse(test1.contains(test2));
		assertFalse(test2.contains(test1));
		assertTrue(test1.overlaps(test2));
		assertTrue(test2.overlaps(test1));
		assertEquals(test1.getOverlap(test2), test2.getOverlap(test1));
		assertEquals(290-246+1 + 25, test1.getOverlap(test2));
		assertEquals(test1.intersection(test2), test2.intersection(test1));
		GenomicRegion intersection = test1.intersection(test2);
		assertEquals(test1.getOverlap(test2), intersection.getSize());
		assertEquals(m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("2", 246), m_Genome.newGenomicCoordinate("3", 25)), intersection);
	}
	
	public void testUnion(){
		GenomicRegion test1 = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("1", 20), m_Genome.newGenomicCoordinate("2", 250));
		assertEquals(test1, test1.union(test1));
		
		GenomicRegion test2 = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("2", 172), m_Genome.newGenomicCoordinate("4", 34));
		assertEquals(test1.union(test2), test2.union(test1));
		GenomicRegion union = test1.union(test2);
		assertEquals(m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("1", 20), m_Genome.newGenomicCoordinate("4", 34)), union);
		assertEquals(test1.getSize()+test2.getSize()-test1.getOverlap(test2), union.getSize());
		
	}
	
	public void testSplit(){
		GenomicRegion test = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("2", 72), m_Genome.newGenomicCoordinate("4", 68));
		GenomicCoordinate splitCoord = m_Genome.newGenomicCoordinate("3", 17);
		GenomicRegion left = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("2", 72), m_Genome.newGenomicCoordinate("3", 16));
		GenomicRegion right = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("3", 17), m_Genome.newGenomicCoordinate("4", 68));
		assertEquals(test.getSize(), left.getSize()+right.getSize());
		GenomicRegion[] split = test.split(splitCoord);
		assertEquals(left, split[0]);
		assertEquals(right, split[1]);
		assertEquals(test.getSize(), split[0].getSize()+split[1].getSize());
		
		assertEquals(test, test.split(test.getStart())[0]);
		assertEquals(test, test.split(test.getStart())[1]);
		assertEquals(test.split(test.getStart())[0], test.split(test.getStart())[1]);
	}
	
	public void testAdjacent(){
		GenomicRegion test1 = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("2", 21), m_Genome.newGenomicCoordinate("3", 45));
		GenomicRegion test2 = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("4", 1), m_Genome.newGenomicCoordinate("4", 49));
		GenomicRegion test3 = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("1", 77), m_Genome.newGenomicCoordinate("2", 20));
		GenomicRegion test4 = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("1", 7), m_Genome.newGenomicCoordinate("2", 13));
		GenomicRegion test5 = m_Genome.newGenomicRegion(m_Genome.newGenomicCoordinate("3", 44), m_Genome.newGenomicCoordinate("4", 118));
		
		assertFalse(test1.adjacentTo(test1));
		assertTrue(test1.adjacentTo(test2));
		assertTrue(test2.adjacentTo(test1));
		assertTrue(test1.adjacentTo(test3));
		assertTrue(test3.adjacentTo(test1));
		assertFalse(test1.adjacentTo(test4));
		assertFalse(test4.adjacentTo(test1));
		assertFalse(test1.adjacentTo(test1));
		assertFalse(test5.adjacentTo(test1));
				
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
