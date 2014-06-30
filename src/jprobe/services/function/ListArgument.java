package jprobe.services.function;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import util.gui.ChangeNotifier;
import util.progress.ProgressListener;

public abstract class ListArgument<P, E, C extends JComponent & ChangeNotifier> extends AbstractArgument<P,C> {
	
	protected ListArgument(String name, String tooltip, String category,
			Character shortFlag, String prototypeValue, boolean optional) {
		super(name, tooltip, category, shortFlag, prototypeValue, optional);
	}
	
	/**
	 * This method should extract the user's input from this Argument's component.
	 * @param comp - this Argument's component
	 * @return - a list of entry objects entered by the user
	 */
	protected abstract List<E> getUserInput(C comp);
	
	/**
	 * This method should take the user's entries and modify the given parameters
	 * object accordingly.
	 * @param params - parameter object to modify
	 * @param entries - user entries parsed either from this Argument's JComponent
	 * or from the command line
	 */
	protected abstract void process(P params, List<E> entries);
	
	/**
	 * This method takes a command line argument and parses it into an appropriate
	 * entry object. This method is called by the {@link #parse(ProgressListener, String[])} method
	 * in order to construct a list of user entries to be passed to the {@link #process(P, List)}
	 * method.
	 * @param arg - command line string passed by the user to this argument
	 * @return entry object that is the result of parsing this string
	 */
	protected abstract E parse(ProgressListener l, String arg);
	
	/**
	 * Parses the strings passed by the user into a list of entries for processing
	 * by the {@link #process(P, List)} method.
	 * @param args
	 * @return
	 */
	protected List<E> parse(ProgressListener l, String[] args){
		List<E> list = new ArrayList<E>();
		for(String s : args){
			list.add(this.parse(l, s));
		}
		return list;
	}

	@Override
	public void process(P params, C comp) {
		this.process(params, this.getUserInput(comp));
	}

	@Override
	public void parse(ProgressListener l, P params, String[] args) {
		List<E> entries = this.parse(l, args);
		this.process(params, entries);
	}


}
