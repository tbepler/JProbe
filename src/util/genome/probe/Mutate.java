package util.genome.probe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.genome.GenomicCoordinate;
import util.genome.GenomicRegion;
import util.genome.kmer.Kmer;

public class Mutate {
	
	public static Probe mutate(Probe mut, Kmer kmer, double escoreCutoff, int bindingSiteBarrier){
		List<GenomicRegion> bindingSites = getBindingSites(mut);
		List<GenomicRegion> protectedRegions = getProtectedRegions(bindingSites, bindingSiteBarrier);
		Set<GenomicCoordinate> protectedCoords = toCoordinateSet(protectedRegions);
	}
	
	private static List<GenomicRegion> getBindingSites(Probe p){
		List<GenomicRegion> bindingSites = new ArrayList<GenomicRegion>();
		for(GenomicRegion bindingSite : p.getBindingSites()){
			bindingSites.add(bindingSite);
		}
		return bindingSites;
	}
	
	private static List<GenomicRegion> getProtectedRegions(List<GenomicRegion> bindingSites, int bindingSiteBarrier){
		List<GenomicRegion> prot = new ArrayList<GenomicRegion>();
		for(GenomicRegion bindingSite : bindingSites){
			GenomicRegion protRegion = new GenomicRegion(
					bindingSite.getStart().decrement(bindingSiteBarrier),
					bindingSite.getEnd().increment(bindingSiteBarrier)
					);
			prot.add(protRegion);
		}
		return prot;
	}
	
	private static Set<GenomicCoordinate> toCoordinateSet(Collection<GenomicRegion> regions){
		Set<GenomicCoordinate> coords = new HashSet<GenomicCoordinate>();
		for(GenomicRegion r : regions){
			for(GenomicCoordinate c : r){
				coords.add(c);
			}
		}
		return coords;
	}
	
	private static Set<GenomicCoordinate> toCoordinateSet(GenomicRegion region){
		Set<GenomicCoordinate> coords = new HashSet<GenomicCoordinate>();
		for(GenomicCoordinate coord : region){
			coords.add(coord);
		}
		return coords;
	}
	
}
