package br.usp.iterador.gui;

import br.usp.iterador.internal.logic.CompileTimeException;
import br.usp.iterador.plugin.PluginManager;

public interface IteratorFrame {

	/**
	 * Adds a menu
	 *
	 * @see br.usp.iterador.plugin.PluginMenuFrame#addMenu(java.awt.Menu)
	 */
	void updateMenu();

	int getMenuHeight();

	void updateDrawing(PluginManager manager) throws CompileTimeException;

	void clear();

	PulgaCanvas getCanvas();

	void reorganize();

	void repaint();

	void setTitle(String title);

}