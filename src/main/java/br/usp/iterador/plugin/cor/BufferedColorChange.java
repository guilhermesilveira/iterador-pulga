package br.usp.iterador.plugin.cor;

import javax.swing.JMenu;

import br.usp.iterador.gui.menu.MenuBuilder;
import br.usp.iterador.gui.menu.MenuConfigurator;
import br.usp.iterador.logic.Controller;
import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.plugin.Plugin;
import br.usp.iterador.plugin.tool.Tool;

/**
 * Runs buffered color changes.
 * 
 * @author Guilherme Silveira
 */
public class BufferedColorChange implements Plugin {

	private Controller controller;

	public BufferedColorChange(Controller controller) {
		this.controller = controller;
	}

	public JMenu getMenu(MenuBuilder menuBuilder) {
		MenuConfigurator menu = menuBuilder
				.getMenu("bufferedcolorchange.title");
		menu.addExecute("bufferedcolorchange.title", "plugin.bcc.change");
		return menu.getJMenu();
	}

	public IterationListener getIterationListener() {
		return null;
	}

	public void activate() {
		controller.registerAction("plugin.bcc.change", ChangeAColor.class);
	}

	public void deactivate() {
		controller.unregisterAction("plugin.bcc.change");
	}

	public Object getSerializedData() {
		return null;
	}

	public void activate(Object s) {
	}

	public Tool[] getTools() {
		return new Tool[0];
	}

}
