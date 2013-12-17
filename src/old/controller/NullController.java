package old.controller;

import old.datatypes.DataType;
import old.view.data.DataListener;

public class NullController implements CoreController{

	@Override
	public void update(int event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DataType[] selectArgs(Class<? extends DataType>[] requiredArgs,
			Class<? extends DataType>[] optionalArgs) {
		// TODO Auto-generated method stub
		return new DataType[]{};
	}

}
