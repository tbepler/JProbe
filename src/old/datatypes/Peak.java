package old.datatypes;

import java.io.BufferedWriter;
import java.util.Scanner;

import old.core.Constants;
import old.datatypes.location.*;
import old.datatypes.sequence.Sequence;

import org.w3c.dom.Element;
/**
 * This class is used to represent peak objects. It contains the chromosome, start and ending locations for the peak and can have a sequence associated with it.
 * 
 * @author Tristan Bepler
 * @see Location, Sequence, DataType
 */

public class Peak implements Location, Sequence, DataType{
	
	private Sequence seq = null;
	private Location loc;
	private int id;
	
	public Peak(Location loc) {
		this.loc = loc;
	}
	
	public Peak(Location loc, Sequence seq){
		this(loc);
		this.seq = seq;
	}
	
	public void setSequence(Sequence seq){
		this.seq = seq;
	}
	
	@Override
	public String getChr(){
		return loc.getChr();
	}
	
	@Override
	public int getStart(){
		return loc.getStart();
	}
	
	@Override
	public int getEnd(){
		return loc.getEnd();
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public Location parseLocation(String l) {
		return loc.parseLocation(l);
	}

	@Override
	public String locationToString() {
		return loc.locationToString();
	}

	@Override
	public String getSeq() {
		if(seq == null) return "";
		return seq.getSeq();
	}

	@Override
	public int length() {
		if(seq == null) return 0;
		return seq.length();
	}

	@Override
	public String getMutationFlag() {
		if(seq == null) return "";
		return seq.getMutationFlag();
	}

	@Override
	public String getOrientationFlag() {
		if(seq == null) return "";
		return seq.getOrientationFlag();
	}

	@Override
	public String seqToString() {
		if(seq == null) return "";
		return seq.seqToString();
	}

	@Override
	public String getName() {
		return "";
	}

}
