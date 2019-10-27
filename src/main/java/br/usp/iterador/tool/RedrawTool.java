package br.usp.iterador.tool;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import br.usp.iterador.logic.Controller;
import br.usp.iterador.plugin.tool.Tool;

/**
 * Redraws the image.
 * 
 * @author Guilherme Silveira
 */
public class RedrawTool implements Tool {

	public String getName() {
		return "redraw";
	}

	public String getTooltip() {
		return "redraw_tooltip";
	}

	public void activate(Controller controller) {
		controller.executeAction("redraw");
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
		return new ImageIcon(RedrawTool.class.getResource("Refresh16.gif"));
	}

}
