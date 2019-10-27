package br.usp.iterador.plugin.mandelbrot;

import javax.swing.JMenu;

import br.usp.iterador.gui.menu.MenuBuilder;
import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.plugin.Plugin;
import br.usp.iterador.plugin.tool.Tool;

/**
 * Mandelbrot plugin
 * 
 * @author Guilherme Silveira
 */
public class MandelbrotPlugin implements Plugin {

	public void activate() {
	}

	public void deactivate() {
	}

	/**
	 * Exits when reach INFINITY
	 * 
	 * @see br.usp.iterador.plugin.Plugin#getIterationCode()
	 */
	public String getIterationCode() {
		return "\tif(x1==Double.POSITIVE_INFINITY || x2==Double.POSITIVE_INFINITY || x1==Double.NaN || x2==Double.NaN) return false;";
	}

	public IterationListener getIterationListener() {
		return null;
	}

	public JMenu getMenu(MenuBuilder menuBuilder) {
		return null;
	}

	public Object getSerializedData() {
		return null;
	}

	public Tool[] getTools() {
		return new Tool[0];
	}

	public void activate(Object s) {
	}

}
