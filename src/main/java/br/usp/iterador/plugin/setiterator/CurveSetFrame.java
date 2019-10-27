package br.usp.iterador.plugin.setiterator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import br.usp.iterador.gui.SimpleIteratorFrame;
import br.usp.iterador.gui.util.CloseButton;
import br.usp.iterador.gui.util.DataModel;
import br.usp.iterador.gui.util.ExecuteButton;
import br.usp.iterador.gui.util.GuiFactory;
import br.usp.iterador.logic.Controller;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.model.Application;
import br.usp.iterador.plugin.PluginManager;
import br.usp.iterador.plugin.gui.PulgaFrame;
import br.usp.iterador.plugin.gui.WindowManager;
import br.usp.iterador.task.LongTaskManager;

/**
 * Initial condition set.
 * 
 * @author Guilherme Silveira
 */
public class CurveSetFrame implements PulgaAction {

	private static final Logger logger = Logger.getLogger(CurveSetFrame.class);

	private PulgaFrame frame;

	private final PluginManager pluginManager;

	private final GuiFactory gui = new GuiFactory();

	private final Application app;

    private final WindowManager windows;

    private final SimpleIteratorFrame iteratorFrame;

    private final Controller controller;

	public CurveSetFrame(PluginManager manager, WindowManager windows,
			Application app,Controller controller, 
            SimpleIteratorFrame iteratorFrame) {
		this.pluginManager = manager;
        this.windows = windows;
        this.iteratorFrame = iteratorFrame;
        this.controller = controller;
		this.app = app;
		PulgaFrame frame = windows.showFrame("initial_curve_set",
				"initial_curve_set", createContentPane(), 400, 100);
		frame.setVisible(true);
		this.frame = frame;
	}

	private Container createContentPane() {
		Container container = new JPanel(new BorderLayout());
		container.setLayout(new BorderLayout());
		container.add(getMiddle(), BorderLayout.CENTER);
		container.add(getButtons(), BorderLayout.SOUTH);
		return container;
	}

	public void newCurve() {
		CurveSet set = pluginManager.getActivePlugin(InitialCurvePlugin.class)
				.getData();
		set.addCurve(new Curve());
		frame.rebuild(createContentPane());
	}

	public void execute() {
		LongTaskManager manager = new LongTaskManager(windows,
				new CurveSetExecutor(pluginManager, app, controller),
				iteratorFrame);
		manager.start();
	}

	private Component getButtons() {
		return gui.getPanel(new ExecuteButton("new_curve", this, "newCurve"),
				new ExecuteButton("execute", this, "execute"),
				gui.createCloseButton());
	}

	private JPanel getMiddle() {

		List<Curve> curves = pluginManager.getActivePlugin(
				InitialCurvePlugin.class).getData().getCurves();

		int dim = app.getDimension();
		JPanel panel = new JPanel(new GridLayout(curves.size() + 1, dim));
		for (int i = 1; i <= dim; i++) {
			panel.add(new JLabel("x" + (i)));
		}

		logger.debug("creating " + curves.size() + "lines");

		DataModel dataModel = new DataModel(curves);
		for (Curve curve : curves) {
			for (int i = 0; i < dim; i++) {
				CurveField field = new CurveField(curve, i);
				logger.debug("creating curve " + curve + " expression " + i);
				dataModel.addModel(field);
				panel.add(field.getComponent());
			}
		}

		return panel;

	}

}
