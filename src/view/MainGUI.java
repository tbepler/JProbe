package view;

import java.util.*;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import controller.CoreController;
import core.Core;
import datatypes.DataType;

public class MainGUI extends JFrame implements CoreController{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Core core;
	
	private JTextArea testDisplay;
	
	public MainGUI(){
		super("ArrayGenerator");
		initComponents();
		try{
			core = new Core();
		} catch (Exception e){
			System.err.println("Core initialization error.");
			System.err.println(e.getMessage());
			System.exit(0);
		}
		core.addObserver(this);
		String out = "";
		Collection<String> modules = core.getModuleNames();
		for(String module : modules){
			out += module+"\n";
		}
		testDisplay.setText(out);
	}
	

	@Override
	public DataType[] selectArgs(Class<? extends DataType>[] types) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(int event) {
		// TODO Auto-generated method stub
		
	}
	
	private void initComponents(){
		
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		testDisplay = new JTextArea(10, 40);
		testDisplay.setEditable(false);
		testDisplay.setText("test");
		
		GroupLayout layout = new GroupLayout(getContentPane());
		this.getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup().addComponent(testDisplay, Alignment.CENTER)
		);
		layout.setVerticalGroup(
			layout.createParallelGroup().addComponent(testDisplay, Alignment.CENTER)	
		);
		this.pack();
		this.setVisible(true);
	}
	
	
	
	public static void main(String[] args){
		new MainGUI();
	}


	
}
