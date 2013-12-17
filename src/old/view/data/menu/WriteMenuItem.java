package old.view.data.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;

import old.core.Core;
import old.core.exceptions.DataWriteException;
import old.datatypes.DataType;
import old.view.FormatFilter;
import old.view.data.DataContext;
import old.view.data.DataListener;

public class WriteMenuItem extends JMenuItem implements DataListener, ActionListener{
	private static final long serialVersionUID = 1L;
	
	private Core core;
	private JFileChooser fc;
	private DataContext context;
	private DataType curData;
	
	public WriteMenuItem(JFileChooser fc, Core core, DataContext context){
		super("Write");
		this.fc = fc;
		this.core = core;
		this.context = context;
		this.context.addDataListener(this);
		this.update(context);
		this.addActionListener(this);
	}

	@Override
	public void update(DataContext context) {
		this.curData = context.getCurrentData();
		this.setEnabled(core.isWritable(curData));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Map<String, String[]> fileFormats = core.getWritableFormats(curData);
		if(fileFormats == null){
			return;
		}
		fc.resetChoosableFileFilters();
		fc.setAcceptAllFileFilterUsed(false);
		for(String name : fileFormats.keySet()){
			FileFilter f = new FormatFilter(name, fileFormats.get(name));
			fc.addChoosableFileFilter(f);
		}
		int val = fc.showDialog(this, "Write");
		if(val == JFileChooser.APPROVE_OPTION){
			FormatFilter chosen = (FormatFilter) fc.getFileFilter();
			try {
				core.write(curData, fc.getSelectedFile(), chosen.format);
			} catch (DataWriteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	
	
}
