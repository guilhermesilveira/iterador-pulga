package br.usp.iterador.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenu;

import org.apache.log4j.Logger;

import br.usp.iterador.logic.ArrayIterationListener;
import br.usp.iterador.logic.Controller;
import br.usp.iterador.logic.GraphicIterationListener;
import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.model.Application;
import br.usp.iterador.plugin.bacia.BasinPlugin;
import br.usp.iterador.plugin.bifurcationdiagram.DiagramPlugin;
import br.usp.iterador.plugin.cor.BufferedColorChange;
import br.usp.iterador.plugin.edo.euler.EulerPlugin;
import br.usp.iterador.plugin.export.ExportImagePlugin;
import br.usp.iterador.plugin.initialcondition.InitialConditionPlugin;
import br.usp.iterador.plugin.manifold.StableManifoldPlugin;
import br.usp.iterador.plugin.setiterator.InitialCurvePlugin;
import br.usp.iterador.plugin.tool.Tool;
import br.usp.iterador.plugin.tool.ToolManager;
import br.usp.iterador.plugin.zoom.ZoomPlugin;

/**
 * This is the guy who deals with all plugins.
 *
 * @author Guilherme Silveira
 */
public class DefaultPluginManager implements PluginManager {

	private static final Logger LOG = Logger.getLogger(DefaultPluginManager.class);

	private final List<Class<? extends Plugin>> pluginClasses = new ArrayList<Class<? extends Plugin>>();

	private final Map<Class<? extends Plugin>, Plugin> activatedPlugins = new HashMap<Class<? extends Plugin>, Plugin>();

	private final Controller controller;

	private final MenuManager menuManager;

	private final ToolManager toolManager;

	public DefaultPluginManager(Controller controller, MenuManager menuManager,
			ToolManager toolManager) {
		this.controller = controller;
		this.menuManager = menuManager;
		this.toolManager = toolManager;
	}

	public void registerPlugin(Class<? extends Plugin> type) {
		this.pluginClasses.add(type);
	}

	public synchronized Plugin activatePlugin(Class<? extends Plugin> clazz,
			Object data) throws PluginActivationException {

		LOG.info("activating plugin " + clazz.getName());

		Plugin plugin = controller.newInstance(clazz);
		if (data == null) {
			plugin.activate();
		} else {
			plugin.activate(data);
		}

		this.activatedPlugins.put(clazz, plugin);
		this.menuManager.activateMenu(plugin);
		this.toolManager.installTools(plugin);

		return plugin;

	}

	/**
	 * Returns a list with all plugins.
	 */
	public synchronized List<Class<? extends Plugin>> getPluginClasses() {
		return pluginClasses;
	}

	/**
	 * Returns a list with all activated plugins
	 *
	 * @return Returns the activatedPlugins.
	 */
	public synchronized List<Plugin> getActivatedPlugins() {
		return new ArrayList<Plugin>(activatedPlugins.values());
	}

	/**
	 * Returns its menu.
	 */
	public JMenu getMenu() {
		return this.menuManager.getMenu();
	}

	public boolean isActive(Class<? extends Plugin> clazz) {
		return activatedPlugins.containsKey(clazz);
	}

	/**
	 * Deactivates a plugin
	 *
	 * @param clazz
	 *            class
	 */
	public synchronized void deactivatePlugin(Class<? extends Plugin> clazz) {
		Plugin plugin = activatedPlugins.remove(clazz);
		LOG.info("deactivating plugin " + plugin.getClass().getName());
		this.menuManager.deactivateMenu(plugin);
		this.toolManager.uninstallTools(plugin);
		plugin.deactivate();
	}

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
	public <T extends Plugin> T getActivePlugin(Class<T> clazz) {
		return (T) activatedPlugins.get(clazz);
	}

	/**
	 * Deactivates all plugins.
	 */
	public synchronized void deactivateAll() {
		for (Class<? extends Plugin> p : new ArrayList<Class<? extends Plugin>>(
				activatedPlugins.keySet()))
			deactivatePlugin(p);
	}

	/**
	 * Reset its data.
	 */
	public void reset() throws PluginActivationException {
		deactivateAll();
		activatePlugin(ExportImagePlugin.class, null);
		activatePlugin(InitialConditionPlugin.class, null);
		activatePlugin(ZoomPlugin.class, null);
		activatePlugin(DiagramPlugin.class, null);
		activatePlugin(StableManifoldPlugin.class, null);
		activatePlugin(InitialCurvePlugin.class, null);
		activatePlugin(BasinPlugin.class, null);
		activatePlugin(BufferedColorChange.class, null);
		activatePlugin(EulerPlugin.class, null);
	}

	/**
	 * Returns all tools.
	 *
	 * @return tools
	 */
	public List<Tool> getTools() {
		return this.toolManager.getTools();
	}

	/**
	 * Returns the current iteration listener
	 *
	 * @return the current iteration listener
	 */
	public IterationListener getIterationListener(Application app,
			boolean paintPoints, IterationListener... extras) {

		ArrayIterationListener listeners = new ArrayIterationListener();

		// for each plugin, asks for an iteration listener, if someone supplies
		// one, uses it
		if (listeners.size() != 0) {
			return listeners;
		}

		for (Plugin plugin : getActivatedPlugins()) {
			IterationListener listener = plugin.getIterationListener();
			if (listener != null) {
				listeners.add(listener);
			}
		}

		for (IterationListener listener : extras) {
			listeners.add(listener);
		}

		// if noone has supplied it, uses the default GraphicIterationListener
		if (paintPoints) {
			listeners.add(new GraphicIterationListener(app));
		}

		return listeners;
	}

}
