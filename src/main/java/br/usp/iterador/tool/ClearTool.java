package br.usp.iterador.tool;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import br.usp.iterador.logic.Controller;
import br.usp.iterador.plugin.tool.Tool;

/**
 * Clears the window.
 * 
 * @author Guilherme Silveira
 */
public class ClearTool implements Tool {

	public String getName() {
		return "clear";
	}

	public String getTooltip() {
		return "clear_tooltip";
	}

	public void activate(Controller controller) {
		controller.executeAction("clear");
		controller.executeAction("ResetTool");
	}

	public void deactivate() {
	}

	public void onClick(int pointX, int pointY) {
	}

	public void mouseMoved(int x, int y) {
	}

	public Icon getIcon() {
		return new ImageIcon(ClearTool.class.getResource("clear.gif"));
	}

}
