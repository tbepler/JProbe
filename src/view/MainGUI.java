package view;

import java.io.IOException;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import core.Core;

public class MainGUI extends JFrame implements Observer{
	
	private Core core;
	
	private JTextArea testDisplay;
	
	public MainGUI(){
		initComponents();
		try{
			core = new Core("src/modules.xml");
		} catch (Exception e){
			System.err.println("Core initialization error.");
			System.err.println(e.getMessage());
			System.exit(0);
		}
		core.addObserver(this);
	}
	

	@Override
	public void update(Observable arg0, Object arg1) {
		String out = "";
		Map<String, String> modules = core.getModuleMap();
		for(String module : modules.keySet()){
			out += module+":\n"+modules.get(module)+"\n";
		}
		testDisplay.setText(out);
	}
	
	private void initComponents(){
		testDisplay = new JTextArea();
		
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		add(new JLabel("ArrayGenerator"));
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup().addComponent(testDisplay, Alignment.CENTER)
		);
		layout.setVerticalGroup(
			layout.createParallelGroup().addComponent(testDisplay, Alignment.CENTER)	
		);
		pack();
		setVisible(true);
	}
	
	
	
	public static void main(String[] args){
		new MainGUI();
	}

	
}
