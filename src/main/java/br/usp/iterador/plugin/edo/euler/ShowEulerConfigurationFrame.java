package br.usp.iterador.plugin.edo.euler;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import br.usp.iterador.gui.util.DataModel;
import br.usp.iterador.gui.util.ExecuteButton;
import br.usp.iterador.gui.util.GuiFactory;
import br.usp.iterador.gui.util.ModelDoubleField;
import br.usp.iterador.gui.util.ModelTextArea;
import br.usp.iterador.gui.util.PulgaLabel;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.Intermediate;
import br.usp.iterador.model.Parameter;
import br.usp.iterador.plugin.gui.PulgaFrame;
import br.usp.iterador.plugin.gui.WindowManager;

public class ShowEulerConfigurationFrame implements PulgaAction {

	private final PulgaFrame frame;

	private final EulerData data;

	private final GuiFactory gui = new GuiFactory();

	private final Application app;

	public ShowEulerConfigurationFrame(EulerData data, Application app,
			WindowManager windows) {
		this.app = app;
		this.data = data;
		frame = windows.showFrame("euler_solver", "euler_solver",
				createMainPane(), 300, 150);
	}

	public void execute() {
		frame.setVisible(true);
	}

	private Container createMainPane() {
		JPanel panel = new JPanel(new BorderLayout());
		ExecuteButton execute = new ExecuteButton(
				"change_iteration_to_euler_method", this,
				"applyEulerMethodToIteration");
		panel.add(createPane());
		panel.add(gui.getPanel(execute, gui.createCloseButton()),
				BorderLayout.SOUTH);
		return panel;
	}

	private JPanel createPane() {

		ModelTextArea f = new ModelTextArea("f");
		ModelTextArea known = new ModelTextArea("knownSolution");
		ModelDoubleField t0 = new ModelDoubleField("t0");
		ModelDoubleField t1 = new ModelDoubleField("t1");

		new DataModel(data, f, known, t0, t1);

		JPanel panel = new JPanel(new GridLayout(4, 2));
		panel.add(GuiFactory.decorate(new PulgaLabel("f")));
		panel.add(f.getComponent());
		// panel.add(new PulgaLabel("knownSolution"));
		// panel.add(known.getComponent());
		panel.add(GuiFactory.decorate(new PulgaLabel("t0")));
		panel.add(t0.getComponent());
		panel.add(GuiFactory.decorate(new PulgaLabel("t1")));
		panel.add(t1.getComponent());
		return panel;

	}

	public void applyEulerMethodToIteration() {
		double h = (this.data.getT1() - this.data.getT0())
				/ app.getIteratedPoints();
		removeParameters(app.getParameters(), "h");
		String valH = String.format("%.10f", h);
		app.getParameters().add(new Parameter("h", valH, valH, valH));
		remove(app.getIntermediateExpressions(), "_euler");
		app.getIntermediateExpressions().add(
				new Intermediate("double[]", "_euler",
						"_euler = new double[] { \n" + this.data.getF()
								+ "\n};"));
		remove(app.getIntermediateExpressions(), "t");
		app.getIntermediateExpressions().add(
				new Intermediate("double", "t", "t + h"));
		for (int i = 0; i < app.getDimension(); i++) {
			app.getVariables().get(i).setCode(
					"x" + (i + 1) + " + h * _euler[" + i + "]");
		}
	}

	private void removeParameters(List<Parameter> parameters, String field) {
		for (Iterator it = parameters.iterator(); it.hasNext();) {
			Parameter param = (Parameter) it.next();
			if (param.getName().equals(field)) {
				it.remove();
				return;
			}
		}
	}

	private void remove(List<Intermediate> intermediarios, String title) {
		for (Iterator it = intermediarios.iterator(); it.hasNext();) {
			Intermediate i = (Intermediate) it.next();
			if (i.getName().equals(title)) {
				it.remove();
				return;
			}
		}
	}

}
