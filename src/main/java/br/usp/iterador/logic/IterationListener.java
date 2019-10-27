package br.usp.iterador.logic;

import java.awt.Graphics2D;

import br.usp.iterador.gui.GUIHelper;
import br.usp.iterador.internal.logic.ExecutionIterable;
import br.usp.iterador.model.Application;

/**
 * An iteration listener.
 * 
 * @author Guilherme Silveira
 */
public interface IterationListener {

	/**
	 * @return Whether it should continue the iteration
	 */
	public boolean onPoint(Application dados, ExecutionIterable iteration);

	public void init(Graphics2D g, GUIHelper helper);

	/**
	 * Initializes the current data if needed by adding or changing parameters
	 * and colors at the beginning
	 * 
	 * @param data
	 */
	public void initData();

}
