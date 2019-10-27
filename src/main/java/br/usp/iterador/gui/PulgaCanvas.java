package br.usp.iterador.gui;

import br.usp.iterador.gui.util.PulgaRectangle;
import br.usp.iterador.internal.logic.CompileTimeException;
import br.usp.iterador.plugin.PluginManager;

public interface PulgaCanvas {

	/**
	 * Adds a shape
	 * 
	 * @param rectangle
	 *            shape
	 */
	void addShape(PulgaRectangle rectangle);

	void changeSize(int w, int h);

	/**
	 * Clears the background image and asks for repaint
	 * 
	 */
	void clear();

	void setMaxUnitIncrement(int pixels);

	/**
	 * Updates the image
	 * 
	 * @throws CompileTimeException
	 */
	void update(PluginManager manager) throws CompileTimeException;

	/**
	 * Removes all shapes.
	 */
	void clearShapes();

	void repaint();

}