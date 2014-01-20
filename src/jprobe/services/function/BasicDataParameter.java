package jprobe.services.function;

import jprobe.services.data.Data;

public class BasicDataParameter extends AbstractDataParameter{

	public BasicDataParameter(String name, String description, boolean optional, Class<? extends Data> type) {
		super(name, description, optional, type);
	}

	@Override
	public boolean isValid(Data data) {
		return data.getClass() == this.getType();
	}

}
