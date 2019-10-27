package br.usp.iterador.plugin;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenu;

import br.usp.iterador.gui.menu.MenuBuilder;
import br.usp.iterador.gui.util.GuiFactory;
import br.usp.iterador.logic.Component;
import br.usp.iterador.logic.Controller;

/**
 * Plugin menu manager.
 *
 * @author Guilherme Silveira
 */
@Component(autoCreate = true)
public class MenuManager {

	private final Map<Plugin, JMenu> menus = new HashMap<Plugin, JMenu>();

	private final JMenu menu;

	private final Controller controller;

	private final MenuBuilder menuBuilder;

	public MenuManager(MenuBuilder menuBuilder, GuiFactory guiFactory,
			Controller controller) {
		this.controller = controller;
		this.menu = guiFactory.getMenu("plugins");
		this.menuBuilder = menuBuilder;
		menu.add(guiFactory.getMenuItem("plugins_tooltip", "showPluginFrame",
				controller));
	}

	/**
	 * Activates a plugin menu.
	 */
	public void activateMenu(Plugin plugin) {
		JMenu menu = plugin.getMenu(menuBuilder);
		if (menu != null) {
			this.menus.put(plugin, menu);
			this.menu.add(menu);
			controller.executeAction("UpdateMenu");
		}
	}

	/**
	 * Removes a menu.
	 */
	public void deactivateMenu(Plugin p) {
		JMenu m = menus.remove(p);
		if (m != null) {
			this.menu.remove(m);
		}
	}

	public JMenu getMenu() {
		return menu;
	}

}
