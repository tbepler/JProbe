
package old.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.*;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import old.controller.CoreController;
import old.core.Core;
import old.datatypes.DataType;
import old.view.data.DataContext;
import old.view.data.DataListener;
import old.view.data.display.TabbedDataViewer;
import old.view.data.menu.DataMenu;

public class MainGUI extends JFrame implements CoreController{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Core core;
	private JMenuBar menuBar;
	private List<DataContext> contexts;
	private TabbedDataViewer tabs;
	
	public MainGUI(){
		super("ArrayGenerator");
		contexts = new ArrayList<DataContext>();
		try{
			core = new Core(this, "Extensions", "Extensions/modules.xml", "Extensions/datatypes.xml");
		} catch (Exception e){
			System.err.println("Core initialization error.");
			System.err.println(e.getMessage());
			System.exit(0);
		}
		initComponents();
	}
	

	@Override
	public DataType[] selectArgs(Class<? extends DataType>[] requiredArgs, Class<? extends DataType>[] optionalArgs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(int event) {
		
	}
	
	private void initComponents(){
		
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		JPanel content = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		this.setContentPane(content);
		
		tabs = new TabbedDataViewer(core);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.7;
		c.weighty = 0.7;
		content.add(tabs, c);
		
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		menuBar.add(new ModuleMenu(core));
		menuBar.add(new DataMenu(core, tabs));
		
		
		this.pack();
		this.setVisible(true);
	}
	
	
	
	public static void main(String[] args){
		new MainGUI();
	}


	
}
