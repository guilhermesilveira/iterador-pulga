package br.usp.iterador.plugin.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.usp.iterador.plugin.Plugin;
import br.usp.iterador.tool.ToolComparator;

/**
 *
 * @author Guilherme Silveira
 * @since 1.4
 */
public class DefaultToolRepository implements ToolRepository {

	private final Map<Plugin, Tool[]> toolsMap = new HashMap<Plugin, Tool[]>();

	public DefaultToolRepository() {
	}

	public Tool[] uninstallTools(Plugin plugin) {
		return toolsMap.remove(plugin);
	}

	public void installTools(Plugin plugin) {
		toolsMap.put(plugin, plugin.getTools());
	}

	public List<Tool> getTools() {
		List<Tool> tools = new ArrayList<Tool>();
		for (Tool[] rightSide : toolsMap.values()) {
			tools.addAll(Arrays.asList(rightSide));
		}
		Collections.sort(tools, new ToolComparator());
		return tools;
	}

}
