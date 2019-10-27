package br.usp.iterador.gui;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.apache.log4j.Logger;

import br.usp.iterador.model.ImageInfo;

/**
 * Canvas which shows iterated values.
 *
 * @author Guilherme Silveira
 */
public class IteratorCanvas extends Canvas {

	private static Logger LOG = Logger.getLogger(IteratorCanvas.class);

	private static final long serialVersionUID = 3256720671697745713L;

	public IteratorCanvas(ImageInfo image) {
		setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
	}

	public void paint(Graphics g) {
		ImageHandler.getCurrentImage().paintOn((Graphics2D) g, this.getWidth(),
				this.getHeight());
	}

	@Override
	public void setSize(Dimension d) {
		super.setSize(d);
		LOG.debug("Updating canvas with size " + this.getSize());
	}

}
