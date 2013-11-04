package test;


import datatypes.Group;
import datatypes.Peak;
import exceptions.FileReadException;
import exceptions.FormatNotSupportedException;
import readwrite.FileFormat;
import readwrite.TragedyFileReader;

public class TestFileReader extends junit.framework.TestCase{
	
	
	public void testReadPeakGroup(){
		TragedyFileReader reader = new TragedyFileReader();
		boolean error = false;
		try {
			Group<Peak> test = reader.readPeakGroup("src/peakgrouptest1.xml", FileFormat.XML);
			assertEquals(1,test.size());
			Peak p = test.get(1);
			assertEquals(p.getSeq(), "AAGGTTGGTTAAATTAG");
			assertEquals(p.getChr(), "chr1");
			assertEquals(p.getStart(), 100);
			assertEquals(p.getEnd(), 116);
		} catch (FormatNotSupportedException e) {
			error = true;
		} catch (FileReadException e) {
			error = true;
		}
		assertFalse(error);
		
		try {
			Group<Peak> test = reader.readPeakGroup("src/peakgrouptest2.xml", FileFormat.XML);
			assertEquals(3,test.size());
			Peak p = test.get(1);
			assertEquals(p.getSeq(), "AAGGTTGGTTAAATTAG");
			assertEquals(p.getChr(), "chr1");
			assertEquals(p.getStart(), 100);
			assertEquals(p.getEnd(), 116);
			
			p = test.get(2);
			assertEquals(p.getSeq(), "");
			assertEquals(p.getChr(), "chrX");
			assertEquals(p.getStart(), 151616);
			assertEquals(p.getEnd(), 160000);
			
			p = test.get(3);
			assertEquals(p.getSeq(), "");
			assertEquals(p.getChr(), "chr17");
			assertEquals(p.getStart(), 12);
			assertEquals(p.getEnd(), 1254);
		} catch (FormatNotSupportedException e) {
			error = true;
		} catch (FileReadException e) {
			error = true;
		}
		assertFalse(error);
	}
	
	
}
