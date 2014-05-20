package plugins.testDataAndFunction.params;

import java.util.ArrayList;
import java.util.Collection;

import plugins.testDataAndFunction.TestData;

public class FieldDataParams extends FieldParams implements DataParam{
	
	private Collection<TestData> m_Data = new ArrayList<TestData>();

	@Override
	public void setData(Collection<TestData> data) {
		m_Data = data;
	}

	@Override
	public Collection<TestData> getData() {
		return m_Data;
	}
	
}
