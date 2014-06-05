package plugins.functions.gui.dialog;

import java.awt.Frame;
import java.awt.Point;
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
import javax.swing.JPanel;

import util.gui.OnPress;
import util.gui.SwingUtils;

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
		
		@Override
		public void setVisible(boolean visible){
			super.setVisible(visible);
			if(visible){
				this.repaint();
			}
		}
	}
	
	private class IndexedDialogComparator implements Comparator<IndexedDialog>{

		@Override
		public int compare(IndexedDialog arg0, IndexedDialog arg1) {
			return arg0.getIndex() - arg1.getIndex();
		}
		
	}
	
	private Queue<IndexedDialog> m_AvailableDialogs;
	private Map<FunctionPanel, IndexedDialog> m_VisibleDialogs;
	private List<IndexedDialog> m_AllDialogs;
	private Map<JPanel, Point> m_LastLocation;
	private Frame m_Owner;
	private boolean m_Modal;
	
	public FunctionDialogHandler(Frame owner, boolean modal){
		this.m_Owner = owner;
		this.m_Modal = modal;
		m_AvailableDialogs = new PriorityQueue<IndexedDialog>(10, new IndexedDialogComparator());
		m_VisibleDialogs = new HashMap<FunctionPanel, IndexedDialog>();
		m_AllDialogs = new ArrayList<IndexedDialog>();
		m_LastLocation = new HashMap<JPanel, Point>();
	}
	
	private void setLastLocation(JPanel panel, Point loc){
		m_LastLocation.put(panel, loc);
	}
	
	private Point getLastLocation(JPanel panel){
		return m_LastLocation.get(panel);
	}
	
	private IndexedDialog getDialog(String title, JPanel content){
		IndexedDialog dialog;
		if(!m_AvailableDialogs.isEmpty()){
			dialog = m_AvailableDialogs.poll();
			dialog.setTitle(title);
		}else{
			dialog = new IndexedDialog(m_Owner, title, m_Modal, m_AllDialogs.size());
			m_AllDialogs.add(dialog);
		}
		dialog.setContentPane(content);
		dialog.pack();
		Point lastLoc = this.getLastLocation(content);
		if(lastLoc == null){
			SwingUtils.centerWindow(dialog, m_Owner);
		}else{
			dialog.setLocation(lastLoc);
		}
		return dialog;
	}
	
	public void display(final FunctionPanel panel){
		if(m_VisibleDialogs.containsKey(panel)){
			m_VisibleDialogs.get(panel).toFront();
			m_VisibleDialogs.get(panel).setVisible(true);
			return;
		}
		final IndexedDialog dialog = getDialog(panel.getTitle(), panel);
		dialog.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				hide(dialog, panel);
			}
		});
		OnPress close = new OnPress(){
			@Override
			public void act() {
				hide(dialog, panel);
			}
		};
		panel.setCancelAction(close);
		panel.setRunAction(close);
		dialog.getRootPane().setDefaultButton(panel.getRunButton());
		dialog.setVisible(true);
		m_VisibleDialogs.put(panel, dialog);
	}
	
	private void hide(IndexedDialog dialog, FunctionPanel panel){
		dialog.setVisible(false);
		m_AvailableDialogs.add(dialog);
		m_VisibleDialogs.remove(panel);
		this.setLastLocation(panel, dialog.getLocation());
	}
	
}
