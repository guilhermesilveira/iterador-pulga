package br.usp.iterador.plugin;

import java.util.ArrayList;
import java.util.List;

import br.usp.iterador.util.Pair;

/**
 * Serializes all plugins extra configuration.
 *
 * @author Guilherme Silveira
 */
public class PluginSerializer {

	private final PluginManager pluginManager;

	public PluginSerializer(PluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}

	/**
	 * Returns the serialized data list
	 */
	public List<Pair<Class<? extends Plugin>, Object>> getSerializedData() {

		List<Pair<Class<? extends Plugin>, Object>> pluginList = new ArrayList<Pair<Class<? extends Plugin>, Object>>();
		for (Plugin plugin : pluginManager.getActivatedPlugins()) {
			pluginList.add(new Pair<Class<? extends Plugin>, Object>(plugin
					.getClass(), plugin.getSerializedData()));
		}
		return pluginList;

	}

}
