package br.usp.iterador.gui.menu;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.apache.log4j.Logger;

import br.usp.iterador.logic.Controller;
import br.usp.iterador.plugin.PluginManager;

/**
 * Menu loader.
 * 
 * @author Guilherme Silveira
 */
public class MenuLoader {

	private static final Logger LOG = Logger.getLogger(MenuLoader.class);

	private PluginManager pluginManager;

	private Controller controller;

	public MenuLoader(PluginManager pluginManager, Controller controller) {
		LOG.debug("Creating a menu loader with " + controller);
		this.pluginManager = pluginManager;
		this.controller = controller;
	}

	/**
	 * Loads a menu bar.
	 * 
	 * @return the menu bar
	 */
	public JMenuBar load() {
		JMenuBar barraDeMenu = new JMenuBar();
		barraDeMenu.setPreferredSize(new Dimension(500, 30));
		List<JMenu> loadedBar = new MenuBuilder(controller).init("menus.txt");
		barraDeMenu.add(loadedBar.get(0));
		barraDeMenu.add(loadedBar.get(1));
		barraDeMenu.add(loadedBar.get(2));
		barraDeMenu.add(loadedBar.get(3));
		barraDeMenu.add(pluginManager.getMenu());
		return barraDeMenu;
	}

}
