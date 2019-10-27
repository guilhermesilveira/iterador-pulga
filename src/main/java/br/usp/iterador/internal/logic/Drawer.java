package br.usp.iterador.internal.logic;

import java.awt.Graphics2D;

import br.usp.iterador.Constant;
import br.usp.iterador.gui.GUIHelper;
import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.Scale;

/**
 * Responsable for drawing the points.
 * 
 * @author Guilherme Silveira
 */
public class Drawer {

	private final boolean paintPoints;

	private final IterationListener iterationListener;

	private final IterationExecuter executer;

	private final int w, h;
    
    private final Scale wScale, hScale;

	private Application app;

	public Drawer(Application app, IterationListener iterationListener,
			boolean paintPoints, int w, int h) throws CompileTimeException {
		this.app = app;
		this.executer = new IterationExecuter(app);
		this.paintPoints = paintPoints;
		this.iterationListener = iterationListener;
		this.w = w;
		this.h = h;
        this.wScale = new Scale("width",0,w);
        this.hScale = new Scale("height",0,h);
	}

	/**
	 * Initializes it
	 * 
	 * @param g
	 */
	public void init(Graphics2D g) {

		// gets the helper and the clear guy
		GUIHelper helper = new GUIHelper(w, h);
		if (app.isClearBeforeDrawing()) {
			helper.clear(app.getBackgroundColor(), g);
		}

	}

	public void paint(Graphics2D g) {

        if (paintPoints && app.getGrid().isShowAxis()) {
            // gets the helper
            GUIHelper helper = new GUIHelper(w, h);
            g.setColor(app.getGrid().getColor());
            int xPos = (int) app.getXScale().changeValueTo(0.0, wScale);
            g.drawLine(xPos,0,xPos,h);
            int yPos = h - (int) app.getYScale().changeValueTo(0.0, hScale);
            g.drawLine(0,yPos,w,yPos);
        }

        // iterates
		executer.promoteIteration(iterationListener);

		// plays with the grid
		if (paintPoints && app.getGrid().isOn()) {
			// gets the helper
			GUIHelper helper = new GUIHelper(w, h);
			g.setColor(app.getGrid().getColor());
			helper.linhasVerticais(g, 10, app.getXScale().getMin(), app
					.getXScale().getMax(), Constant.quatroCasas);
			helper.linhasHorizontais(g, 10, app.getYScale().getMin(), app
					.getYScale().getMax(), Constant.oitoCasas);
		}

	}

}
