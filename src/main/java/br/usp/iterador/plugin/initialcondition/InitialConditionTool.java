package br.usp.iterador.plugin.initialcondition;

import javax.swing.Icon;

import org.apache.log4j.Logger;

import br.usp.iterador.gui.GUIHelper;
import br.usp.iterador.logic.Controller;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.ImageInfo;
import br.usp.iterador.model.Intermediate;
import br.usp.iterador.model.Parameter;
import br.usp.iterador.plugin.tool.Tool;

/**
 * Initial condition mouse listener.
 * 
 * @author Guilherme Silveira
 */
public class InitialConditionTool implements Tool {

	private static Logger logger = Logger.getLogger(InitialConditionTool.class);

	private Application app;

	private Controller controller;

	public String getName() {
		return "initial_condition";
	}

	public String getTooltip() {
		return "initial_condition_tooltip";
	}

	public void activate(Controller controller) {
		this.controller = controller;
		this.app = controller.find(Application.class);
	}

	public void deactivate() {
	}

	public void onClick(int x, int y) {
		ImageInfo info = app.getImage();
		int width = info.getWidth();
		int height = info.getHeight();

		// reescala para os valores ok
		GUIHelper helper = new GUIHelper(width, height);
		double xn, yn;
		xn = helper.mudaEscala(x, 0, width, app.getXScale());
		yn = helper.mudaEscala(height - y, 0, height, app.getYScale());
		logger.info("initial condition plugin: " + xn + ", " + yn + " from "
				+ x + "," + y);

		// seta os valores
		changeValue(app, app.getXScale().getField(), xn);
		changeValue(app, app.getYScale().getField(), yn);

		// chama o redraw
		controller.executeAction("redraw");
	}

	public void mouseMoved(int x, int y) {
	}

	public static void changeValue(Application app, String field, double xn) {
		for (Parameter parameter : app.getParameters()) {
			if (parameter.getName().equals(field)) {
				parameter.setValue("" + xn);
				return;
			}
		}
		for (Intermediate intermediario : app.getIntermediateExpressions()) {
			if (intermediario.getName().equals(field)) {
				// will not change it
				logger
						.error("The plugin will not change any intermediate value! Please use a parameter or an iteration variable.");
				return;
			}
		}
		if (field.startsWith("x")) {
			try {
				int i = Integer.parseInt(field.substring(1));
				app.getVariables().get(i - 1).setInitialValue(xn);
				return;
			} catch (NumberFormatException e) {
				logger.debug(e.getMessage(), e);
			}
		}
		logger.error("Unable to find what should be changed: " + field);
	}

	/**
	 * Returns its icon
	 * 
	 * @see br.usp.iterador.plugin.tool.Tool#getIcon()
	 */
	public Icon getIcon() {
		return null;
	}

}
