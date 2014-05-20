package plugins.testDataAndFunction.params;

import java.util.Collection;

import plugins.testDataAndFunction.TestData;

public interface DataParam {
	
	public void setData(Collection<TestData> data);
	public Collection<TestData> getData();
	
}


