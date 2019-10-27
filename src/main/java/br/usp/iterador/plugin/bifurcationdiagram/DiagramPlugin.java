package br.usp.iterador.plugin.bifurcationdiagram;

import java.io.Serializable;

import javax.swing.JMenu;

import br.usp.iterador.gui.menu.MenuBuilder;
import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.plugin.Plugin;
import br.usp.iterador.plugin.tool.Tool;

/**
 * Bifurcation's diagram plugin. Used to generate bifurcation's diagram by
 * selecting the field and iterating over the x axis (on its viewport).
 * 
 * @author Guilherme Silveira
 */
public class DiagramPlugin implements Plugin {

	public JMenu getMenu(MenuBuilder menuBuilder) {
		return null;
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
		return new Tool[] { new DiagramTool() };
	}

}
