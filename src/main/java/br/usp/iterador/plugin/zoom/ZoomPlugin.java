package br.usp.iterador.plugin.zoom;

import java.io.Serializable;

import javax.swing.JMenu;

import br.usp.iterador.gui.menu.MenuBuilder;
import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.plugin.Plugin;
import br.usp.iterador.plugin.tool.Tool;

/**
 * Zoom plugin.
 * 
 * @author Guilherme Silveira
 */
public class ZoomPlugin implements Plugin {
	
	public JMenu getMenu(MenuBuilder menuBuilder) {
		return menuBuilder.getMenu("zoom").getJMenu();
	}

	public IterationListener getIterationListener() {
		return null;
	}

	public void activate() {
	}

	public void deactivate() {
	}

	public Serializable getSerializedData() {
		return null;
	}

	public void activate(Object data) {
	}

	public Tool[] getTools() {
		return new Tool[] { new ZoomTool() };
	}

}
