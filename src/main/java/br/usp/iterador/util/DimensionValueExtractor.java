package br.usp.iterador.util;

import br.usp.iterador.internal.logic.ExecutionIterable;

/**
 * Extracts a value from a dimension.
 *
 * @author Guilherme Silveira
 */
public class DimensionValueExtractor implements ValueExtractor {

	private final int selected;

	public DimensionValueExtractor(int i) {
		this.selected = i;
	}

	public double extractValue(ExecutionIterable iteration) {
		return iteration.get_X()[selected - 1];
	}

}
