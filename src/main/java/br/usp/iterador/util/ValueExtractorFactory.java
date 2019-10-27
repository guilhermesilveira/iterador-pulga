package br.usp.iterador.util;

import br.usp.iterador.model.Application;
import br.usp.iterador.model.Intermediate;
import br.usp.iterador.model.Parameter;

/**
 * Extractor factory.
 * 
 * @author Guilherme Silveira
 */
public class ValueExtractorFactory {

	public static ValueExtractor findField(Application data, String field) {
		for (Parameter p : data.getParameters()) {
			if (p.getName().equals(field)) {
				return new AdvancedValueExtractor(p.getName());
			}
		}
		for (Intermediate p : data.getIntermediateExpressions()) {
			if (p.getName().equals(field)) {
				return new AdvancedValueExtractor(p.getName());
			}
		}
		for (int i = 1; i <= data.getDimension(); i++) {
			if (field.equals("x" + i)) {
				return new DimensionValueExtractor(i);
			}
		}
		throw new RuntimeException("Field not found: " + field);
	}

}
