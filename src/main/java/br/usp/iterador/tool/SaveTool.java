package br.usp.iterador.tool;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import br.usp.iterador.logic.Controller;
import br.usp.iterador.plugin.tool.Tool;

/**
 * Saves a file.
 * 
 * @author Guilherme Silveira
 */
public class SaveTool implements Tool {

	public String getName() {
		return "save";
	}

	public String getTooltip() {
		return "save_tooltip";
	}

	public void activate(Controller controller) {
		controller.executeAction("save");
		controller.executeAction("ResetTool");
	}

	public void deactivate() {
	}

	public void onClick(int pointX, int pointY) {
	}

	/**
	 * Does nothing
	 * 
	 * @see br.usp.iterador.plugin.tool.Tool#mouseMoved(int, int)
	 */
	public void mouseMoved(int x, int y) {
	}

	/**
	 * Returns its icon
	 * 
	 * @see br.usp.iterador.plugin.tool.Tool#getIcon()
	 */
	public Icon getIcon() {
		return new ImageIcon(SaveTool.class.getResource("Save16.gif"));
	}

}
