package br.usp.iterador.plugin.cor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.JColorChooser;

import org.apache.log4j.Logger;

import br.usp.iterador.gui.ImageHandler;
import br.usp.iterador.gui.IteratorFrame;
import br.usp.iterador.i18n.Messages;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.ImageInfo;

/**
 * Asks for two colors and change them in the buffered image.
 * 
 * @author Guilherme Silveira
 */
public class ChangeAColor implements PulgaAction {

	private static final Logger logger = Logger.getLogger(ChangeAColor.class);

	private BufferedImage bi;

	private Graphics2D g;

	private final Application data;
    private final IteratorFrame frame;

    public ChangeAColor(Application data, IteratorFrame frame) {
		this.data = data;
        this.frame = frame;
    }

	public void changeColor(Color original, Color replace) {
		ImageInfo info = data.getImage();
		g.setColor(replace);
		float[] f = new float[3];
		for (int y = 0; y < info.getHeight(); y++) {
			for (int x = 0; x < info.getWidth(); x++) {
				Color color = new Color(bi.getRGB(x, y));
				double d = dist(original, color);
				if (d < 0.1) {
					logger.debug(Arrays.toString(color.getColorComponents(f))
							+ " :: changing because distance = " + d);
					g.drawRect(x, y, 0, 0);
				}
			}
		}
	}

	private double dist(Color c1, Color c2) {
		return ((c1.getRed() - c2.getRed()) * (c1.getRed() - c2.getRed())
				+ (c1.getGreen() - c2.getGreen())
				* (c1.getGreen() - c2.getGreen()) + (c1.getBlue() - c2
				.getBlue())
				* (c1.getBlue() - c2.getBlue()))
				/ (255 * 255);
	}

	public void execute() {

		// choose colors
		Color c1 = pickColor();
		if (c1 == null) {
			return;
		}
		Color c2 = pickColor();
		if (c2 == null) {
			return;
		}

		bi = ImageHandler.getCurrentImage().getBuffer();
		g = (Graphics2D) bi.getGraphics();

		changeColor(c1, c2);
        frame.getCanvas().repaint();

	}

	private Color pickColor() {
		return JColorChooser.showDialog(null, Messages.getString("color"),
				Color.BLACK);
	}

}
