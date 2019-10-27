package br.usp.iterador.plugin.tool;

import java.util.List;

import br.usp.iterador.plugin.Plugin;

/**
 * A tool repository.
 *
 * @author Guilherme Silveira
 */
public interface ToolRepository {

	/**
	 * Install all tools from this plugin
	 */
	void installTools(Plugin plugin);

	/**
	 * Removes all tools related to this plugin and change the tool to the show
	 * point if needed
	 */
	Tool[] uninstallTools(Plugin plugin);

	/**
	 * Returns all tools
	 */
	List<Tool> getTools();

}
