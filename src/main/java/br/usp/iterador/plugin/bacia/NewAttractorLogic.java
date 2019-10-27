package br.usp.iterador.plugin.bacia;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import br.usp.iterador.plugin.bacia.average.AverageCanvas;
import br.usp.iterador.plugin.bacia.average.PointMovementListener;
import br.usp.iterador.plugin.bacia.model.AttractorBuilder;
import br.usp.iterador.plugin.bacia.model.Cloud;
import br.usp.iterador.plugin.bacia.model.MyPolygon;
import br.usp.iterador.plugin.gui.WindowManager;

/**
 * Logic to register a new attractor.
 * 
 * @author Guilherme Silveira
 */
public class NewAttractorLogic {

	private PointMovementListener movementListener = new PointMovementListener();

	private AttractorBuilder builder = new AttractorBuilder();

	private WindowManager windows;

	public NewAttractorLogic(WindowManager windows) {
		this.windows = windows;
	}

	/**
	 * Executes it
	 * 
	 * @param canvas
	 */
	public void execute(final Basin basin, final AverageCanvas canvas) {
		new Thread(new Runnable() {
			public void run() {
				Cloud attractor = getNewAttractor(basin, canvas);
				basin.addAttractor(attractor);
			}
		}).start();
	}

	private Cloud getNewAttractor(Basin basin, AverageCanvas canvas) {

		// shows a message
		JOptionPane.showMessageDialog(canvas,
				"Select the points, right click to close");

		// activates the listeners
		canvas.addMouseMotionListener(this.movementListener);

		// waits for a new attractor
		while (true) {
			Point p = canvas.waitForNextPoint();
			if (p == null)
				break;
			this.builder.add(p, basin.getAveragesInfo().getAverageScales(),
					canvas.getWidth(), canvas.getHeight());
			canvas.repaint();
			this.movementListener.mouseMoved(new MouseEvent(canvas, 0, 0, 0, 0,
					0, 0, false));
		}

		// deactivates all listeners
		canvas.removeMouseMotionListener(this.movementListener);

		// creates it
		Cloud attractor = builder.create();
		builder.clear();

		// sets its color
		attractor.setColor(Color.BLUE);

		windows.refreshWindows();

		// returns the new attractor
		return attractor;

	}

	public MyPolygon getCurrentPolygon() {
		return builder.getCurrentPolygon();
	}
}
