package util.genome;

import java.util.*;

import util.genome.kmer.Kmer;
import util.genome.pwm.PWM;

public class Sequences {
	
	public static class Profile{
		
		private final List<String> m_Names = new ArrayList<String>();
		private final List<double[]> m_Entries = new ArrayList<double[]>();
		
		public int size(){
			return m_Names.size();
		}
		
		public String getEntryName(int entry){
			return m_Names.get(entry);
		}
		
		public double[] getEntry(int entry){
			return m_Entries.get(entry);
		}
		
		private void put(String name, double[] entry){
			m_Names.add(name);
			m_Entries.add(entry);
		}
		
		@Override
		public String toString(){
			String s = "";
			for(int i=0; i<this.size(); i++){
				s += this.getEntryName(i);
				for(double d : this.getEntry(i)){
					s += "\t" + d;
				}
				s += "\n";
			}
			return s;
		}
		
	}
	
	
	public static Profile profile(String seq, Kmer[] kmers, String[] kmerNames, PWM[] pwms, String[] pwmNames){
		Profile p = new Profile();
		//for each kmer, score the sequence with each word size contained by the kmer
		for(int i=0; i<kmers.length; i++){
			Kmer kmer = kmers[i];
			if(kmer == null) continue;
			String name = i < kmerNames.length ? kmerNames[i] : "Kmer"+(i+1);
			//score the sequence with words of each length contained by the kmer
			for(int wordLen : kmer.getWordLengths()){
				if(seq.length() < wordLen) continue;
				double[] scores = new double[seq.length() - wordLen + 1];
				for(int start = 0; start<scores.length; start++){
					int end = start + wordLen;
					String subseq = seq.substring(start, end);
					scores[start] = kmer.escore(subseq);
				}
				p.put(name + "\t"+wordLen, scores);
			}
		}
		//for each pwm, score the sequence by each word of the size of the pwm
		for(int i=0; i<pwms.length; i++){
			PWM pwm = pwms[i];
			if(pwm == null) continue;
			String name = i < pwmNames.length ? pwmNames[i] : "PWM"+(i+1);
			if(seq.length() < pwm.length()) continue;
			double[] scores = new double[seq.length() - pwm.length() + 1];
			for(int start = 0; start<scores.length; start++){
				int end = start + pwm.length();
				String subseq = seq.substring(start, end);
				scores[start] = pwm.score(subseq);
			}
			p.put(name+"\t"+pwm.length(), scores);
		}
		return p;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
