package br.usp.iterador.gui.util;

/**
 * Base model with resizing capabilities.
 * 
 * @author Guilherme Silveira
 */
public abstract class BasicModel implements ModelField {

	private double width = 0.1, height = 0.1;

	public void setSize(double width, double height) {
		this.width = width;
		this.height = height;
	}

	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

}
