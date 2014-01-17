package plugins.functions.gui.dialog;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class AbstractArgsPanel<T> extends JPanel{
	private static final long serialVersionUID = 1L;

	private List<T> m_Args;
	private List<String> m_Headers;
	
	public AbstractArgsPanel(){
		super(new GridBagLayout());
		m_Args = new ArrayList<T>();
		m_Headers = new ArrayList<String>();
	}
	
	public AbstractArgsPanel(Collection<T> args){
		this();
		m_Args.addAll(args);
		this.layoutGrid();
	}
	
	public AbstractArgsPanel(List<String> headers){
		this();
		m_Headers.addAll(headers);
		this.layoutGrid();
	}
	
	public AbstractArgsPanel(Collection<T> args, List<String> headers){
		this();
		m_Headers.addAll(headers);
		m_Args.addAll(args);
		this.layoutGrid();
	}
	
	public void setHeaders(List<String> headers){
		m_Headers.clear();
		m_Headers.addAll(headers);
		this.layoutGrid();
	}
	
	public void addArg(T arg){
		m_Args.add(arg);
		this.layoutGrid();
	}
	
	public void removeArg(T arg){
		m_Args.remove(arg);
		this.layoutGrid();
	}
	
	public void setArgs(Collection<T> args){
		m_Args.clear();
		m_Args.addAll(args);
		this.layoutGrid();
	}
	
	public void clearArgs(){
		m_Args.clear();
		this.layoutGrid();
	}
	
	protected abstract List<Component> generateRowComponents(T argument);
	
	private void layoutGrid(){
		this.removeAll();
		GridBagConstraints gbc = new GridBagConstraints();
		for(int j=0; j<m_Headers.size(); j++){
			gbc.gridx = j+1;
			this.add(new JLabel(m_Headers.get(j)), gbc);
		}
		for(int i=0; i<m_Args.size(); i++){
			gbc = new GridBagConstraints();
			gbc.gridy = i+2;
			List<Component> rowComponents = this.generateRowComponents(m_Args.get(i));
			for(int j=0; j<rowComponents.size(); j++){
				gbc.gridx = j+1;
				this.add(rowComponents.get(j), gbc);
			}
		}
		this.revalidate();
	}
	
}
