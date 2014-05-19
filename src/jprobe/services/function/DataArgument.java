package jprobe.services.function;

import java.util.List;

import javax.swing.JComponent;

import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.function.components.DataArgsComponent;
import jprobe.services.function.components.ValidListener;
import jprobe.services.function.components.ValidNotifier;

public abstract class DataArgument<P> extends AbstractArgument<P> implements ValidListener {
	
	private final DataArgsComponent m_Component;
	private final Class<? extends Data> m_DataClass;
	
	protected DataArgument(JProbeCore core, String name, String tooltip, String category, boolean optional,
			Class<? extends Data> dataClass, int minArgs, int maxArgs, boolean allowDuplicates){
		super(name, tooltip, category, optional);
		m_DataClass = dataClass;
		m_Component = new DataArgsComponent(
				core,
				minArgs,
				maxArgs,
				allowDuplicates,
				new DataArgsComponent.DataValidFunction() { @Override public boolean isValid(Data d) { return DataArgument.this.isValid(d); } }
				);
		m_Component.addListener(this);
	}
		
	protected abstract P process(P params, List<Data> data);

	@Override
	public boolean isValid() { return m_Component.isStateValid(); }
	
	protected boolean isValid(Data d){
		return m_DataClass.isAssignableFrom(d.getClass());
	}

	@Override
	public JComponent getComponent() {
		return m_Component;
	}

	@Override
	public P process(P params) {
		return process(params, m_Component.getDataArgs());
	}

	@Override
	public void update(ValidNotifier notifier, boolean valid) {
		this.notifyListeners();
	}


}
