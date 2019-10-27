package br.usp.iterador.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import br.usp.iterador.gui.util.DataModel;
import br.usp.iterador.gui.util.GuiFactory;
import br.usp.iterador.gui.util.ModelComboField;
import br.usp.iterador.gui.util.ModelField;
import br.usp.iterador.gui.util.ModelIntField;
import br.usp.iterador.gui.util.MultiPaneCreator;
import br.usp.iterador.gui.util.ScaleField;
import br.usp.iterador.logic.Controller;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.model.Application;
import br.usp.iterador.plugin.gui.PulgaFrame;
import br.usp.iterador.plugin.gui.WindowManager;

/**
 * Controls the iterations and appearance.
 *
 * @author Guilherme Silveira
 */
public class ShowIterationsFrame implements PulgaAction {

	private ModelIntField iteratedPoints = new ModelIntField("iteratedPoints"),
			trashPoints = new ModelIntField("trashPoints"),
			iterationPower = new ModelIntField("iterationPower");

	private ModelComboField fields[];

	private ScaleField x = new ScaleField("xScale"), y = new ScaleField(
			"yScale");

	private Application app;

	private GuiFactory guiFactory = new GuiFactory();

	private Controller controller;

	public ShowIterationsFrame(Application app, Controller controller) {
		this.app = app;
		this.controller = controller;
		fields = new ModelComboField[] {
				new ModelComboField(app, app.getXScale()),
				new ModelComboField(app, app.getYScale()) };
	}

	public void execute(Controller controller, WindowManager windows) {

		PulgaFrame frame = windows.showFrame("iterations", "iterations",
				getMainContent(), 350, 300);

		DataModel model = new DataModel(app);

		model.addModel(this.iterationPower, this.iteratedPoints,
				this.trashPoints, this.fields[0], this.fields[1]);
		model.addModel((ModelField[]) this.x.getFields());
		model.addModel((ModelField[]) this.y.getFields());

		model.addActionListener(guiFactory.getLogicListener("basic/updateSize",
				controller));

		frame.setVisible(true);

	}

	private JPanel getPanelX() {
		JPanel panel = new JPanel(new GridLayout(1, 3));
		panel.add(fields[0].getComponent());
		panel.add(x.getFields()[0].getComponent());
		panel.add(x.getFields()[1].getComponent());
		return panel;
	}

	private JPanel getPanelY() {
		JPanel panel = new JPanel(new GridLayout(1, 3));
		panel.add(fields[1].getComponent());
		panel.add(y.getFields()[0].getComponent());
		panel.add(y.getFields()[1].getComponent());
		return panel;
	}

	private JPanel getContent() {
		JPanel pane = new JPanel(new BorderLayout());
		pane.add(getMiddle(), BorderLayout.CENTER);
		pane.add(guiFactory.createCloseButton(), BorderLayout.SOUTH);
		return pane;
	}

	private JPanel getMiddle() {
		JPanel jPanel = new JPanel(new GridLayout(10, 1));

		jPanel.add(guiFactory.getLabel("scale.x"));
		jPanel.add(getPanelX());
		jPanel.add(guiFactory.getLabel("scale.y"));
		jPanel.add(getPanelY());
		jPanel.add(guiFactory.getLabel("trash_points"));
		jPanel.add(this.trashPoints.getComponent());
		jPanel.add(guiFactory.getLabel("iterated_points"));
		jPanel.add(this.iteratedPoints.getComponent());
		jPanel.add(guiFactory.getLabel("iteration_power"));
		jPanel.add(this.iterationPower.getComponent());

		return jPanel;
	}

	private JScrollPane getMainContent() {
		return new MultiPaneCreator().createPane(getContent(),
				new AppearancePaneCreator(controller, guiFactory)
						.getContent(app));
	}

}
