package util.genome.peak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.genome.GenomicRegion;
import util.genome.GenomicSequence;
import util.genome.ParsingException;
import util.genome.Strand;
import util.genome.reader.GenomeReader;
import util.genome.reader.query.LocationBoundedSequenceQuery;
import util.genome.reader.query.LocationQuery;
import util.genome.reader.query.SequenceQuery;

public class Parser {
	
	public static final String WHITESPACE_REGEX = "\\s";
	public static final String[][] PEAK_FORMATS = new String[][]{
		{"BED format (.bed)", "bed"},
		{"ENCODE peak (.*)", "*"}
	};
	public static final String[][] PEAK_SEQ_FORMATS = new String[][]{
		{"PeakSeq format (.peakSeq, .*)", "peakSeq", "*"}
	};
	
	public static PeakSequenceGroup readFromGenome(GenomeReader reader, PeakGroup peaks){
		List<PeakSequence> peakSeqs = new ArrayList<PeakSequence>();
		List<LocationQuery> queries = new ArrayList<LocationQuery>();
		for(Peak p : peaks){
			queries.add(new PeakQuery(p, reader.getGenome(), peakSeqs));
		}
		reader.read(queries, new ArrayList<SequenceQuery>(), new ArrayList<LocationBoundedSequenceQuery>());
		return new PeakSequenceGroup(peakSeqs);
	}
	
	public static PeakSequence parsePeakSequence(String s) throws ParsingException{
		try{
			String[] split = s.split(WHITESPACE_REGEX);
			String sequence = "";
			GenomicRegion region = null;
			String name = "";
			int score = -1;
			Strand strand = Strand.UNKNOWN;
			double signalVal = -1;
			double pVal = -1;
			double qVal = -1;
			int pointSource = -1;
			for(int i=1; i<=PeakSequence.ELEMENTS && i<=split.length; i++){
				String cur = split[i-1];
				switch(i){
				case 1:
					sequence = cur;
					break;
				case 2:
					region = GenomicRegion.parseString(cur);
					break;
				case 3:
					name = cur;
					break;
				case 4:
					score = Integer.parseInt(cur);
					break;
				case 5:
					strand = Strand.parseStrand(cur);
					break;
				case 6:
					signalVal = Double.parseDouble(cur);
					break;
				case 7:
					pVal = Double.parseDouble(cur);
					break;
				case 8:
					qVal = Double.parseDouble(cur);
					break;
				case 9:
					pointSource = Integer.parseInt(cur);
					break;
				default:
					break;
				}
			}
			return new PeakSequence(new GenomicSequence(sequence, region), name, score, strand, signalVal, pVal, qVal, pointSource);
		} catch (Exception e){
			throw new ParsingException(e);
		}
	}
	
	public static PeakSequenceGroup parsePeakSeqGroup(InputStream s) throws ParsingException{
		List<PeakSequence> peakSeqs = new ArrayList<PeakSequence>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(s));
		String line;
		try {
			while((line = reader.readLine()) != null){
				try{
					peakSeqs.add(parsePeakSequence(line));
				} catch (Exception e){
					//do nothing
				}
			}
		} catch (IOException e) {
			//do nothing
		}
		return new PeakSequenceGroup(peakSeqs);
	}

	public static Peak parsePeak(String s) throws ParsingException{
		try{
			Map<String, Object> optional = new HashMap<String, Object>();
			String chrom = "";
			long chromStart = -1;
			long chromEnd = -1;
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
						optional.put(Peak.QVAL, Double.parseDouble(cur));
						return new Peak(chrom, chromStart, chromEnd, optional);
					}
					break;
				case 6:
					optional.put(Peak.STRAND, Strand.parseStrand(cur));
					break;
				case 7:
					optional.put(Peak.SIGNAL_VAL, Double.parseDouble(cur));
					break;
				case 8:
					optional.put(Peak.PVAL, Double.parseDouble(cur));
					break;
				case 9:
					optional.put(Peak.QVAL, Double.parseDouble(cur));
					break;
				case 10:
					optional.put(Peak.POINT_SOURCE, Integer.parseInt(cur));
					break;
				default:
					break;
				}
			}
			if(chrom.equals("") || chromStart == -1 || chromEnd == -1){
				throw new ParsingException("Error: peaks require values for chromosome, chromosomeStart, and chromosomeEnd at the minimum");
			}
			return new Peak(chrom, chromStart, chromEnd, optional);
		} catch(ParsingException e){
			throw e;
		} catch(Exception e){
			throw new ParsingException(e);
		}
	}
	
	public static PeakGroup parsePeakGroup(InputStream s){
		List<Peak> peaks = new ArrayList<Peak>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(s));
		String line;
		try {
			while((line = reader.readLine()) != null){
				try {
					peaks.add(parsePeak(line));
				} catch (ParsingException e) {
					//e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new PeakGroup(peaks);
	}
	
	
	
	
	
}
