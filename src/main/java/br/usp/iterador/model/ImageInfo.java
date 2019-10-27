package br.usp.iterador.model;

/**
 * Represents the main aspects of the image
 * 
 * @author Guilherme Silveira
 */
public class ImageInfo {

	private int width = 800, height = 600;

	/**
	 * @return Returns the height.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            The height to set.
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return Returns the width.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            The width to set.
	 */
	public void setWidth(int width) {
		this.width = width;
	}
}
