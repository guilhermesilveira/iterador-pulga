package br.usp.pulga.iteration;

import br.usp.pulga.Iteration;

/**
 * Prints the current coordinate to sysout. Debug only.
 *
 * @author Guilherme Silveira
 */
public class SysoutListener implements IterationListener {

	private final Axis x;

	private final Axis y;

	public SysoutListener(Axis x, Axis y) {
		this.x = x;
		this.y = y;
	}

	public void after(Iteration iteration) {
		print(iteration);
	}

	private void print(Iteration iteration) {
		double x0 = iteration.getVariable(x.getFieldName());
		double x1 = iteration.getVariable(y.getFieldName());
		System.out.println(x0 + "," + x1);
	}

	public void before(Iteration iteration) {
	}

	public void init(Iteration iteration) {
		System.out.println("Comecando com ");
		print(iteration);
	}

}
