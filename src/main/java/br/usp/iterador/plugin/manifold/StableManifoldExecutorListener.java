package br.usp.iterador.plugin.manifold;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import br.usp.iterador.Constant;
import br.usp.iterador.gui.GUIHelper;
import br.usp.iterador.gui.ImageHandler;
import br.usp.iterador.internal.logic.CompileTimeException;
import br.usp.iterador.internal.logic.IterationExecuter;
import br.usp.iterador.internal.logic.ValueApplier;
import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.GridOptions;
import br.usp.iterador.model.ImageInfo;
import br.usp.iterador.plugin.PluginManager;
import br.usp.iterador.universeiterator.UniverseIteratorExecutorListener;

/**
 * Stable manifold execution listener
 * 
 * @author Guilherme Silveira
 */
public class StableManifoldExecutorListener implements
		UniverseIteratorExecutorListener {

	private static final Logger LOG = Logger
			.getLogger(StableManifoldExecutorListener.class);

	private static final int MAX_TRIALS = 200;

	private final IterationExecuter executer;

	private final int maxAmount;

	private final Color[][] color;

	private final Graphics2D graphics;

	private final Application app;

	private final IterationListener listeners;

	private final Rectangle2D focus;

	private final int minAmount;

	private final ManifoldIterationListener manifoldListener;

	private final StableManifoldValues values = new StableManifoldValues();

	public StableManifoldExecutorListener(Application app, Rectangle2D focus,
			PluginManager pluginManager) throws CompileTimeException {

		this.executer = new IterationExecuter(app);
		this.manifoldListener = new ManifoldIterationListener(app);

		this.app = app;

		this.color = new Color[app.getImage().getHeight() + 5][app.getImage()
				.getWidth() + 5];
		this.graphics = ImageHandler.getCurrentImage().getGraphics();
		this.minAmount = Integer.parseInt(JOptionPane.showInputDialog(
				"Min iterations inside", "20"));
		this.maxAmount = Integer.parseInt(JOptionPane.showInputDialog(
				"Max iterations inside", "30"));
		StableManifoldIterationListener iterationListener = new StableManifoldIterationListener(
				focus, this.maxAmount, this.minAmount, app, this.values);
		this.listeners = pluginManager.getIterationListener(app, false,
				iterationListener);
		LOG.debug("Using focus area: " + focus);
		this.focus = focus;

	}

	public void init() {
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, app.getImage().getWidth(), app.getImage()
				.getHeight());
		ImageInfo info = app.getImage();
		GUIHelper helper = new GUIHelper(info.getWidth(), info.getHeight());
		this.manifoldListener.init(graphics, helper);
	}

	public void doPoint(double leftX, double leftY, double rightX,
			double rightY, int ypos, int xpos, int stepY, int stepX,
			int correctX, int correctY) {

		double x = random(leftX, rightX), y = random(leftY, rightY);

		// initialize
		initValues(x, y);

		// iterates
		int result = executer.promoteIteration(listeners);

		// result
		int amount = values.getTotal();

		if (amount > this.minAmount && amount < this.maxAmount) {
			for (int k = 0; k < MAX_TRIALS; k++) {
				x = random(leftX, rightX);
				y = random(leftY, rightY);
				initValues(x, y);
				result = executer.promoteIteration(listeners);
				if ((amount = values.getTotal()) >= this.maxAmount) {
					break;
				}
			}
		}

		Color color = Color.WHITE;
		if (amount >= this.maxAmount) {
			color = Color.BLACK;
			initValues(x, y);
			manifoldListener.setResult(result);
			executer.promoteIteration(this.manifoldListener);
		}

		// paints
		graphics.setColor(color);
		graphics.fillRect(xpos, app.getImage().getHeight() - ypos, 0, 0);

		// graphics.fillRect(xpos, height - ypos - stepY, stepX, stepY);

		this.color[correctY][correctX] = color;

	}

	private double random(double left, double right) {
		return left + Math.random() * (right - left);
	}

	private void initValues(double x, double y) {
		// applies both values
		new ValueApplier(app).applyValue(app.getXScale().getField(), x);
		new ValueApplier(app).applyValue(app.getYScale().getField(), y);

		if (focus.contains(x, y)) {
			LOG.debug("beginning with field inside: " + x + " and " + y);
		}
	}

	public void finish() {
		GUIHelper helper = new GUIHelper(app.getImage().getWidth(), app
				.getImage().getHeight());
		GridOptions grid = app.getGrid();
		if (grid.isOn()) {
			graphics.setColor(grid.getColor());
			helper.linhasHorizontais(graphics, 5, app.getYScale().getMin(), app
					.getYScale().getMax(), Constant.quatroCasas);
			helper.linhasVerticais(graphics, 5, app.getXScale().getMin(), app
					.getXScale().getMax(), Constant.quatroCasas);
		}
	}

}
