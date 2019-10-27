package br.usp.iterador.gui.util;

import br.usp.iterador.gui.GUIHelper;
import br.usp.iterador.model.Scale;

public class ScaleHelper {

	private Scale xScale;
	private Scale yScale;

	public ScaleHelper(Scale x, Scale y) {
		this.xScale = x;
		this.yScale = y;
	}

	public double[] fromViewToWorld(int x, int width, int y, int height) {
		y = height - y;
		// reescala para os valores ok
		GUIHelper helper = new GUIHelper(width, height);
		double realX = helper.mudaEscala(x, 0, width, xScale);
		double realY = helper.mudaEscala(y, 0, height, yScale);
		return new double[]{realX,realY};
	}

}
