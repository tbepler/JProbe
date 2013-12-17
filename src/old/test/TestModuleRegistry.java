package old.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

import old.core.ModuleRegistry;
import old.modules.Module;

import org.xml.sax.SAXException;

public class TestModuleRegistry extends junit.framework.TestCase{

	
	public void testRegistry() throws ClassNotFoundException, InstantiationException, IllegalAccessException, URISyntaxException, IOException, SAXException{
		ModuleRegistry r = new ModuleRegistry("Extensions", "src/modules.xml");
		Collection<String> names = r.getModuleNames();
		assertEquals(2, names.size());
		assertTrue(names.contains("test_module"));
		assertTrue(names.contains("extension"));
		Module test = r.getModule("test_module");
		assertEquals("A module for testing purposes.", r.getDescription("test_module"));
		Module extension = r.getModule("extension");
		assertEquals("A module for testing module extensions.", r.getDescription("extension"));
	}
		
}
