package plugins.functions.gui;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jprobe.services.function.Function;

public class FunctionDialogHandler{
	
	private class IndexedDialog extends JDialog{
		private static final long serialVersionUID = 1L;
		
		private int index;
		
		private IndexedDialog(Frame owner, String title, boolean modal, int index){
			super(owner, title, modal);
			this.index = index;
		}
		
		public int getIndex(){
			return index;
		}
		
	}
	
	private class IndexedDialogComparator implements Comparator<IndexedDialog>{

		@Override
		public int compare(IndexedDialog arg0, IndexedDialog arg1) {
			return arg0.getIndex() - arg1.getIndex();
		}
		
	}
	
	private Queue<IndexedDialog> availableDialogs;
	private Map<FunctionPanel, IndexedDialog> visibleDialogs;
	private List<IndexedDialog> allDialogs;
	private Frame owner;
	private boolean modal;
	
	public FunctionDialogHandler(Frame owner, boolean modal){
		this.owner = owner;
		this.modal = modal;
		availableDialogs = new PriorityQueue<IndexedDialog>(10, new IndexedDialogComparator());
		visibleDialogs = new HashMap<FunctionPanel, IndexedDialog>();
		allDialogs = new ArrayList<IndexedDialog>();
	}
	
	private IndexedDialog getDialog(String title){
		if(!availableDialogs.isEmpty()){
			IndexedDialog dialog = availableDialogs.poll();
			dialog.setTitle(title);
			return dialog;
		}else{
			IndexedDialog dialog = new IndexedDialog(owner, title, modal, allDialogs.size());
			allDialogs.add(dialog);
			return dialog;
		}
	}
	
	public void display(final FunctionPanel panel){
		if(visibleDialogs.containsKey(panel)){
			visibleDialogs.get(panel).toFront();
			return;
		}
		final IndexedDialog dialog = getDialog(panel.getTitle());
		dialog.setContentPane(panel);
		OnPress close = new OnPress(){
			@Override
			public void act() {
				dialog.setVisible(false);
				availableDialogs.add(dialog);
				visibleDialogs.remove(panel);
			}
		};
		panel.setCancelAction(close);
		panel.setRunAction(close);
		dialog.pack();
		dialog.setVisible(true);
		visibleDialogs.put(panel, dialog);
	}
	
}
