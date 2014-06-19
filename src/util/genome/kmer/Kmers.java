package util.genome.kmer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import util.DNAUtils;
import util.genome.kmer.Kmer.Score;

public class Kmers {
	
	private static final int INIT_KMER_CAPACITY = 40000;
	private static final String KMER_REGEX = "^\\s*[AaCcGgTt\\.]+\\s+[AaCcGgTt\\.]+\\s+[\\-0123456789Ee\\.]+\\s+[\\-0123456789Ee\\.]+\\s+[\\-0123456789Ee\\.]+\\s*$";
	
	public static Kmer readKmer(InputStream in){
		final Map <String, Score> words = new HashMap<String, Score>(INIT_KMER_CAPACITY);
		List<String> wordList = new ArrayList<String>(INIT_KMER_CAPACITY);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		try {
			while((line = reader.readLine()) != null){
				if(line.matches(KMER_REGEX)){
					String[] tokens = line.trim().split("\\s+");
					Score s = new Score(Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]), Double.parseDouble(tokens[4]));
					words.put(tokens[0].intern(), s);
					words.put(tokens[1].intern(), s);
					wordList.add(tokens[0]);
				}
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sortWords(wordList, words);
		
		if(isUngapped(words)){
			return new UngappedKmer(words, wordList);
		}else{
			return new GappedKmer(words, wordList);
		}
		
	}
	
	public static Kmer combine(Collection<Kmer> kmers){
		Map<String, Score> wordScores = new HashMap<String, Score>(INIT_KMER_CAPACITY);
		Collection<String> words = new HashSet<String>(INIT_KMER_CAPACITY);
		for(Kmer k : kmers){
			for(String word : k){
				Score s = k.getScore(word);
				if(wordScores.containsKey(word)){
					Score cur = wordScores.get(word);
					if(s.ESCORE > cur.ESCORE){
						wordScores.put(word, s);
						wordScores.put(DNAUtils.reverseCompliment(word), s);
					}
				}else{
					wordScores.put(word, s);
					wordScores.put(DNAUtils.reverseCompliment(word), s);
					words.add(word);
				}
			}
		}
		List<String> wordsList = new ArrayList<String>(words);
		sortWords(wordsList, wordScores);
		
		if(containsGapped(kmers)){
			return new GappedKmer(wordScores, wordsList);
		}else{
			return new UngappedKmer(wordScores, wordsList);
		}
		
	}
	
	private static void sortWords(List<String> words, final Map<String, Score> scores){
		Collections.sort(words, new Comparator<String>(){

			@Override
			public int compare(String o1, String o2) {
				double dif = scores.get(o2).ESCORE - scores.get(o1).ESCORE;
				if(dif > 0) return 1;
				if(dif < 0) return -1;
				return o1.compareTo(o2);
			}
			
		});
	}
	
	private static boolean containsGapped(Collection<Kmer> kmers){
		for(Kmer k : kmers){
			if(k instanceof GappedKmer){
				return true;
			}
		}
		return false;
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
