package br.usp.iterador.internal.logic;

import java.awt.Color;

/**
 * Interface to be implemente during runtime
 *
 * @author Guilherme Silveira
 */
public interface ExecutionIterable {

	void set_X(double[] temp);

	void nextIntermediateRound();

	void nextRound();

	void prepare();

	/**
	 * Returns false if it should stop
	 *
	 * @param b
	 * @return
	 */
	void runPieceOfCode(int trash, int iteration);

	/**
	 * Returns the X array
	 *
	 * @return the x array
	 */
	double[] get_X();

	/**
	 * Gets a value by a specific type name
	 *
	 * @param name
	 *            type name
	 * @return value
	 */
	double getRealValue(String name);

	/**
	 * Changes its color
	 * @param color	color
	 */
	void setColor(Color color);

	/**
	 * Returns its color
	 * @return	its color
	 */
	Color getColor();

	/**
	 * Returns the current iteration.
	 * @return	the iteration
	 */
	int getIteration();

	/**
	 * Tells this iteration to change to the next iteration
	 *
	 */
	void iterate();

}
