package br.usp.iterador.gui;

/**
 * Deals with the image.
 * 
 * @author Guilherme Silveira
 */
public class ImageHandler {

	private static final ImageView currentImage = new SimpleImageView();

	public static ImageView getCurrentImage() {
		return currentImage;
	}

}
