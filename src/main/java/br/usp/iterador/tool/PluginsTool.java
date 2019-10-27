package br.usp.iterador.tool;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import br.usp.iterador.logic.Controller;
import br.usp.iterador.plugin.tool.Tool;

/**
 * Selects the plugins.
 * 
 * @author Guilherme Silveira
 */
public class PluginsTool implements Tool {

	public String getName() {
		return "plugins";
	}

	public void activate(Controller controller) {
		controller.executeAction("showPluginFrame");
	}

	public void deactivate() {
	}

	public void onClick(int pointX, int pointY) {
	}

	public void mouseMoved(int x, int y) {
	}

	public Icon getIcon() {
		return new ImageIcon(PluginsTool.class.getResource("Preferences16.gif"));
	}

}
