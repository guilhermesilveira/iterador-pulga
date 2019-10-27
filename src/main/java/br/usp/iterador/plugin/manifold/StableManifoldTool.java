package br.usp.iterador.plugin.manifold;

import java.awt.Color;

import javax.swing.Icon;

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
 * Stable manifold tool.
 * 
 * @author Guilherme Silveira
 */
public class StableManifoldTool implements Tool {

	private static final Logger LOG = Logger
			.getLogger(StableManifoldTool.class);

	private Controller controller;

	private Application app;

	private SimpleIteratorFrame iteratorFrame;

	private final StableManifoldData data;

	public StableManifoldTool(StableManifoldData data) {
		this.data = data;
	}

	public String getName() {
		return "stable_manifold";
	}

	public void activate(Controller controller) {
		this.iteratorFrame = controller.find(SimpleIteratorFrame.class);
		this.app = controller.find(Application.class);
		this.iteratorFrame.getCanvas().clearShapes();
		this.controller = controller;
		data.getView().reset();
	}

	public void deactivate() {
		data.getView().reset();
	}

	public synchronized void onClick(int pointX, int pointY) {

		ImageInfo info = app.getImage();
		int width = info.getWidth();
		int height = info.getHeight();

		ScaleHelper scaleHelper = new ScaleHelper(app.getXScale(), app
				.getYScale());
		double p[] = scaleHelper.fromViewToWorld(pointX, width, pointY, height);

		LOG.info("zoom plugin: " + p[0] + ", " + p[1]);

		// adds the point
		data.getView().addPoint(p[0], p[1]);
		if (data.getView().isDone()) {
			controller.executeAction("StableManifoldExecution");
		} else {
			data.getView().setOrigin(pointX, pointY);
		}
	}

	public void mouseMoved(int x, int y) {
		if (data.getView().getNumberOfPoints() == 1) {
			SimpleIteratorCanvas canvas = iteratorFrame.getCanvas();
			canvas.clearShapes();
			int xp = Math.min(data.getView().getOrigin()[0], x), yp = Math.min(
					data.getView().getOrigin()[1], y);
			int w = Math.abs(data.getView().getOrigin()[0] - x), h = Math
					.abs(data.getView().getOrigin()[1] - y);
			canvas.addShape(new PulgaRectangle(xp, yp, w, h, Color.YELLOW));
			canvas.repaint();
		}
	}

	/**
	 * Returns its icon
	 * 
	 * @see br.usp.iterador.plugin.tool.Tool#getIcon()
	 */
	public Icon getIcon() {
		return null;
	}

}
