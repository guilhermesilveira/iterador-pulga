package br.usp.iterador.gui;

import br.usp.iterador.gui.IteratorFrame;
import br.usp.iterador.internal.logic.CompileTimeException;
import br.usp.iterador.plugin.PluginManager;

public class MockedIteratorFrame implements IteratorFrame {

	private final MockedPulgaCanvas canvas = new MockedPulgaCanvas();

	public void updateMenu() {
	}

	public int getMenuHeight() {
		return 0;
	}

	public void updateDrawing(PluginManager manager)
			throws CompileTimeException {
	}

	public void clear() {
	}

	public MockedPulgaCanvas getCanvas() {
		return canvas;
	}

	public void reorganize() {
	}

	public void repaint() {

	}

	public void setTitle(String title) {
	}

}
