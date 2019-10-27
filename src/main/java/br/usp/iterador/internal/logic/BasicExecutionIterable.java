package br.usp.iterador.internal.logic;

import java.awt.Color;

public abstract class BasicExecutionIterable implements ExecutionIterable {

	protected int iteration;

	protected Color currentColor = Color.YELLOW;

	public Color getColor() {
		return this.currentColor;
	}

	public void setColor(Color newColor) {
		this.currentColor = newColor;
	}

	protected double[] _x;

	public double[] get_X() {
		return this._x;
	}

	public void set_X(double[] t) {
		this._x = t;
	}

	/**
	 * Returns the current iteration.
	 * 
	 * @return the iteration
	 */
	public int getIteration() {
		return iteration;
	}

	/**
	 * Tells this iteration to change to the next iteration
	 * 
	 */
	public void iterate() {
		iteration++;
	}

    public double norm(double x1,double x2) {
        return x1*x1+x2*x2;
    }

}
