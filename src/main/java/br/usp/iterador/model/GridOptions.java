package br.usp.iterador.model;

import java.awt.Color;

/**
 * Grid
 * 
 * @author Guilherme Silveira
 */
public class GridOptions {

	/**
	 * Whether it should be displayed or not
	 */
	private boolean on = false;

	/** Its color */
	private Color color = Color.WHITE;

	private boolean showAxis = false;

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}

	public boolean isShowAxis() {
		return showAxis;
	}

	public void setShowAxis(boolean showAxis) {
		this.showAxis = showAxis;
	}

}
