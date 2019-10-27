package br.usp.iterador.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;

import br.usp.iterador.gui.util.DataModel;
import br.usp.iterador.gui.util.GuiFactory;
import br.usp.iterador.gui.util.ModelCheckbox;
import br.usp.iterador.gui.util.ModelColorField;
import br.usp.iterador.gui.util.ModelIntField;
import br.usp.iterador.logic.Controller;
import br.usp.iterador.model.Application;

/**
 * Controls the grid and scale options.
 *
 * @author Guilherme Silveira
 */
public class AppearancePaneCreator {

	private final Controller controller;

	private final GuiFactory guiFactory;

	public AppearancePaneCreator(Controller controller, GuiFactory guiFactory) {
		this.controller = controller;
		this.guiFactory = guiFactory;
	}

	public JPanel getContent(Application app) {

		ModelIntField width = new ModelIntField("image.width"), height = new ModelIntField(
				"image.height");

		ModelColorField backgroundColor = new ModelColorField("backgroundColor"), gridColor = new ModelColorField(
				"grid.color");

		ModelCheckbox showGrid = new ModelCheckbox("show_grid", "grid.on"), clear = new ModelCheckbox(
				"clear_before_drawing", "clearBeforeDrawing");

        ModelCheckbox showAxis = new ModelCheckbox("showAxis", "grid.showAxis");

		DataModel dataModel = new DataModel(app, backgroundColor, gridColor,
				width, height, showGrid, clear, showAxis);

		dataModel.addActionListener(guiFactory.getLogicListener(
				"basic/updateSize", controller));

		JPanel jPanel = new JPanel(new GridLayout(11, 1));
		jPanel.add(clear.getComponent());
		jPanel.add(showGrid.getComponent());
        jPanel.add(showAxis.getComponent());
		jPanel.add(guiFactory.getLabel("grid_color"));
		jPanel.add(gridColor.getComponent());
		jPanel.add(guiFactory.getLabel("background_color"));
		jPanel.add(backgroundColor.getComponent());
		jPanel.add(guiFactory.getLabel("width"));
		jPanel.add(width.getComponent());
		jPanel.add(guiFactory.getLabel("height"));
		jPanel.add(height.getComponent());
		return jPanel;
	}
}
