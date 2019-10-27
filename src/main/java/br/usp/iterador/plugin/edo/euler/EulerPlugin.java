package br.usp.iterador.plugin.edo.euler;

import javax.swing.JMenu;

import br.usp.iterador.gui.menu.MenuBuilder;
import br.usp.iterador.logic.Controller;
import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.plugin.Plugin;
import br.usp.iterador.plugin.tool.Tool;

/**
 * Creates the edo iteration for solving some functions.
 * 
 * @author Guilherme Silveira
 */
public class EulerPlugin implements Plugin {

	private EulerData data;

	private Controller controller;

	public EulerPlugin(Controller controller) {
		this.controller = controller;
	}

	public JMenu getMenu(MenuBuilder menuBuilder) {
		return menuBuilder.getMenu("euler_solver").addExecute("config",
				"ShowEulerFrame").getJMenu();
	}

	public IterationListener getIterationListener() {
		return null;
	}

	public void activate() {
		activate(new EulerData());
	}

	public void deactivate() {
		controller.unregisterIOC(data);
	}

	public Object getSerializedData() {
		return this.data;
	}

	public void activate(Object data) {
		this.data = (EulerData) data;
		controller.registerDI(data);
		controller
				.registerAction("ShowEulerFrame", ShowEulerConfigurationFrame.class);
	}

	public Tool[] getTools() {
		return new Tool[0];
	}

}
