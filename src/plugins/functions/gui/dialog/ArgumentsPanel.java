package plugins.functions.gui.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.osgi.framework.Bundle;

import plugins.functions.gui.Constants;
import plugins.functions.gui.SwingFunctionExecutor;
import util.Observer;
import util.Subject;
import jprobe.services.JProbeCore;
import jprobe.services.function.Argument;
import jprobe.services.function.Function;

public class ArgumentsPanel<T> extends JPanel implements Subject<Boolean>, Observer<Boolean>{
	private static final long serialVersionUID = 1L;
	
	private static <T> Map<String, Collection<Argument<? super T>>> groupByCategory(Collection<Argument<? super T>> args){
		Map<String, Collection<Argument<? super T>>> map = new LinkedHashMap<String, Collection<Argument<? super T>>>();
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
	private final List<Component> m_CategoryPanels = new ArrayList<Component>();
	
	private boolean m_Valid;
	
	private boolean m_LayedOut = false;
	
	public ArgumentsPanel(Function<T> function){
		super(new GridBagLayout());
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
				panel.add(argPanel,this.argPanelConstraints());
			}
			m_CategoryPanels.add(panel);
		}
		this.updateValidity();
	}
	
	@Override
	public Dimension getPreferredSize(){
		if(!m_LayedOut){
			this.layoutColumns(m_CategoryPanels, Constants.ARGS_PANEL_TARGET_ASPECT);
			m_LayedOut = true;
		}
		return super.getPreferredSize();
	}
	
	@Override
	public Dimension getMaximumSize(){
		if(!m_LayedOut){
			this.layoutColumns(m_CategoryPanels, Constants.ARGS_PANEL_TARGET_ASPECT);
			m_LayedOut = true;
		}
		return super.getMaximumSize();
	}
	
	@Override
	public Dimension getMinimumSize(){
		if(!m_LayedOut){
			this.layoutColumns(m_CategoryPanels, Constants.ARGS_PANEL_TARGET_ASPECT);
			m_LayedOut = true;
		}
		return super.getMinimumSize();
	}
	
	@Override
	public void doLayout(){
		if(!m_LayedOut){
			this.layoutColumns(m_CategoryPanels, Constants.ARGS_PANEL_TARGET_ASPECT);
			m_LayedOut = true;
		}
		super.doLayout();
	}
	
	@Override
	public void validate(){
		if(!m_LayedOut){
			this.layoutColumns(m_CategoryPanels, Constants.ARGS_PANEL_TARGET_ASPECT);
			m_LayedOut = true;
		}
		super.validate();
	}
	
	protected GridBagConstraints categoryPanelConstraints(int row){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = row;
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		return gbc;
	}
	
	protected void layoutColumns(List<Component> panels, double targetAspect){
		List<Column> layout = getBestLayout(panels, targetAspect);
		if(layout == null){
			return;
		}
		for(int i=0; i<layout.size(); i++){
			JPanel panel = new JPanel(new GridBagLayout());
			List<Component> comps = layout.get(i).getComponents();
			for(int j=0; j<comps.size(); j++){
				GridBagConstraints gbc = categoryPanelConstraints(j);
				panel.add(comps.get(j), gbc);
			}
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = i;
			gbc.gridy = 0;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			this.add(panel, gbc);
		}
	}
	
	private static List<Column> getBestLayout(List<Component> comps, double targetAspect){
		int height = getHeight(comps);
		double bestDist = Double.POSITIVE_INFINITY;
		List<Column> bestLayout = null;
		for(int cols=1; cols<=comps.size(); cols++){
			if(bestLayout == null){
				bestLayout = createColumns(comps, height, cols);
				bestDist = distance(bestLayout, targetAspect);
			}else{
				List<Column> layout = createColumns(comps, height, cols);
				double dist = distance(layout, targetAspect);
				if(dist > bestDist){
					return bestLayout;
				}
				bestDist = dist;
				bestLayout = layout;
			}
		}
		return bestLayout;
	}
	
	private static double distance(List<Column> layout, double targetAspect){
		double aspect = getAspect(layout);
		return Math.abs(targetAspect - aspect);
	}
	
	private static double getAspect(List<Column> layout){
		double height = getLayoutHeight(layout);
		double width = getLayoutWidth(layout);
		return width/height;
	}
	
	private static int getLayoutWidth(List<Column> layout){
		int width = 0;
		for(Column c : layout){
			width += c.getWidth();
		}
		return width;
	}
	
	private static int getLayoutHeight(List<Column> layout){
		int height = Integer.MIN_VALUE;
		for(Column c : layout){
			if(c.getHeight() > height){
				height = c.getHeight();
			}
		}
		return height;
	}
	
	protected static class Column{
		private final List<Component> m_Comps = new ArrayList<Component>();
		private int m_Height = 0;
		private int m_Width = 0;
		
		public Column(){
			//default constructor
		}
		
		//copy constructor
		private Column(Column other){
			m_Comps.addAll(other.m_Comps);
			m_Height = other.m_Height;
			m_Width = other.m_Width;
		}
		
		public void addComponent(Component c){
			m_Comps.add(c);
			Dimension pref = c.getPreferredSize();
			m_Height += pref.height;
			m_Width = Math.max(m_Width, pref.width);
		}
		
		public List<Component> getComponents(){
			return m_Comps;
		}
		
		public boolean isEmpty() { return m_Comps.isEmpty(); }
		
		public int getHeight() { return m_Height; }
		public int getWidth() { return m_Width; }
		
		public Column copy(){
			return new Column(this);
		}
	}
	
	protected static List<Column> createColumns(List<Component> panels, int height, int num){
		if(num < 1){
			return new ArrayList<Column>();
		}
		double colHeight = ((double) height)/((double) num);
		List<List<Column>> layouts = new ArrayList<List<Column>>();
		layouts.add(new ArrayList<Column>()); //init first layout
		//generate the possible layouts with the given number of columns and column height
		for(Component c : panels){
			int compHeight = c.getPreferredSize().height;
			List<List<Column>> newLayouts = new ArrayList<List<Column>>();
			//add the component to each layout
			for(List<Column> layout : layouts){
				if(layout.isEmpty()){ //init the first column in the layout
					layout.add(new Column());
				}
				Column cur = getCurColumn(layout, num, colHeight);
				if(cur.getHeight() + compHeight <= colHeight || cur.isEmpty() || layout.size() >= num){
					cur.addComponent(c);
				}else{ //component will cause column to exceed max height and new column can be made,
					// so try adding to this column and next column
					splitLayout(layout, cur, c, newLayouts);
				}
			}
			//add the new layouts to the current layouts
			layouts.addAll(newLayouts);
		}
		
		//now pick the most balanced layout and return it
		return pickBestLayout(layouts);
		
	}
	
	protected static List<Column> pickBestLayout(List<List<Column>> layouts){
		double best = Double.POSITIVE_INFINITY;
		List<Column> bestLayout = null;
		for(List<Column> layout : layouts){
			double balance = computeBalance(layout);
			if(balance < best){
				best = balance;
				bestLayout = layout;
			}
		}
		
		return bestLayout;
	}
	
	protected static double computeBalance(List<Column> layout){
		double mean = meanHeight(layout);
		double balance = 0;
		for(Column col : layout){
			balance += Math.abs(col.getHeight() - mean);
		}
		return balance;
	}
	
	private static double meanHeight(List<Column> layout){
		double sum = 0;
		for(Column col : layout){
			sum += col.getHeight();
		}
		return sum / ((double) layout.size());
	}
	
	protected static void splitLayout(List<Column> layout, Column cur, Component c, List<List<Column>> newLayouts){
		//copy the layout and add the component to the cur column of the copy
		List<Column> copy = new ArrayList<Column>();
		for(Column col : layout){
			if(col == cur){
				Column curCopy = cur.copy();
				curCopy.addComponent(c);
				copy.add(curCopy);
			}else{
				copy.add(col);
			}
		}
		newLayouts.add(copy);
		//now create a new column in the layout and add the component to that column
		Column newCol = new Column();
		layout.add(newCol);
		newCol.addComponent(c);
	}
	
	protected static Column getCurColumn(List<Column> layout, int maxCols, double maxColHeight){
		Column cur = layout.get(layout.size() - 1);
		if(cur.getHeight() > maxColHeight && layout.size() < maxCols){ //create new column if column is full and more columns possible
			cur = new Column();
			layout.add(cur);
		}
		return cur;
	}
	
	protected static int getWidth(List<Component> panels){
		int width = 0;
		for(Component c : panels){
			int cWidth = c.getPreferredSize().width;
			if(cWidth > width) width = cWidth;
		}
		return width;
	}
	
	protected static int getHeight(List<Component> panels){
		int height = 0;
		for(Component c : panels){
			height += c.getPreferredSize().height;
		}
		return height;
	}
	
	protected GridBagConstraints argPanelConstraints(){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 1.0;
		return gbc;
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
		JPanel panel = new JPanel(new GridBagLayout());
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




