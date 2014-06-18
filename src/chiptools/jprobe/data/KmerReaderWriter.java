package chiptools.jprobe.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import util.DNAUtils;
import util.genome.kmer.Kmer.Score;
import jprobe.services.data.Data;
import jprobe.services.data.DataReader;
import jprobe.services.data.DataWriter;

public class KmerReaderWriter implements DataReader, DataWriter{

	@Override
	public FileNameExtensionFilter[] getValidWriteFormats() {
		return new FileNameExtensionFilter[]{
			new FileNameExtensionFilter("Kmer file", "txt", "*")	
		};
	}

	@Override
	public void write(Data data, FileNameExtensionFilter format, BufferedWriter out) throws Exception {
		util.genome.kmer.Kmer kmer = ((Kmer) data).getKmer();
		Queue<String> wordsSorted = new PriorityQueue<String>();
		for(String s : kmer){
			wordsSorted.add(s);
		}
		Set<String> written = new HashSet<String>();
		String s;
		while(!wordsSorted.isEmpty()){
			s = wordsSorted.poll();
			if(!written.contains(s)){
				String rvscomp = DNAUtils.reverseCompliment(s);
				Score score = kmer.getScore(s);
				out.write(s + "\t" + rvscomp + "\t" + score + "\n");
				written.add(s);
				written.add(rvscomp);
			}
		}
	}

	@Override
	public FileFilter[] getValidReadFormats() {
		return new FileFilter[]{
			new FileFilter(){

				@Override
				public boolean accept(File arg0) {
					return true;
				}

				@Override
				public String getDescription() {
					return "Kmer file";
				}
				
			}
		};
	}

	@Override
	public Data read(FileFilter format, InputStream in) throws Exception {
		util.genome.kmer.Kmer kmer = util.genome.kmer.Kmers.readKmer(in);
		return new Kmer(kmer);
	}

	@Override
	public Class<? extends Data> getWriteClass() {
		return Kmer.class;
	}

	@Override
	public Class<? extends Data> getReadClass() {
		return Kmer.class;
	}

}
