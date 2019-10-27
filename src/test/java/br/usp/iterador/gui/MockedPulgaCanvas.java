package br.usp.iterador.gui;

import br.usp.iterador.gui.PulgaCanvas;
import br.usp.iterador.gui.util.PulgaRectangle;
import br.usp.iterador.internal.logic.CompileTimeException;
import br.usp.iterador.plugin.PluginManager;

public class MockedPulgaCanvas implements PulgaCanvas {

	private boolean repainted;

	private boolean clear;

	public boolean isRepainted() {
		return repainted;
	}

	public void addShape(PulgaRectangle rectangle) {
	}

	public void changeSize(int w, int h) {
	}

	public void clear() {
		clear = true;
	}

	public void setMaxUnitIncrement(int pixels) {
	}

	public void update(PluginManager manager) throws CompileTimeException {
	}

	public void clearShapes() {
	}

	public void repaint() {
		repainted = true;
	}

	public boolean isClear() {
		return clear;
	}

}
