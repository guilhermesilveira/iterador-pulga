package br.usp.iterador.plugin;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Mock;

import br.usp.iterador.AbstractTestCase;
import br.usp.iterador.util.Pair;

public class PluginSerializerTest extends AbstractTestCase {

	public void testCollectsAllInfo() {

		List<Plugin> plugins = new ArrayList<Plugin>();
		List<Object> values = new ArrayList<Object>();
		for (int i = 0; i < 5; i++) {
			Object value = new Object();
			values.add(value);
			Mock plugin = mock(Plugin.class);
			plugin.expects(once()).method("getSerializedData").will(returnValue(values.get(i)));
			plugins.add((Plugin) plugin.proxy());
		}

		Mock manager = mock(PluginManager.class);
		manager.expects(once()).method("getActivatedPlugins").will(returnValue(plugins));

		PluginSerializer serializer = new PluginSerializer((PluginManager) manager.proxy());
		List<Pair<Class<? extends Plugin>, Object>> result = serializer.getSerializedData();
		assertEquals(plugins.size(), result.size());
		for (int i = 0; i < 5; i++) {
			assertEquals(values.get(i), result.get(i).getSecond());
		}

	}

}
