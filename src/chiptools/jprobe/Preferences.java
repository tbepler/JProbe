package chiptools.jprobe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.SwingUtilities;

import plugins.jprobe.gui.services.PreferencesPanel;
import jprobe.services.ErrorHandler;
import util.Observer;
import util.Subject;

public class Preferences extends PreferencesPanel implements Subject<Preferences.Update>{
	private static final long serialVersionUID = 1L;
	
	public enum Update{
		BINDING_SITE_HTML,
		MUTATION_SITE_HTML,
		BOTH;
	}
	
	private static final Preferences PREF = new Preferences();
	
	public static Preferences getInstance(){
		return PREF;
	}
	
	private static final String BINDING_SITE_TAG = "ProbeBindingSiteHTML";
	private static final String MUTATION_SITE_TAG = "ProbeMutationSiteHTML";
	
	private static final String BINDING_SITE_HTML_START = "<font color=red>";
	private static final String BINDING_SITE_HTML_END ="</font>";
	private static final String MUT_SITE_HTML_START = "<font color=blue>";
	private static final String MUT_SITE_HTML_END = "</font>";
	
	private final Collection<Observer<Update>> m_Obs = new HashSet<Observer<Update>>();
	
	private final HTMLEditorPanel m_BindingSite = new HTMLEditorPanel(BINDING_SITE_TAG);
	private final HTMLEditorPanel m_MutationSite = new HTMLEditorPanel(MUTATION_SITE_TAG);
	
	private String m_BindingStart = BINDING_SITE_HTML_START;
	private String m_BindingEnd = BINDING_SITE_HTML_END;
	private String m_MutStart = MUT_SITE_HTML_START;
	private String m_MutEnd = MUT_SITE_HTML_END;
	
	private Preferences(){ //singleton
		super();
		this.add(m_BindingSite);
		m_BindingSite.setStartHTML(m_BindingStart);
		m_BindingSite.setEndHTML(m_BindingEnd);
		this.add(m_MutationSite);
		m_MutationSite.setStartHTML(m_MutStart);
		m_MutationSite.setEndHTML(m_MutEnd);
	}
	
	@Override
	public void apply(){
		m_BindingStart = m_BindingSite.getStartHTML();
		m_BindingEnd = m_BindingSite.getEndHTML();
		m_MutStart = m_MutationSite.getStartHTML();
		m_MutEnd = m_MutationSite.getEndHTML();
		this.notifyObs(Update.BOTH);
	}
	
	@Override
	public void close(){
		//System.err.println("Closing");
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				m_BindingSite.setStartHTML(m_BindingStart);
				m_BindingSite.setEndHTML(m_BindingEnd);
				m_MutationSite.setStartHTML(m_MutStart);
				m_MutationSite.setEndHTML(m_MutEnd);
			}
			
		});
	}
	
	public void setBindingStartHTML(String html){
		m_BindingStart = html;
		m_BindingSite.setStartHTML(html);
		this.notifyObs(Update.BINDING_SITE_HTML);
	}
	
	public String getBindingStartHTML(){
		return m_BindingStart;
	}
	
	public void setBindingEndHTML(String html){
		m_BindingEnd = html;
		m_BindingSite.setEndHTML(html);
		this.notifyObs(Update.BINDING_SITE_HTML);
	}
	
	public String getBindingEndHTML(){
		return m_BindingEnd;
	}
	
	public void setMutStartHTML(String html){
		m_MutStart = html;
		m_MutationSite.setStartHTML(html);
		this.notifyObs(Update.MUTATION_SITE_HTML);
	}
	
	public String getMutStartHTML(){
		return m_MutStart;
	}
	
	public void setMutEndHTML(String html){
		m_MutEnd = html;
		m_MutationSite.setEndHTML(html);
		this.notifyObs(Update.MUTATION_SITE_HTML);
	}
	
	public String getMutEndHTML(){
		return m_MutEnd;
	}
	
	public void load(InputStream in){
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		try {
			while((line = reader.readLine()) != null){
				try{
					this.parse(line);
				}catch(Exception e){
					//do nothing
				}
			}
			reader.close();
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, ChiptoolsActivator.getBundle());
		}
	}
	
	protected void parse(String s){
		String[] tokens = s.split("\t");
		if(tokens[0].equals(BINDING_SITE_TAG)){
			this.setBindingStartHTML(tokens[1]);
			this.setBindingEndHTML(tokens[2]);
		}else if(tokens[0].equals(MUTATION_SITE_TAG)){
			this.setMutStartHTML(tokens[1]);
			this.setMutEndHTML(tokens[2]);
		}
	}
	
	public void save(OutputStream out){
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		try {
			writer.write(BINDING_SITE_TAG + "\t" + m_BindingStart + "\t" + m_BindingEnd + "\n");
			writer.write(MUTATION_SITE_TAG + "\t" + m_MutStart + "\t" + m_MutEnd + "\n");
			writer.close();
		} catch (IOException e) {
			ErrorHandler.getInstance().handleException(e, ChiptoolsActivator.getBundle());
		}
	}
	
	protected void notifyObs(Update u){
		for(Observer<Update> obs : m_Obs){
			obs.update(this, u);
		}
	}
	
	@Override
	public void register(Observer<Update> obs) {
		m_Obs.add(obs);
	}

	@Override
	public void unregister(Observer<Update> obs) {
		m_Obs.remove(obs);
	}
	
}
