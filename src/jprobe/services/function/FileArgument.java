package jprobe.services.function;

import java.io.File;

import javax.swing.JFileChooser;
import jprobe.services.Workspace;
import jprobe.services.function.components.FileArgComponent;
import util.progress.ProgressListener;

public abstract class FileArgument<P> extends AbstractArgument<P, FileArgComponent>{
	
	public static final String DEFAULT_BUTTON_TEXT = "Browse";
	public static final String PROTOTYPE_TEXT = "File name here";
	public static final String PROTOTYPE_VAL = "FILE";


	protected FileArgument(String name, String tooltip, String category, Character shortFlag, boolean optional) {
		super(name, tooltip, category, shortFlag, PROTOTYPE_VAL, optional);
	}
	
	protected FileArgument(String name, String tooltip, String category, Character shortFlag, String prototypeVal, boolean optional) {
		super(name, tooltip, category, shortFlag, prototypeVal, optional);
	}
	
	protected abstract JFileChooser getJFileChooser();
	protected abstract boolean isValid(File f);
	protected abstract void process(P params, File f);
	
	protected String getButtonText(){
		return DEFAULT_BUTTON_TEXT;
	}
	
	protected String getLablePrototypeText(){
		return PROTOTYPE_TEXT;
	}
	
	@Override
	public void parse(ProgressListener l, P params, String[] args){
		if(args.length < 1 || args.length > 1){
			throw new RuntimeException(this.getName()+" requires 1 argument. Received "+args.length);
		}
		File f = new File(args[0]);
		if(!this.isValid(f)){
			throw new RuntimeException("File \""+args[0]+"\" is not valid");
		}
		this.process(params, f);
	}

	@Override
	public boolean isValid(FileArgComponent component) {
		return this.isValid(component.getFile());
	}

	@Override
	public FileArgComponent createComponent(Workspace w) {
		return new FileArgComponent(this.getJFileChooser(), this.getButtonText(), this.getLablePrototypeText());
	}

	@Override
	public void process(P params, FileArgComponent component) {
		this.process(params, component.getFile());
	}



}
