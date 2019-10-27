package br.usp.pulga.iteration;

import br.usp.pulga.Iteration;

public class PlotListener implements IterationListener {

	private final PGraphics graphics;

	private final World xWorld;

	private final World yWorld;

	private final Axis x;

	private final Axis y;

	private final int trashPoints;

	public PlotListener(PGraphics pgraphics, World xWorld, World yWorld,
			Axis x, Axis y) {
		this(pgraphics, xWorld, yWorld, x, y, 0);
	}

	public PlotListener(PGraphics pgraphics, World xWorld, World yWorld,
			Axis x, Axis y, int trashPoints) {
		this.graphics = pgraphics;
		this.xWorld = xWorld;
		this.yWorld = yWorld;
		this.x = x;
		this.y = y;
		this.trashPoints = trashPoints;
	}

	public void after(Iteration iteration) {
		if (iteration.getVariable("iteration") > trashPoints) {
			double x0 = iteration.getVariable(x.getFieldName());
			double x1 = iteration.getVariable(y.getFieldName());
			int px = (int) xWorld.locate(x0, x);
			int py = (int) yWorld.locate(x1, y);
			graphics.rect(px, py, 0, 0);
		}
	}

	public void before(Iteration iteration) {
	}

	public void init(Iteration iteration) {
	}

}
