package plugins.functions.gui.dialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.osgi.framework.Bundle;

import plugins.functions.gui.SwingFunctionExecutor;
import util.Observer;
import util.Subject;
import jprobe.services.JProbeCore;
import jprobe.services.function.Argument;
import jprobe.services.function.Function;

public class ArgumentsPanel<T> extends JPanel implements Subject<Boolean>, Observer<Boolean>{
	private static final long serialVersionUID = 1L;
	
	private static <T> Map<String, Collection<Argument<? super T>>> groupByCategory(Collection<Argument<? super T>> args){
		Map<String, Collection<Argument<? super T>>> map = new TreeMap<String, Collection<Argument<? super T>>>();
		for(Argument<? super T> arg : args){
			String cat = arg.getCategory();
			if(map.containsKey(cat)){
				map.get(cat).add(arg);
			}else{
				Collection<Argument<? super T>> group = new ArrayList<Argument<? super T>>();
				group.add(arg);
				map.put(cat, group);
			}
		}
		return map;
	}
	
	private final Collection<Observer<Boolean>> m_Obs = new HashSet<Observer<Boolean>>();
	
	private final Function<T> m_Function;
	private final Collection<ArgumentPanel<T>> m_ArgPanels = new ArrayList<ArgumentPanel<T>>();
	
	private boolean m_Valid;
	
	public ArgumentsPanel(Function<T> function){
		super();
		m_Function = function;
		m_Valid = false;
		Map<String, Collection<Argument<? super T>>> categoryGrouping = groupByCategory(function.getArguments());
		for(String category : categoryGrouping.keySet()){
			Collection<Argument<? super T>> args = categoryGrouping.get(category);
			JPanel panel = this.generatePanel(category, args);
			for(Argument<? super T> arg : args){
				ArgumentPanel<T> argPanel = this.generateArgPanel(arg);
				m_ArgPanels.add(argPanel);
				argPanel.register(this);
				panel.add(argPanel);
			}
			this.add(panel);
		}
		this.updateValidity();
	}
	
	public void run(JProbeCore core, Bundle bundle){
		T params = m_Function.newParameters();
		for(ArgumentPanel<T> argPanel : m_ArgPanels){
			argPanel.process(params);
		}
		SwingFunctionExecutor<T> executor = new SwingFunctionExecutor<T>(m_Function, params, core.getDataManager(), bundle);
		executor.execute();
	}
	
	protected JPanel generatePanel(String category, Collection<Argument<? super T>> args){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		Border border = BorderFactory.createTitledBorder(category);
		panel.setBorder(border);
		return panel;
	}
	
	protected ArgumentPanel<T> generateArgPanel(Argument<? super T> arg){
		return new ArgumentPanel<T>(arg);
	}
	
	public boolean areArgsValid(){
		return m_Valid;
	}
	
	private void updateValidity(){
		boolean valid = true;
		for(ArgumentPanel<T> argPanel : m_ArgPanels){
			valid = valid && argPanel.isArgValid();
		}
		if(m_Valid != valid){
			m_Valid = valid;
			this.notifyObservers(this.areArgsValid());
		}
	}

	@Override
	public void update(Subject<Boolean> observed, Boolean notification) {
		this.updateValidity();
	}
	
	protected void notifyObservers(boolean bool){
		for(Observer<Boolean> obs : m_Obs){
			obs.update(this, bool);
		}
	}

	@Override
	public void register(Observer<Boolean> obs) {
		m_Obs.add(obs);
	}

	@Override
	public void unregister(Observer<Boolean> obs) {
		m_Obs.remove(obs);
	}
	
}




