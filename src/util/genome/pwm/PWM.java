package util.genome.pwm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.*;

public class PWM implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private static final int NUM_BASES = 4;
	private static final int A = 0;
	private static final int C = 1;
	private static final int G = 2;
	private static final int T = 3;
	
	private static final String A_LINE = "A:";
	private static final String C_LINE = "C:";
	private static final String G_LINE = "G:";
	private static final String T_LINE = "T:";
	
	private int getBaseIndex(char base){
		if(base == 'a' || base == 'A'){
			return A;
		}
		if(base == 'c' || base == 'C'){
			return C;
		}
		if(base == 'g' || base == 'G'){
			return G;
		}
		if(base == 't' || base == 'T'){
			return T;
		}
		throw new RuntimeException("No encoding for base: "+base);
	}
	
	
	public static final PWM readPWM(InputStream in){
		List<Double> a = new ArrayList<Double>();
		List<Double> c = new ArrayList<Double>();
		List<Double> g = new ArrayList<Double>();
		List<Double> t = new ArrayList<Double>();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		try {
			while((line = reader.readLine()) != null){
				if(line.startsWith(A_LINE)){
					a = parse(line);
				}
				if(line.startsWith(C_LINE)){
					c = parse(line);
				}
				if(line.startsWith(G_LINE)){
					g = parse(line);
				}
				if(line.startsWith(T_LINE)){
					t = parse(line);
				}
				//check if all bases have been read
				if(!a.isEmpty() && !c.isEmpty() && !g.isEmpty() && !t.isEmpty()){
					break;
				}
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new PWM(toArray(a), toArray(c), toArray(g), toArray(t));
	}
	
	private static final double[] toArray(Collection<Double> col){
		double[] array = new double[col.size()];
		int i = 0;
		for(double d : col){
			array[i] = d;
			i++;
		}
		return array;
	}
	
	private static final List<Double> parse(String line){
		String[] tokens = line.split("\\s");
		List<Double> scores = new ArrayList<Double>();
		for(int i=1; i<tokens.length; i++){
			scores.add(Double.parseDouble(tokens[i]));
		}
		return scores;
	}
	
	private final double[][] m_Scores;
	
	public PWM(double[] aScores, double[] cScores, double[] gScores, double[] tScores){
		if(aScores.length != cScores.length || aScores.length != gScores.length || aScores.length != tScores.length){
			throw new RuntimeException("Cannot create a PWM from score arrays of different lengths: A "+aScores.length+
					", C "+cScores.length+", G "+gScores.length+", T "+tScores.length+".");
		}
		m_Scores = new double[NUM_BASES][aScores.length];
		for(int i=0; i<aScores.length; i++){
			m_Scores[A][i] = aScores[i];
			m_Scores[C][i] = cScores[i];
			m_Scores[G][i] = gScores[i];
			m_Scores[T][i] = tScores[i];
		}
	}
	
	/**
	 * Returns the score of the given word according to this PWM. The word score is the sum of the (char,position) scores.
	 * @param word - sequence to be scored by this PWM
	 * @return the summed position scores
	 */
	public double score(String word){
		if(word.length() == this.length()){
			double score = 0;
			for(int i=0; i<word.length(); i++){
				char base = word.charAt(i);
				score += m_Scores[getBaseIndex(base)][i];
			}
			return score;
		}
		throw new RuntimeException("Cannot score word: "+word+". Word length is not the same as PWM length: "+this.length()+".");
	}
	
	/**
	 * Scores a sequence according to this PWM. Returns an array containing the scores of each word of contained
	 * by the given sequence.
	 * @param seq - the sequence to be scored
	 * @return an array of size (seq.length() - PWM.length() + 1) containing the scores of each word of size PWM.length() contained by the sequence 
	 */
	public double[] scoreSeq(String seq){
		if(seq.length() < this.length()){
			throw new RuntimeException("Cannot score sequence: "+seq+". Sequence length is less than this PWMs length: "+this.length()+".");
		}
		double[] scores = new double[seq.length() - this.length() + 1];
		for(int i=0; i<scores.length; i++){
			String word = seq.substring(i, i+this.length());
			scores[i] = this.score(word);
		}
		return scores;
	}
	
	public int length(){
		return m_Scores[A].length;
	}
	
}