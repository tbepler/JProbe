package jprobe.save;

public class SaveException extends Exception{
	private static final long serialVersionUID = 1L;

	public SaveException() {
		super();
	}

	public SaveException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public SaveException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SaveException(String arg0) {
		super(arg0);
	}

	public SaveException(Throwable arg0) {
		super(arg0);
	}

}
