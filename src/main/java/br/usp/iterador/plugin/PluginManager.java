package br.usp.iterador.plugin;

import java.util.List;

import javax.swing.JMenu;

import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.model.Application;
import br.usp.iterador.plugin.tool.Tool;

public interface PluginManager {

	/**
	 * Registers a plugin.
	 */
	void registerPlugin(Class<? extends Plugin> type);

	/**
	 * Activates a specific plugin.
	 */
	Plugin activatePlugin(Class<? extends Plugin> clazz, Object data)
			throws PluginActivationException;

	/**
	 * Returns a list with all plugins.
	 */
	List<Class<? extends Plugin>> getPluginClasses();

	/**
	 * Returns a list with all activated plugins
	 *
	 * @return Returns the activatedPlugins.
	 */
	List<Plugin> getActivatedPlugins();

	/**
	 * Returns its menu.
	 */
	JMenu getMenu();

	boolean isActive(Class<? extends Plugin> clazz);

	/**
	 * Deactivates a plugin
	 *
	 * @param clazz
	 *            class
	 */
	void deactivatePlugin(Class<? extends Plugin> clazz);

	/**
	 * Gets an active plugin
	 *
	 * @param <T>
	 *            plugin type
	 * @param clazz
	 *            plugin class
	 * @return plugin or null if not active
	 */
	@SuppressWarnings("unchecked")
	<T extends Plugin> T getActivePlugin(Class<T> clazz);

	/**
	 * Deactivates all plugins.
	 */
	void deactivateAll();

	/**
	 * Reset its data.
	 */
	void reset() throws PluginActivationException;

	/**
	 * Returns all tools.
	 *
	 * @return tools
	 */
	List<Tool> getTools();

	/**
	 * Returns the current iteration listener
	 *
	 * @return the current iteration listener
	 */
	IterationListener getIterationListener(Application app,
			boolean paintPoints, IterationListener... extras);

}