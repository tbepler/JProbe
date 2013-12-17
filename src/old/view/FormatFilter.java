package old.view;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FormatFilter extends FileFilter{
	
	public final String format;
	private String[] extensions;
	
	public FormatFilter(String format, String[] extensions){
		this.format = format;
		this.extensions = extensions;
	}

	@Override
	public boolean accept(File f) {
		if(extensions == null || extensions.length < 1){
			return true;
		}
		for(String ex : extensions){
			if(f.getName().endsWith(ex)){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		if(extensions == null || extensions.length < 1){
			return format + " (.*)";
		}else{
			String d = format + " (";
			for(int i=0; i<extensions.length; i++){
				if(i == 0){
					d+=extensions[i];
				}else{
					d+=", "+extensions[i];
				}
			}
			d += ")";
			return d;
		}
	}
	
	
	
}
