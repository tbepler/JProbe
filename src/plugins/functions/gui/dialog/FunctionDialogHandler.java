package plugins.functions.gui.dialog;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.swing.JDialog;

import plugins.functions.gui.utils.OnPress;

public class FunctionDialogHandler{
	
	private class IndexedDialog extends JDialog{
		private static final long serialVersionUID = 1L;
		
		private int index;
		
		private IndexedDialog(Frame owner, String title, boolean modal, int index){
			super(owner, title, modal);
			this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			this.setResizable(false);
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
			dialog.setLocationRelativeTo(null);
			allDialogs.add(dialog);
			return dialog;
		}
	}
	
	public void display(final FunctionPanel panel){
		if(visibleDialogs.containsKey(panel)){
			visibleDialogs.get(panel).toFront();
			visibleDialogs.get(panel).setVisible(true);
			return;
		}
		final IndexedDialog dialog = getDialog(panel.getTitle());
		dialog.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				hide(dialog, panel);
			}
		});
		dialog.setContentPane(panel);
		OnPress close = new OnPress(){
			@Override
			public void act() {
				hide(dialog, panel);
			}
		};
		panel.setCancelAction(close);
		panel.setRunAction(close);
		dialog.pack();
		dialog.setVisible(true);
		visibleDialogs.put(panel, dialog);
	}
	
	private void hide(IndexedDialog dialog, FunctionPanel panel){
		dialog.setVisible(false);
		availableDialogs.add(dialog);
		visibleDialogs.remove(panel);
	}
	
}
