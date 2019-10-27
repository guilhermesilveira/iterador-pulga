package br.usp.iterador.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import br.usp.iterador.gui.util.GuiFactory;
import br.usp.iterador.i18n.Messages;
import br.usp.iterador.logic.Controller;
import br.usp.iterador.plugin.PluginManager;
import br.usp.iterador.plugin.tool.Tool;
import br.usp.iterador.plugin.tool.ToolManager;
import br.usp.iterador.tool.ClearTool;
import br.usp.iterador.tool.LoadTool;
import br.usp.iterador.tool.PluginsTool;
import br.usp.iterador.tool.RedrawTool;
import br.usp.iterador.tool.SaveTool;
import br.usp.iterador.tool.ShowPointTool;

/**
 * Toolbar builder.
 *
 * @author Guilherme Silveira
 */
public class ToolBarBuilder {

	private final JToolBar toolBar = new JToolBar();

	private final PluginManager pluginManager;

	private final ToolManager toolManager;

	private final GuiFactory guiFactory;

	private final Controller controller;

	public ToolBarBuilder(PluginManager pluginManager, GuiFactory guiFactory,
			Controller controller, ToolManager toolManager) {
		this.guiFactory = guiFactory;
		this.pluginManager = pluginManager;
		this.controller = controller;
		this.toolManager = toolManager;
	}

	/**
	 * Rebuilds the toolbar
	 *
	 * @return the toolbar
	 */
	public JToolBar buildToolBar() {
		toolBar.removeAll();
		for (Tool tool : getBasicTools()) {
			toolBar.add(newBarButton(tool));
		}
		for (Tool tool : pluginManager.getTools()) {
			toolBar.add(newBarButton(tool));
		}
		return toolBar;
	}

	/**
	 * Returns the basic tools
	 *
	 * @return basic tools
	 */
	private Tool[] getBasicTools() {
		return new Tool[] { new LoadTool(),
				new SaveTool(), new ClearTool(), new RedrawTool(),
				instantiate(ShowPointTool.class), new PluginsTool() };
	}

	private Tool instantiate(Class<? extends Tool> type) {
		return controller.newInstance(type);
	}

	/**
	 * Createsa a bar button based on this tool
	 *
	 * @param tool
	 *            tool
	 * @return button
	 */
	private JButton newBarButton(final Tool tool) {
		JButton button = null;
		if (tool.getIcon() != null) {
			button = guiFactory.getButton(tool.getIcon());
		} else {
			button = guiFactory.getButton(tool.getName());
		}
		button.setToolTipText(Messages.getString(tool.getName() + "_tooltip"));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toolManager.activateTool(tool);
			}
		});
		return button;
	}

}
