package chiptools.jprobe.function.agilentformatter.component;

import java.util.ArrayList;
import java.util.List;

import chiptools.jprobe.function.agilentformatter.DataCategory;
import jprobe.services.JProbeCore;
import jprobe.services.data.Data;
import jprobe.services.function.components.DataArgsComponent;
import jprobe.services.function.components.DataSelectionPanel;

public class DataCategoriesComponent<D extends Data> extends DataArgsComponent<D>{
	private static final long serialVersionUID = 1L;
	
	public DataCategoriesComponent(
			JProbeCore core,
			int minArgs,
			int maxArgs,
			boolean allowDuplicates,
			Class<D> dataClass,
			jprobe.services.function.components.DataArgsComponent.DataValidFunction validFunction) {
		super(core, minArgs, maxArgs, allowDuplicates, dataClass, validFunction);
	}
	
	@Override
	protected DataSelectionPanel<D> newDataSelectionPanel(JProbeCore core, boolean optional){
		return new DataCategoriesSelectionComponent<D>(core, optional);
	}
	
	public List<DataCategory<D>> getDataCategories(){
		List<DataCategory<D>> list = new ArrayList<DataCategory<D>>();
		for(DataSelectionPanel<D> comp : this.getSelectionComps()){
			if(comp instanceof DataCategoriesSelectionComponent){
				DataCategoriesSelectionComponent<D> sel = (DataCategoriesSelectionComponent<D>) comp;
				DataCategory<D> category = sel.getDataCategory();
				if(category != null){
					list.add(category);
				}
			}
		}
		return list;
	}
	
}
