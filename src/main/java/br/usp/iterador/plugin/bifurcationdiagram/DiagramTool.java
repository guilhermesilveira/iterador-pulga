package br.usp.iterador.plugin.bifurcationdiagram;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import br.usp.iterador.logic.Controller;
import br.usp.iterador.plugin.tool.Tool;

/**
 * Diagram's tool.
 * 
 * @author Guilherme Silveira
 */
public class DiagramTool implements Tool {

	private Controller controller;

	public void activate(Controller controller) {
		this.controller = controller;
		controller.registerAction("DiagramDrawer", DiagramDrawer.class);
		controller.executeAction("DiagramDrawer");
		controller.executeAction("ResetTool");
	}

	public void deactivate() {
		controller.unregisterAction("DiagramDrawer");
	}

	public String getName() {
		return "bifurcation-diagram-tool";
	}

	public String getTooltip() {
		return "bifurcation-diagram-tool.tooltip";
	}

	public void mouseMoved(int x, int y) {
	}

	public void onClick(int x, int y) {
	}

	public Icon getIcon() {
		return new ImageIcon(DiagramTool.class.getResource("bifurcation.gif"));
	}

}
