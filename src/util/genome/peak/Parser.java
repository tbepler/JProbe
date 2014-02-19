package util.genome.peak;

import java.util.HashMap;
import java.util.Map;

import util.genome.ParsingException;
import util.genome.Strand;

public class Parser {
	
	public static final String WHITESPACE_REGEX = "\\s";

	public static Peak parsePeak(String s) throws ParsingException{
		try{
			Map<String, Object> optional = new HashMap<String, Object>();
			String chrom = "";
			long chromStart = -1;
			long chromEnd = -1;
			double signalVal = -1;
			String[] split = s.split(WHITESPACE_REGEX);
			for(int i=1; i<=10 && i<=split.length; i++){
				String cur = split[i-1];
				switch(i){
				case 1:
					chrom = cur;
					break;
				case 2:
					chromStart = Long.parseLong(cur);
					break;
				case 3:
					chromEnd = Long.parseLong(cur);
					break;
				case 4:
					optional.put(Peak.NAME, cur);
					break;
				case 5:
					try{
						optional.put(Peak.SCORE, Integer.parseInt(cur));
					} catch (Exception e){
						signalVal = Double.parseDouble(cur);
						return new Peak(chrom, chromStart, chromEnd, signalVal, optional);
					}
					break;
				case 6:
					optional.put(Peak.STRAND, Strand.parseStrand(cur));
					break;
				case 7:
					signalVal = Double.parseDouble(cur);
					break;
				case 8:
					optional.put(Peak.PVAL, Double.parseDouble(cur));
					break;
				case 9:
					optional.put(Peak.QVAL, Double.parseDouble(cur));
				case 10:
					optional.put(Peak.POINT_SOURCE, Integer.parseInt(cur));
				default:
					break;
				}
			}
			if(chrom.equals("") || chromStart == -1 || chromEnd == -1 || signalVal == -1){
				throw new ParsingException("Error: peaks require values for chromosome, chromosomeStart, chromosomeEnd, and signalValue at the minimum");
			}
			return new Peak(chrom, chromStart, chromEnd, signalVal, optional);
		} catch(ParsingException e){
			throw e;
		} catch(Exception e){
			throw new ParsingException(e);
		}
	}
	
	
	
	
	
	
}
