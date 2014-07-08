package jprobe.framework.controller;

import java.util.Collection;

import javax.swing.table.TableModel;

public interface DataObject extends TableModel{
	
	public static int WRITABLE_CHANGED = 0;
	public static int NAME_CHANGED = 1;
	
	public boolean isWritable();
	public Collection<Format> formats();
	
	public String getName();
	public void setName();
	
	public void addChangeObserver(ChangeObserver obs);
	public void removeChangeObserver(ChangeObserver obs);
	
}
