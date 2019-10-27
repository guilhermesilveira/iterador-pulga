package br.usp.iterador.plugin;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JPanel;

import br.usp.iterador.gui.IteratorFrame;
import br.usp.iterador.gui.util.GuiFactory;
import br.usp.iterador.logic.Controller;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.plugin.gui.WindowManager;

/**
 * Plugin manager: activation/deactivation.
 * 
 * @author Guilherme Silveira
 */
public class ShowPluginManager implements PulgaAction {

	private final IteratorFrame frame;

	private final PluginManager manager;

	private final GuiFactory guiFactory;

	private final Controller controller;

	public ShowPluginManager(IteratorFrame frame, PluginManager manager,
			GuiFactory factory, Controller controller) {
		this.frame = frame;
		this.manager = manager;
		this.guiFactory = factory;
		this.controller = controller;
	}

	public void execute(WindowManager windows) {
		Container container = new JPanel();
		container.setLayout(new BorderLayout());
		container.add(getMiddle(guiFactory, manager), BorderLayout.CENTER);
		container.add(getButtons(guiFactory), BorderLayout.SOUTH);
		windows.showFrame("pluginmanager", "pluginmanager.title", container,
				500, 300);
		controller.executeAction("ResetTool");
	}

	private Component getMiddle(GuiFactory guiFactory, PluginManager manager) {
		return guiFactory.createPane(guiFactory
				.createTable(new PluginTableModel(manager, frame)));
	}

	private JPanel getButtons(GuiFactory guiFactory) {
		return guiFactory.getPanel(guiFactory.createCloseButton());
	}

}
