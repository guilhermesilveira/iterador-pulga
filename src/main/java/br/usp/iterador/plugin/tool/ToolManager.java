package br.usp.iterador.plugin.tool;

import java.util.List;

import org.apache.log4j.Logger;

import br.usp.iterador.logic.Controller;
import br.usp.iterador.plugin.Plugin;

/**
 * Tool manager.
 *
 * @author Guilherme Silveira
 */
public class ToolManager {

	private static final Logger LOG = Logger.getLogger(ToolManager.class);

    private final ToolRepository repository;

	private final Controller controller;

	private Tool currentTool;

    public ToolManager(Controller controller, ToolRepository repository) {
		this.controller = controller;
        this.repository = repository;
    }

	public void activateTool(Tool tool) {
		LOG.debug("Activating tool " + tool.getName());
		if (currentTool != null) {
			currentTool.deactivate();
		}
		currentTool = tool;
		tool.activate(controller);
	}

	public Tool getCurrentTool() {
		return currentTool;
	}

	public void installTools(Plugin plugin) {
		this.repository.installTools(plugin);
	}

	public void uninstallTools(Plugin plugin) {
		Tool[] tools = this.repository.uninstallTools(plugin);
		for (Tool t : tools) {
			if (currentTool.equals(t)) {
				controller.executeAction("ResetTool");
				break;
			}
		}
	}

	public List<Tool> getTools() {
		return this.repository.getTools();
	}

}
