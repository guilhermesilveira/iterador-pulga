package br.usp.iterador.tool;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import br.usp.iterador.logic.Controller;
import br.usp.iterador.plugin.tool.Tool;

/**
 * Loads a file.
 * 
 * @author Guilherme Silveira
 */
public class LoadTool implements Tool {

	public String getName() {
		return "load";
	}

	public String getTooltip() {
		return "load_tooltip";
	}

	public void activate(Controller controller) {
		controller.executeAction("load");
		controller.executeAction("ResetTool");
	}

	public void deactivate() {
	}

	public void onClick(int pointX, int pointY) {
	}

	public void mouseMoved(int x, int y) {
	}

	public Icon getIcon() {
		return new ImageIcon(LoadTool.class.getResource("Open16.gif"));
	}

}
