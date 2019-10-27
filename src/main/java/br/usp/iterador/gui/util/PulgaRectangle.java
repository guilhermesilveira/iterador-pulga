package br.usp.iterador.gui.util;

import java.awt.Color;
import java.awt.Rectangle;

/**
 * Pulga rectangle with color.
 * 
 * @author Guilherme Silveira
 */
public class PulgaRectangle {

	private Rectangle rectangle;

	private Color color;

	public PulgaRectangle(int x, int y, int w, int h, Color color) {
		this.rectangle = new Rectangle(x, y, w, h);
		this.color = color;
	}

	/**
	 * @return Returns the color.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @see java.awt.Rectangle#getHeight()
	 */
	public int getHeight() {
		return (int) rectangle.getHeight();
	}

	/**
	 * @see java.awt.Rectangle#getWidth()
	 */
	public int getWidth() {
		return (int) rectangle.getWidth();
	}

	/**
	 * @see java.awt.Rectangle#getX()
	 */
	public int getX() {
		return (int) rectangle.getX();
	}

	/**
	 * @see java.awt.Rectangle#getY()
	 */
	public int getY() {
		return (int) rectangle.getY();
	}

}
