package util.genome.kmer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

import util.genome.kmer.Kmer.Score;

public class Kmers {
	
	private static final int INIT_KMER_CAPACITY = 40000;
	private static final String KMER_REGEX = "^\\s*[AaCcGgTt\\.]+\\s+[AaCcGgTt\\.]+\\s+[\\-0123456789Ee\\.]+\\s+[\\-0123456789Ee\\.]+\\s+[\\-0123456789Ee\\.]+\\s*$";
	
	public static Kmer readKmer(InputStream in){
		Map <String, Score> words = new LinkedHashMap<String, Score>(INIT_KMER_CAPACITY);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		try {
			while((line = reader.readLine()) != null){
				if(line.matches(KMER_REGEX)){
					String[] tokens = line.trim().split("\\s+");
					Score s = new Score(Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]), Double.parseDouble(tokens[4]));
					words.put(tokens[0].intern(), s);
					words.put(tokens[1].intern(), s);
				}
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(isUngapped(words)){
			return new UngappedKmer(words);
		}else{
			return new GappedKmer(words);
		}
		
	}
	
	
	private static String UNGAPPED_REGEX = "^[AaTtGgCc]$";
	
	public static boolean isUngapped(Map<String, Score> motifs){
		for(String motif : motifs.keySet()){
			if(!motif.matches(UNGAPPED_REGEX)){
				return false;
			}
		}
		return true;
	}
	
}
