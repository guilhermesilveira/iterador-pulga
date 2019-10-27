package br.usp.iterador.plugin.connectpoints;

import javax.swing.JMenu;

import br.usp.iterador.gui.menu.MenuBuilder;
import br.usp.iterador.gui.menu.MenuConfigurator;
import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.model.Application;
import br.usp.iterador.plugin.Plugin;
import br.usp.iterador.plugin.tool.Tool;

/**
 * Connects two consecutive points plugin.
 * 
 * @author Guilherme Silveira
 */
public class ConnectPointsPlugin implements Plugin {

	private ConnectPointsData data = new ConnectPointsData();

	private Application app;

	public ConnectPointsPlugin(Application app) {
		this.app = app;
	}

	public JMenu getMenu(MenuBuilder menuBuilder) {
		MenuConfigurator menu = menuBuilder.getMenu("connect_points");
		menu.add("config", ShowConnectConfig.class);
		return menu.getJMenu();
	}

	public IterationListener getIterationListener() {
		return new ConnectPointsIterationListener(app, data);
	}

	public void activate() {

	}

	public void deactivate() {

	}

	public Object getSerializedData() {
		return this.data;
	}

	public void activate(Object data) {
		this.data = (ConnectPointsData) data;
	}

	public Tool[] getTools() {
		return new Tool[0];
	}

	public ConnectPointsData getData() {
		return data;
	}

}
