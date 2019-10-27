package br.usp.iterador.logic.system;

import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.model.Application;
import br.usp.iterador.plugin.tool.ToolManager;
import br.usp.iterador.tool.ShowPointTool;

public class ResetTool implements PulgaAction {

	public void execute(Application application, ToolManager toolManager) {
		toolManager.activateTool(new ShowPointTool(application));
	}

}
