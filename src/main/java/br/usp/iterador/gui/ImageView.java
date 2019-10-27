package br.usp.iterador.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import br.usp.iterador.model.Application;

public interface ImageView {

	void paintOn(Graphics2D g, int w, int h);

	/**
	 * Changes the image buffer
	 * 
	 * @param w
	 *            width
	 * @param h
	 *            height
	 */
	void setSize(Application app, int w, int h);

	/**
	 * Returns the buffered graphics
	 * 
	 * @return the graphics
	 */
	Graphics2D getGraphics();

	/**
	 * Returns the buffer itself
	 * 
	 * @return itself
	 */
	BufferedImage getBuffer();

	boolean isStillValid(Application app);

}