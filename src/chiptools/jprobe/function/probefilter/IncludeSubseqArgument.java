package chiptools.jprobe.function.probefilter;

import java.io.File;

import javax.swing.JComponent;

import chiptools.jprobe.function.ChiptoolsFileArg;

public class IncludeSubseqArgument extends ChiptoolsFileArg<ProbeFilterParam>{

	protected IncludeSubseqArgument(boolean optional) {
		super(IncludeSubseqArgument.class, optional);
	}
	
	@Override
	protected void setFile(File f){
		
		super.setFile(f);
	}
	
	@Override
	public JComponent getComponent(){
		JComponent comp = super.getComponent();
		
		return comp;
	}
	
	@Override
	protected boolean isValid(File f) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void process(ProbeFilterParam params, File f) {
		// TODO Auto-generated method stub
		
	}

}
