package old.datatypes.location;

import org.w3c.dom.Element;


public class GenomeLocation implements Location{
	
	private String chr;
	private int start;
	private int end;
	
	public GenomeLocation(String chr, int start, int end){
		this.chr = chr;
		this.start = start;
		this.end = end;
	}
	
	@Override
	public String getChr() {
		return chr;
	}

	@Override
	public int getStart() {
		return start;
	}

	@Override
	public int getEnd() {
		return end;
	}

	@Override
	public Location parseLocation(String l) {
		return new GenomeLocation(l.substring(0, l.indexOf(':')),
								  Integer.parseInt(l.substring(l.indexOf(':')+1),l.indexOf('-')),
								  Integer.parseInt(l.substring(l.indexOf('-')+1)));
	}

	@Override
	public String locationToString() {
		return chr + ":" + start + "-" + end;
	}
	
	public static GenomeLocation readXML(Element e){
		return new GenomeLocation(
				e.getElementsByTagName("chromosome").item(0).getTextContent().trim(),
				Integer.parseInt(e.getElementsByTagName("start").item(0).getTextContent().trim()),
				Integer.parseInt(e.getElementsByTagName("end").item(0).getTextContent().trim())
				);
	}

}
