package br.usp.iterador.plugin;

import javax.swing.JMenu;

import br.usp.iterador.gui.menu.MenuBuilder;
import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.plugin.tool.Tool;

/**
 * A plugin interface.
 * 
 * @author Guilherme Silveira
 */
public interface Plugin {

	/**
	 * Returns its menu bar
	 * 
	 * @param menuBuilder
	 * 
	 * @return the menu bar
	 */
	JMenu getMenu(MenuBuilder menuBuilder);

	/**
	 * Returns an iteration listener if it should change its behaviour or null
	 * otherwise
	 * 
	 * @return the iteration listener, null otherwise
	 */
	IterationListener getIterationListener();

	/**
	 * Activates this plugins
	 */
	void activate();

	/**
	 * Deactivates this plugin
	 */
	void deactivate();

	/**
	 * Returns any data that should be serialized in order to save and load
	 * 
	 * @return the data or null if nothing should be saved
	 */
	Object getSerializedData();

	/**
	 * Recovers its internal structure from a serializable object
	 * 
	 * @param s
	 *            object
	 */
	void activate(Object s);

	/**
	 * Returns this plugins tools. This method is always called after
	 * activation.
	 * 
	 * @return the tools
	 */
	Tool[] getTools();

}
