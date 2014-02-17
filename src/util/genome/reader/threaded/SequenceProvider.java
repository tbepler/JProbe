package util.genome.reader.threaded;

import util.genome.GenomicRegion;

public class SequenceProvider {
	
	private final Object m_WorkerLock = new Object();
	private final Object m_SupplierLock = new Object();
	
	private volatile String m_Sequence;
	private volatile GenomicRegion m_Region;
	private int m_Workers;
	private int m_ProcessingWorkers;
	private int m_CompletedWorkers;
	private boolean m_Updating;
	private boolean m_Initialized;
	private boolean m_Done;
	
	public SequenceProvider(int numWorkers){
		m_Sequence = null;
		m_Region = null;
		m_Workers = numWorkers;
		m_ProcessingWorkers = 0;
		m_CompletedWorkers = numWorkers;
		m_Updating = false;
		m_Initialized = false;
		m_Done = false;
	}
	
	private void checkDataReadyAndWait(){
		synchronized(m_WorkerLock){
			while(m_Updating || !m_Initialized){
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public String getSequence(){
		this.checkDataReadyAndWait();
		return m_Sequence;
	}
	
	public GenomicRegion getRegion(){
		this.checkDataReadyAndWait();
		return m_Region;
	}
	
	public void processing(){
		synchronized(m_WorkerLock){
			m_ProcessingWorkers++;
			m_WorkerLock.notifyAll();
		}
	}
	
	private boolean allWorkersProcessing(){
		return m_ProcessingWorkers == m_Workers;
	}
	
	public void processingComplete(){
		synchronized(m_WorkerLock){
			while(!this.allWorkersProcessing()){
				try {
					m_WorkerLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			m_CompletedWorkers++;
			if(m_CompletedWorkers >= m_Workers){
				m_ProcessingWorkers = 0;
			}
		}
		synchronized(m_SupplierLock){
			m_SupplierLock.notifyAll();
		}
		this.waitForNewData();
	}
	
	private void waitForNewData(){
		synchronized(m_WorkerLock){
			while(m_CompletedWorkers != 0){
				try {
					m_WorkerLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void workerTerminated(){
		synchronized(m_WorkerLock){
			while(!this.allWorkersProcessing()){
				try {
					m_WorkerLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			m_Workers--;
			if(m_CompletedWorkers >= m_Workers){
				m_ProcessingWorkers = 0;
			}
		}
		synchronized(m_SupplierLock){
			m_SupplierLock.notifyAll();
		}
	}
	
	private boolean allWorkersCompleted(){
		synchronized(m_WorkerLock){
			return m_CompletedWorkers >= m_Workers;
		}
	}
	
	public void setData(String sequence, GenomicRegion region){
		synchronized(m_SupplierLock){
			while(!this.allWorkersCompleted()){
				try {
					m_SupplierLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		synchronized(m_WorkerLock){
			m_Updating = true;
			m_Sequence = sequence;
			m_Region = region;
			if(!m_Initialized){
				m_Initialized = true;
			}
			m_CompletedWorkers = 0;
			m_Updating = false;
			m_WorkerLock.notifyAll();
		}
	}
	
	public boolean done(){
		synchronized(m_WorkerLock){
			while(!m_Initialized){
				try {
					m_WorkerLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return m_Done;
		}
	}
	
	public void setDone(boolean done){
		synchronized(m_SupplierLock){
			while(!this.allWorkersCompleted()){
				try {
					m_SupplierLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		synchronized(m_WorkerLock){
			m_Done = done;
			m_CompletedWorkers = 0;
			m_WorkerLock.notifyAll();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
