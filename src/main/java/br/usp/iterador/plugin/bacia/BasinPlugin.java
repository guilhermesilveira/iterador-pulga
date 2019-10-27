package br.usp.iterador.plugin.bacia;

import javax.swing.JMenu;

import br.usp.iterador.gui.menu.MenuBuilder;
import br.usp.iterador.gui.menu.MenuConfigurator;
import br.usp.iterador.logic.Controller;
import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.plugin.Plugin;
import br.usp.iterador.plugin.gui.WindowManager;
import br.usp.iterador.plugin.tool.Tool;

/**
 * Basin plugin.
 * 
 * @author Guilherme Silveira
 */
public class BasinPlugin implements Plugin {

	private BasinController controller;

	private Basin basin = new Basin();

	private WindowManager windows;

	private Controller control;

	public BasinPlugin(WindowManager windows, Controller control) {
		this.windows = windows;
		this.control = control;
	}

	public JMenu getMenu(MenuBuilder menuBuilder) {
		MenuConfigurator config = menuBuilder.getMenu("basin_of_attraction");
		config.addExecute("BasinPlugin.menu.basic", "showBOAConfiguration");
		config.addExecute("BasinPlugin.menu.average", "boa.showAverageSamples");
		config.addExecute("BasinPlugin.menu.execute", "boa.executeBasin");
		return config.getJMenu();
	}

	public IterationListener getIterationListener() {
		return null;
	}

	public void activate() {
		activate(new Basin());
	}

	public void deactivate() {
	}

	public Object getSerializedData() {
		return basin;
	}

	public void activate(Object data) {
		this.basin = (Basin) data;
		this.controller = new BasinController(windows, basin);
		control.registerDI(basin);
		control.registerDI(controller);
	}

	public Tool[] getTools() {
		return new Tool[0];
	}

}
