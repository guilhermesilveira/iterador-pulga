package br.usp.iterador.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import org.apache.log4j.Logger;

import br.usp.iterador.model.Application;
import br.usp.iterador.model.ImageInfo;

class SimpleImageView implements ImageView {

	private static final Logger logger = Logger
			.getLogger(SimpleImageView.class);

	private BufferedImage buffer = null;

	private int currentWidth;

	private int currentHeight;

	public void paintOn(Graphics2D g, int w, int h) {
		logger.debug("rescaling and painting");
		g.drawImage(buffer, w, h, new ImageObserver() {
			public boolean imageUpdate(Image img, int infoflags, int x, int y,
					int width, int height) {
				return false;
			}
		});
	}

	public void setSize(Application app, int w, int h) {
		BufferedImage image = new BufferedImage(w, h, ColorSpace.TYPE_RGB);
		logger.debug("Changing buffer size to " + w + "," + h + " with buffer "
				+ image);

		// buffers it
		buffer = image;
		// TODO remove is ok?
		if (buffer == null)
			return;
		currentWidth = w;
		currentHeight = h;

		// if it should clear before drawing, clears it
		if (app.isClearBeforeDrawing()) {
			clearBackground(app, w, h);
		}
	}

	public Graphics2D getGraphics() {
		return (Graphics2D) buffer.getGraphics();
	}

	private void clearBackground(Application app, int w, int h) {
		// sets the background if needed
		Graphics2D g = (Graphics2D) buffer.getGraphics();
		g.setColor(Color.BLACK);
		new GUIHelper(w, h).clear(app.getBackgroundColor(), g);
	}

	public boolean isStillValid(Application app) {
		ImageInfo info = app.getImage();
		return (buffer != null && info.getWidth() == currentWidth && info
				.getHeight() == currentHeight);
	}

	public BufferedImage getBuffer() {
		return buffer;
	}

}
