package br.usp.iterador.plugin.initialcondition;

import java.io.Serializable;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JPanel;

import br.usp.iterador.gui.FormulaPane;
import br.usp.iterador.gui.PanelBuilder;
import br.usp.iterador.gui.menu.MenuBuilder;
import br.usp.iterador.gui.util.DataModel;
import br.usp.iterador.io.SimpleProperties;
import br.usp.iterador.jxpath.JXPathException;
import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.plugin.Plugin;
import br.usp.iterador.plugin.tool.Tool;

/**
 * Initial condition plugin.
 * 
 * @author Guilherme Silveira
 */
public class InitialConditionPlugin implements Plugin {

	private PanelBuilder panelBuilder;

	public InitialConditionPlugin(PanelBuilder panelBuilder) {
		this.panelBuilder = panelBuilder;
	}

	public JMenu getMenu(MenuBuilder menuBuilder) {
		return null;
	}

	public IterationListener getIterationListener() {
		return null;
	}

	public void activate() {
	}

	public void deactivate() {
	}

	public Serializable getSerializedData() {
		return null;
	}

	public void activate(Object data) {
	}

	public Tool[] getTools() {
		return new Tool[] { new InitialConditionTool() };
	}

	@FormulaPane
	public JPanel createPane(List<DataModel> models) throws JXPathException {
		SimpleProperties prop = new SimpleProperties(
				"/initialConditionPlugin.frame");
		return panelBuilder.build(prop, models);
	}

}
