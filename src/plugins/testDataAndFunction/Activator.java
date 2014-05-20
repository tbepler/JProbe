package plugins.testDataAndFunction;

import jprobe.services.Debug;
import jprobe.services.JProbeCore;
import jprobe.services.Log;
import jprobe.services.data.DataReader;
import jprobe.services.data.DataWriter;
import jprobe.services.function.Function;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator{
	
	private static Bundle BUNDLE = null;
	private static JProbeCore CORE = null;
	
	public static Bundle getBundle(){
		return BUNDLE;
	}
	
	public static JProbeCore getCore(){
		return CORE;
	}
	
	private JProbeCore core;
	private Function fun = new TestFunction();
	private Function longFun = null;
	private Function dataParamFun = new DataParamFunction();
	private Function fieldParamFun = new FieldParamFunction();
	private Function dataFieldParamFun = new DataFieldFunction();
	private DataReader testReader = new TestDataReader();
	private DataWriter testWriter = new TestDataWriter();
	
	@Override
	public void start(BundleContext context) throws Exception {
		BUNDLE = context.getBundle();
		longFun = new LongFunction(context.getBundle());
		ServiceReference ref = context.getServiceReference(JProbeCore.class);
		CORE = (JProbeCore) context.getService(ref);
		core = CORE;
		core.getFunctionManager().addFunction(fun, context.getBundle());
		core.getFunctionManager().addFunction(longFun, context.getBundle());
		core.getFunctionManager().addFunction(dataParamFun, context.getBundle());
		core.getFunctionManager().addFunction(fieldParamFun, context.getBundle());
		core.getFunctionManager().addFunction(dataFieldParamFun, context.getBundle());
		core.getDataManager().addDataReader(testReader, context.getBundle());
		core.getDataManager().addDataWriter(testWriter, context.getBundle());
		if(Debug.getLevel() == Debug.FULL || Debug.getLevel() == Debug.LOG){
			Log.getInstance().write(context.getBundle(), "TestPlugin started.");
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		core.getFunctionManager().removeFunction(fun, context.getBundle());
		core.getFunctionManager().removeFunction(longFun, context.getBundle());
		core.getFunctionManager().removeFunction(dataParamFun, context.getBundle());
		core.getFunctionManager().removeFunction(fieldParamFun, context.getBundle());
		core.getFunctionManager().removeFunction(dataFieldParamFun, context.getBundle());
		core.getDataManager().removeDataReader(testReader, context.getBundle());
		core.getDataManager().removeDataWriter(testWriter, context.getBundle());
		if(Debug.getLevel() == Debug.FULL || Debug.getLevel() == Debug.LOG){
			Log.getInstance().write(context.getBundle(), "TestPlugin stopped.");
		}
	}

}
