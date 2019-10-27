package br.usp.iterador.util;

import br.usp.iterador.internal.logic.ExecutionIterable;
import br.usp.iterador.model.Application;

/**
 * Calculates view to world coordinates.
 *
 * @author Guilherme Silveira
 */
public class WorldView {

	private final ValueExtractor xField, yField;

	public WorldView(Application data) {
		xField = ValueExtractorFactory.findField(data, data.getXScale()
				.getField());
		yField = ValueExtractorFactory.findField(data, data.getYScale()
				.getField());
	}

	public double[] getAxisValues(ExecutionIterable iteration) {
		// se for um parametro
		return new double[] { xField.extractValue(iteration),
				yField.extractValue(iteration) };
	}

}
