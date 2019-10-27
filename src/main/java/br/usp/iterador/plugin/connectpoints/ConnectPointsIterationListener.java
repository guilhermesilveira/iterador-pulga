package br.usp.iterador.plugin.connectpoints;

import java.awt.Graphics2D;
import java.util.HashMap;

import br.usp.iterador.gui.GUIHelper;
import br.usp.iterador.internal.logic.ExecutionIterable;
import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.model.Application;
import br.usp.iterador.util.WorldView;

/**
 * Join points with lines.
 * 
 * @author Guilherme Silveira
 */
public class ConnectPointsIterationListener implements IterationListener {

	private ConnectPointsData data;

	private GUIHelper helper;

	private Graphics2D g;

	private int lastX, lastY;

	private WorldView view;

	public ConnectPointsIterationListener(Application app,
			ConnectPointsData data) {
		this.data = data;
		this.view = new WorldView(app);
	}

	// fills the intermediate values
	private HashMap<String, Integer> valores = new HashMap<String, Integer>();
	{
		for (int i = 0; i != 100; i++) {
			valores.put("x" + (i + 1), new Integer(i));
		}
	}

	public boolean onPoint(Application dados, ExecutionIterable iteration) {

		// gets x and y
		double[] pos = view.getAxisValues(iteration);

		// translate them
		int px = (int) helper.mudaEscala(pos[0], dados.getXScale().getMin(),
				dados.getXScale().getMax(), 0, helper.getWidth());
		int py = (int) helper.mudaEscala(pos[1], dados.getYScale().getMin(),
				dados.getYScale().getMax(), 0, helper.getHeight());

		py = helper.getHeight() - py;

		// paint them
		if (iteration.getIteration() > dados.getTrashPoints()) {
			g.setColor(this.data.getColor());
			g.drawLine(lastX, lastY, px, py);
		}
		this.lastX = px;
		this.lastY = py;

		return true;
	}

	/**
	 * Initializes the data
	 */
	public void init(Graphics2D g, GUIHelper helper) {
		this.helper = helper;
		this.g = g;
	}

	public void initData() {
	}

}
