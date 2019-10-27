package br.usp.iterador.plugin.setiterator;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import net.janino.ClassBodyEvaluator;
import net.janino.Scanner;

import org.apache.log4j.Logger;

import br.usp.iterador.logic.Controller;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.Parameter;
import br.usp.iterador.plugin.PluginManager;
import br.usp.iterador.task.LongTask;

public class CurveSetExecutor implements LongTask {

	private static final Logger logger = Logger
			.getLogger(CurveSetExecutor.class);

	private int totalPieces;

	private int complete = 0;

	private PluginManager pluginManager;

	private Application app;

	private Controller controller;

	public CurveSetExecutor(PluginManager pluginManager, Application app,
			Controller controller) {
		super();
		this.pluginManager = pluginManager;
		this.app = app;
		this.controller = controller;
	}

	public void run() {

		List<Curve> curves = pluginManager.getActivePlugin(
				InitialCurvePlugin.class).getData().getCurves();

		int pieces = 1000;
		this.totalPieces = pieces * curves.size();
		boolean first = true;

		for (Curve curve : curves) {

			int dim = app.getDimension();

			String code = "";

			for (Parameter p : app.getParameters()) {
				code += String.format(Locale.ENGLISH, "\tdouble %s = %s;\n", p
						.getName(), p.getValue());
			}
			code += "\n\n";

			code += "public double[] getValueAt(double t) {\n";
			code += "\treturn new double[]{\n";
			for (int i = 0; i < dim; i++) {
				code += "\t\t";
				if (i != 0)
					code += ",";
				code += "(" + curve.getExpression(i) + ")\n";
			}
			code += "\t\t};\n}";

			logger.info("Going to compile\n" + code);

			try {

				Scanner sc = new Scanner(null, new StringReader(code));
				Class clazz = new ClassBodyEvaluator(sc, null,
						new Class[] { CurveClass.class }, null).evaluate();
				CurveClass curveEvaluator = (CurveClass) clazz.newInstance();
				double val = 0;

				double piece = 1.0 / pieces;
				for (int i = 0; i <= pieces; i++) {
					executeFor(curveEvaluator, val, first);
					val += piece;
					first = false;
					complete++;
				}

			} catch (Exception e) {
				logger.error("Impossible to run for curve " + curve, e);
			}

		}

	}

	private void executeFor(CurveClass curveEvaluator, double t, boolean first) {

		double pos[] = curveEvaluator.getValueAt(t);
		logger.debug("Beginning with " + Arrays.toString(pos));
		for (int i = 0; i < pos.length; i++) {
			app.getVariables().get(i).setInitialValue(pos[i]);
		}

		// seta o intermediario clear para false
		boolean old = app.isClearBeforeDrawing();

		// if not the first, changes to not clear
		if (!first) {
			app.setClearBeforeDrawing(false);
		}

		// chama o redraw
		controller.executeAction("redraw");
		app.setClearBeforeDrawing(old);

	}

	public String getName() {
		return "initial_curves";
	}

	public double getPercentageComplete() {
		return (100.0 * complete) / totalPieces;
	}

}
