package br.usp.iterador.plugin.bacia;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;

import org.apache.log4j.Logger;

import br.usp.iterador.Constant;
import br.usp.iterador.gui.GUIHelper;
import br.usp.iterador.internal.logic.CompileTimeException;
import br.usp.iterador.internal.logic.IterationExecuter;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.GridOptions;
import br.usp.iterador.model.ImageInfo;
import br.usp.iterador.universeiterator.UniverseIteratorExecutorListener;

/**
 * Executes the basin plugin algorithm.
 * 
 * @author Guilherme Silveira
 */
public class BasinExecutorListener implements UniverseIteratorExecutorListener {

	private static final Logger logger = Logger
			.getLogger(BasinExecutorListener.class);

	private IterationExecuter executer;

	private int height, width;

	private Color[][] color;

	private Graphics2D graphics;

	private Application app;

	private final BasinController basinController;

	public BasinExecutorListener(Basin basin, Application app,
			Graphics2D graphics, BasinController controller)
			throws CompileTimeException {

		this.app = app;
		this.basinController = controller;
		this.executer = new IterationExecuter(app, new BasinIterationListener(
				controller, basin));

		this.graphics = graphics;

		ImageInfo info = app.getImage();
		this.height = info.getHeight();
		this.width = info.getWidth();

		this.color = new Color[height + 5][width + 5];

	}

	public void init() {
		logger.debug("Clearing background");
		graphics.setColor(app.getBackgroundColor());
		graphics.fillRect(0, 0, width, height);
	}

	/**
	 * @param step
	 *            o numero de passos entre blocos
	 * @param stepX
	 * @return
	 * 
	 */
	public void doPoint(double leftX, double leftY, double rightX,
			double rightY, int ypos, int xpos, int stepY, int stepX,
			int correctX, int correctY) {

		double middleX = (leftX + rightX) / 2, middleY = (leftY + rightY) / 2;
		// iterates
		IterationResult result = new BasinLogic(basinController).iterate(app,
				executer, middleX, middleY);
		logger.debug("Iteration result: " + middleX + "," + middleY + " :: "
				+ Arrays.toString(result.getAverages()));
		Color color = basinController.getPointColor(result.getAverages());

		// int iterations = result.getIterations() - dados.getTrashPoints();

		// GRADIENTE: 0 porcento se demorou tudo para convergir
		/*
		 * double percentColor = 1 - ((iterations + 0.0) / dados
		 * .getIteratedPoints()); percentColor *= percentColor; // percentColor *=
		 * percentColor; int red = (int) (color.getRed() * percentColor); int
		 * green = (int) (color.getGreen() * percentColor); int blue = (int)
		 * (color.getBlue() * percentColor); percentColor = 1 - percentColor;
		 * red += (int) (bg.getRed() * percentColor); green += (int)
		 * (bg.getGreen() * percentColor); blue += (int) (bg.getBlue() *
		 * percentColor); Color finalColor = new Color(red, green, blue);
		 */
		/*
		 * logger.debug(iterations + "," + dados.getIteratedPoints() + "," +
		 * finalColor + "," + percentColor);
		 */

		// paints
		// TODO ignorando os valores
		graphics.setColor(color);
		graphics.fillRect(xpos, height - ypos - stepY, stepX, stepY);

		this.color[correctY][correctX] = color;

	}

	public void finish() {
		GUIHelper helper = new GUIHelper(width, height);
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
