package chiptools.jprobe.function.agilentformatter;

import java.io.File;
import java.util.List;

import util.progress.ProgressListener;
import chiptools.Constants;
import chiptools.Resources;
import chiptools.jprobe.ChiptoolsActivator;
import chiptools.jprobe.data.Probes;
import chiptools.jprobe.function.agilentformatter.component.DataCategoriesComponent;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.function.DataArgument;
import jprobe.services.function.Function;
import jprobe.services.function.ListArgument;
import jprobe.services.function.components.DataArgsComponent.DataValidFunction;

public class ProbeCategoriesArgument extends ListArgument<AgilentFormatterParams, DataCategory<Probes>, DataCategoriesComponent<Probes>>{
	
	private final JProbeCore m_Core = ChiptoolsActivator.getCore();
	
	@SuppressWarnings("rawtypes")
	public ProbeCategoriesArgument(Class<? extends Function> funcClass, boolean optional) {
		super(
				Resources.getArgumentName(funcClass, ProbeCategoriesArgument.class),
				Resources.getArgumentDescription(funcClass, ProbeCategoriesArgument.class),
				Resources.getArgumentCategory(funcClass, ProbeCategoriesArgument.class),
				Resources.getArgumentFlag(funcClass, ProbeCategoriesArgument.class),
				Resources.getArgumentPrototype(funcClass, ProbeCategoriesArgument.class),
				optional
				);
	}

	@Override
	protected DataCategoriesComponent<Probes> generateComponent() {
		DataCategoriesComponent<Probes> comp = new DataCategoriesComponent<Probes>(
				m_Core,
				1,
				Integer.MAX_VALUE,
				false,
				Probes.class,
				new DataValidFunction(){

					@Override
					public boolean isValid(Data d) {
						return d instanceof Probes;
					}
					
				}
				);
		return comp;
	}

	@Override
	protected boolean isValid(DataCategoriesComponent<Probes> comp) {
		if(comp == null) return false;
		return comp.isStateValid();
	}

	@Override
	protected void process(AgilentFormatterParams params, List<DataCategory<Probes>> entries) {
		params.PROBE_CATEGORIES = entries;
	}

	@Override
	protected List<DataCategory<Probes>> getUserInput(DataCategoriesComponent<Probes> comp) {
		return comp.getDataCategories();
	}
	
	@Override
	protected List<DataCategory<Probes>> parse(ProgressListener l, String[] args){
		if(args.length < 1){
			throw new RuntimeException(this.getName()+" requires at least 1 argument.");
		}
		return super.parse(l, args);
	}
	
	@Override
	protected DataCategory<Probes> parse(ProgressListener l, String arg) {
		String[] tokens = arg.split(Constants.PROBES_CATEGORIES_SEPARATOR);
		String category;
		String probeStr;
		switch(tokens.length){
		case 1:
			probeStr = tokens[0].trim();
			category = new File(probeStr).getName();
			break;
		case 2:
			category = tokens[0].trim();
			probeStr = tokens[1].trim();
			break;
		default:
			throw new RuntimeException("Cannot parse probe category argument: "+arg);
		}
		Probes p = DataArgument.parse(
				m_Core,
				Probes.class,
				new DataValidFunction(){

					@Override
					public boolean isValid(Data d) {
						return d instanceof Probes;
					}

				},
				probeStr,
				l
				);
		return new DataCategory<Probes>(category, p);
		
	}

}
