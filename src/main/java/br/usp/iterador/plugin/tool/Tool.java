package br.usp.iterador.plugin.tool;

import javax.swing.Icon;

import br.usp.iterador.logic.Controller;

/**
 * A plugin tool to be displayed in the toolbar
 * 
 * @author Guilherme Silveira
 */
public interface Tool {

	String getName();

	void activate(Controller controller);

	void deactivate();

	void onClick(int x, int y);

	/**
	 * The mouse has moved
	 * 
	 * @param x
	 *            x pos
	 * @param y
	 *            y pos
	 */
	void mouseMoved(int x, int y);
	
	/**
	 * Returns its icon, if it has any
	 * @return	the icon
	 */
	Icon getIcon();

}
