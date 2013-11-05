package test;


import datatypes.Group;
import datatypes.Peak;
import readwrite.FileFormat;
import readwrite.TragedyFileReader;

public class TestFileReader extends junit.framework.TestCase{
	
	public void testReadFactory(){
		TragedyFileReader reader = new TragedyFileReader();
		boolean error = false;
		try{
			String[] readTypes = reader.getValidReadFormats(Peak.class);
			String[] peakTypes = Peak.PEAK_READ_FORMATS;
			assertEquals(peakTypes.length, readTypes.length);
			for(int i=0; i<peakTypes.length; i++){
				assertEquals(peakTypes[i], readTypes[i]);
			}
		} catch(Exception e){
			e.printStackTrace();
			error = true;
		}
		assertFalse(error);
	}
	
	public void testReadPeakGroupXML(){
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
		} catch (Exception e) {
			error = true;
			e.printStackTrace();
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
		} catch (Exception e) {
			error = true;
			e.printStackTrace();
		}
		assertFalse(error);
	}

	
	public void testReadPeakGroupBED(){
		TragedyFileReader reader = new TragedyFileReader();
		boolean error = false;
		try{
			Group<Peak> test = reader.readPeakGroup("src/peakgrouptest3.bed", FileFormat.BED);
			assertEquals(5, test.size());
			Peak p = test.get(1);
			assertEquals(p.getSeq(), "");
			assertEquals(p.getChr(), "chr1");
			assertEquals(p.getStart(), 1);
			assertEquals(p.getEnd(), 5);
			
			p = test.get(2);
			assertEquals(p.getSeq(), "");
			assertEquals(p.getChr(), "chr2");
			assertEquals(p.getStart(), 100);
			assertEquals(p.getEnd(), 150);
			
			p = test.get(3);
			assertEquals(p.getSeq(), "");
			assertEquals(p.getChr(), "chr12");
			assertEquals(p.getStart(), 15);
			assertEquals(p.getEnd(), 100);
			
			p = test.get(4);
			assertEquals(p.getSeq(), "");
			assertEquals(p.getChr(), "chr20");
			assertEquals(p.getStart(), 10500);
			assertEquals(p.getEnd(), 15619);
			
			p = test.get(5);
			assertEquals(p.getSeq(), "");
			assertEquals(p.getChr(), "chrY");
			assertEquals(p.getStart(), 2567);
			assertEquals(p.getEnd(), 3401);
		} catch (Exception e){
			error = true;
			e.printStackTrace();
		}
		assertFalse(error);
	}
	
	
}
