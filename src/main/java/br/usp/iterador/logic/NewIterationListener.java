package br.usp.iterador.logic;

import br.usp.iterador.internal.logic.ExecutionIterable;
import br.usp.iterador.model.Application;

public interface NewIterationListener {

	void init();

	/**
	 * A new point has been iterated.
	 *
	 * @param application
	 *            data
	 * @param iterable
	 *            data
	 */
	boolean onPoint(Application dados, ExecutionIterable iterable);

	String getExtraCode();

	Class getExtraInterface();

}
