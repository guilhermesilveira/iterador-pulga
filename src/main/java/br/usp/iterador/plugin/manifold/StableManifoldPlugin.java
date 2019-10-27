package br.usp.iterador.plugin.manifold;

import java.io.Serializable;

import javax.swing.JMenu;

import br.usp.iterador.gui.menu.MenuBuilder;
import br.usp.iterador.logic.Controller;
import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.plugin.Plugin;
import br.usp.iterador.plugin.tool.Tool;

/**
 * Stable manifold plugin.
 * 
 * @author Guilherme Silveira
 */
public class StableManifoldPlugin implements Plugin {

	private Controller controller;

	private StableManifoldData data;

	public StableManifoldPlugin(Controller controller) {
		this.controller = controller;
	}

	public JMenu getMenu(MenuBuilder menuBuilder) {
		return null;
	}

	public IterationListener getIterationListener() {
		return null;
	}

	public void activate() {
		activate(null);
	}

	public void deactivate() {
		controller.unregisterIOC(this.data);
	}

	public Serializable getSerializedData() {
		return null;
	}

	public void activate(Object data) {
		this.data = new StableManifoldData();
		controller.registerDI(this.data);
		controller.registerAction("StableManifoldExecution",
				StableManifoldExecution.class);
	}

	public Tool[] getTools() {
		return new Tool[] { new StableManifoldTool(data) };
	}

}
