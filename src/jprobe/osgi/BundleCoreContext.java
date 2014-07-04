package jprobe.osgi;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.save.LoadException;
import jprobe.services.CoreEvent;
import jprobe.services.CoreListener;
import jprobe.services.JProbeCore;
import jprobe.services.Workspace;
import jprobe.services.data.Data;
import jprobe.services.data.ReadException;
import jprobe.services.data.WriteException;
import jprobe.services.function.Function;

public class BundleCoreContext implements JProbeCore, CoreListener{
	
	private static final Logger LOG = LoggerFactory.getLogger(BundleCoreContext.class);
	private static final int CACHE_CLEAN_RATE = 100;
	
	private final Collection<CoreListener> m_Listeners = new ArrayList<CoreListener>();
	private final Map<Workspace, WeakReference<BundleWorkspaceContext>> m_Cache = new WeakHashMap<Workspace, WeakReference<BundleWorkspaceContext>>();
	private int m_Queries = 0;
	
	private final Bundle m_Bundle;
	private final JProbeCore m_Parent;
	
	public BundleCoreContext(Bundle bundle, JProbeCore parent){
		m_Bundle = bundle;
		m_Parent = parent;
	}
	
	protected Workspace getBundleWorkspace(Workspace w){
		//first check the cache to see if a bundle workspace context has already been created for this workspace
		if(m_Cache.containsKey(w)){
			WeakReference<BundleWorkspaceContext> ref = m_Cache.get(w);
			Workspace cached = ref.get();
			if(cached != null){
				return cached;
			}
		}
		if(++m_Queries >= CACHE_CLEAN_RATE){
			//clean the cache and reset the counter
			Iterator<Entry<Workspace, WeakReference<BundleWorkspaceContext>>> iter = m_Cache.entrySet().iterator();
			while(iter.hasNext()){
				Entry<Workspace, WeakReference<BundleWorkspaceContext>> cur = iter.next();
				if(cur.getValue().get() == null){
					iter.remove();
				}
			}
			m_Queries = 0;
		}
		//otherwise, create a new bundle workspace context, cache it, and return it
		BundleWorkspaceContext context = new BundleWorkspaceContext(m_Bundle, w);
		m_Cache.put(w, new WeakReference<BundleWorkspaceContext>(context));
		return context;
	}
	
	@Override
	public String toString(){
		return m_Parent.toString();
	}
	
	@Override
	public Mode getMode() {
		return m_Parent.getMode();
	}

	@Override
	public String getName() {
		return m_Parent.getName();
	}

	@Override
	public String getVersion() {
		return m_Parent.getVersion();
	}

	@Override
	public String getPreferencesDir() {
		return m_Parent.getPreferencesDir();
	}

	@Override
	public String getLogsDir() {
		return m_Parent.getLogsDir();
	}

	@Override
	public String getUserDir() {
		return m_Parent.getUserDir();
	}

	@Override
	public void addCoreListener(CoreListener listener) {
		if(m_Listeners.isEmpty()){
			m_Parent.addCoreListener(this);
		}
		m_Listeners.add(listener);
	}

	@Override
	public void removeCoreListener(CoreListener listener) {
		m_Listeners.remove(listener);
		if(m_Listeners.isEmpty()){
			m_Parent.removeCoreListener(this);
		}
	}

	@Override
	public void shutdown() {
		LOG.info("Shutdown called by bundle: {}", m_Bundle);
		m_Parent.shutdown();
	}

	@Override
	public Workspace newWorkspace() {
		LOG.info("New workspace called by bundle: {}", m_Bundle);
		return this.getBundleWorkspace(m_Parent.newWorkspace());
	}

	@Override
	public Workspace openWorkspace(InputStream in, String source) throws LoadException {
		LOG.info("Open workspace from source: {} called by bundle: {}", source, m_Bundle );
		return this.getBundleWorkspace(m_Parent.openWorkspace(in, source));
	}

	@Override
	public Workspace getWorkspace(int index) {
		return this.getBundleWorkspace(m_Parent.getWorkspace(index));
	}

	@Override
	public int indexOf(Workspace w) {
		if(w instanceof BundleWorkspaceContext){
			BundleWorkspaceContext context = (BundleWorkspaceContext) w;
			return m_Parent.indexOf(context.getParentWorkspace());
		}
		return m_Parent.indexOf(w);
	}

	@Override
	public List<Workspace> getWorkspaces() {
		List<Workspace> contexts = new ArrayList<Workspace>();
		for(Workspace w : m_Parent.getWorkspaces()){
			contexts.add(this.getBundleWorkspace(w));
		}
		return Collections.unmodifiableList(contexts);
	}

	@Override
	public void closeWorkspace(int index) {
		LOG.info("Close workspace with index: {} called by bundle: {}", index, m_Bundle);
		m_Parent.closeWorkspace(index);
	}

	@Override
	public void closeWorkspace(Workspace w) {
		LOG.info("Close workspace: {} called by bundle: {}", w, m_Bundle);
		if(w instanceof BundleWorkspaceContext){
			BundleWorkspaceContext context = (BundleWorkspaceContext) w;
			m_Parent.closeWorkspace(context.getParentWorkspace());
		}else{
			m_Parent.closeWorkspace(w);
		}
	}

	@Override
	public int numWorkspaces() {
		return m_Parent.numWorkspaces();
	}

	@Override
	public Collection<Function<?>> getFunctions() {
		return m_Parent.getFunctions();
	}

	@Override
	public Collection<Class<? extends Data>> getReadableDataClasses() {
		return m_Parent.getReadableDataClasses();
	}

	@Override
	public List<FileFilter> getReadFormats(Class<? extends Data> readClass) {
		return m_Parent.getReadFormats(readClass);
	}

	@Override
	public <D extends Data> D readData(Class<D> readClass, FileFilter format, InputStream in) throws ReadException {
		LOG.info("Read {} using format: {} from stream: {} called by bundle: {}", readClass, format, in, m_Bundle);
		return m_Parent.readData(readClass, format, in);
	}

	@Override
	public boolean isReadable(Class<? extends Data> dataClass) {
		return m_Parent.isReadable(dataClass);
	}

	@Override
	public Collection<Class<? extends Data>> getWritableDataClasses() {
		return m_Parent.getWritableDataClasses();
	}

	@Override
	public List<FileNameExtensionFilter> getWriteFormats(
			Class<? extends Data> writeClass) {
		return m_Parent.getWriteFormats(writeClass);
	}

	@Override
	public void writeData(Data d, FileNameExtensionFilter format,
			OutputStream out) throws WriteException {
		LOG.info("Write {} using format: {} to stream: {} called by bundle: {}", d != null ? d.getClass() : null, format, out, m_Bundle);
		m_Parent.writeData(d, format, out);
	}

	@Override
	public boolean isWritable(Class<? extends Data> dataClass) {
		return m_Parent.isWritable(dataClass);
	}
	
	protected void fireCoreEvent(CoreEvent event){
		for(CoreListener l : m_Listeners){
			l.update(this, event);
		}
	}

	@Override
	public void update(JProbeCore source, CoreEvent event) {
		if(source == m_Parent){
			switch(event.type){
			case WORKSPACE_CLOSED:
				this.fireCoreEvent(new CoreEvent(event.type, this.getBundleWorkspace(event.workspace)));
				break;
			case WORKSPACE_NEW:
				this.fireCoreEvent(new CoreEvent(event.type, this.getBundleWorkspace(event.workspace)));
				break;
			default:
				this.fireCoreEvent(event);
				break;
			}
		}
	}

}
