package br.usp.iterador.plugin.setiterator;

import javax.swing.JMenu;

import br.usp.iterador.gui.menu.MenuBuilder;
import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.plugin.Plugin;
import br.usp.iterador.plugin.tool.Tool;

/**
 * Initial curve plugin.
 * 
 * @author Guilherme Silveira
 */
public class InitialCurvePlugin implements Plugin {

	private CurveSet data = new CurveSet();

	public JMenu getMenu(MenuBuilder menuBuilder) {
		return menuBuilder.getMenu("initial_curve_set").add(
				"initial_curve_set", CurveSetFrame.class).getJMenu();
	}

	public IterationListener getIterationListener() {
		return null;
	}

	public void activate() {
	}

	public void deactivate() {
	}

	public Object getSerializedData() {
		return this.data;
	}

	public void activate(Object data) {
		this.data = (CurveSet) data;
	}

	public Tool[] getTools() {
		return new Tool[0];
	}

	public CurveSet getData() {
		return data;
	}

}
