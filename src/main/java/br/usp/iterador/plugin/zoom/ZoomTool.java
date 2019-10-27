package br.usp.iterador.plugin.zoom;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import br.usp.iterador.gui.SimpleIteratorFrame;
import br.usp.iterador.gui.SimpleIteratorCanvas;
import br.usp.iterador.gui.util.PulgaRectangle;
import br.usp.iterador.gui.util.ScaleHelper;
import br.usp.iterador.logic.Controller;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.ImageInfo;
import br.usp.iterador.plugin.tool.Tool;

/**
 * Zoom tool.
 *
 * @author Guilherme Silveira
 */
public class ZoomTool implements Tool {

	private static final Logger LOG = Logger.getLogger(ZoomTool.class);

	private RectangleData data = new RectangleData();

	private Controller controller;

	public String getName() {
		return "zoom";
	}

	public String getTooltip() {
		return "zoom_tooltip";
	}

	public void activate(Controller controller) {
		data.reset();
		this.controller = controller;
	}

	public void deactivate() {
		data.reset();
	}

	public synchronized void onClick(int pointX, int pointY) {

		Application app = controller.find(Application.class);

		ImageInfo info = app.getImage();
		int width = info.getWidth();
		int height = info.getHeight();

		ScaleHelper scaleHelper = new ScaleHelper(app.getXScale(), app
				.getYScale());
		double p[] = scaleHelper.fromViewToWorld(pointX, width, pointY, height);

		LOG.info("zoom plugin: " + p[0] + ", " + p[1]);

		// adds the point
		data.addPoint(p[0], p[1]);
		if (data.isDone()) {
			executeZoom(app);
		} else {
			data.setOrigin(pointX, pointY);
		}
	}

	private void executeZoom(Application app) {

		double[][] c = data.getCoordinates();

		SimpleIteratorFrame frame = controller.find(SimpleIteratorFrame.class);
		frame.getCanvas().clearShapes();

		app.getXScale().setMin(Math.min(c[0][0], c[1][0]));
		app.getXScale().setMax(Math.max(c[0][0], c[1][0]));
		app.getYScale().setMin(Math.min(c[0][1], c[1][1]));
		app.getYScale().setMax(Math.max(c[0][1], c[1][1]));

		// chama o redraw
		controller.executeAction("redraw");

		data.reset();

	}

	/**
	 * When the mouse moves, shows the rectangle if it has selected only one
	 * point.
	 */
	public void mouseMoved(int x, int y) {
		if (data.getNumberOfPoints() == 1) {
			SimpleIteratorCanvas canvas = controller.find(SimpleIteratorFrame.class).getCanvas();
			canvas.clearShapes();
			int xp = Math.min(data.getOrigin()[0], x), yp = Math.min(data
					.getOrigin()[1], y);
			int w = Math.abs(data.getOrigin()[0] - x), h = Math.abs(data
					.getOrigin()[1]
					- y);
			canvas.addShape(new PulgaRectangle(xp, yp, w, h, Color.YELLOW));
			canvas.repaint();
		}
	}

	public Icon getIcon() {
		return new ImageIcon(ZoomTool.class.getResource("ZoomIn16.gif"));
	}

}
