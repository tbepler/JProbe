package jprobe.framework.controller;

import java.util.Collection;

import javax.swing.table.TableModel;

public interface DataObject extends TableModel{
	
	public boolean isWritable();
	public Collection<Format> formats();
	
	public String getName();
	public void setName();
	
}
