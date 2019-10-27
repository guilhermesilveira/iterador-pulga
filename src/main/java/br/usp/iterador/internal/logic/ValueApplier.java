package br.usp.iterador.internal.logic;

import org.apache.log4j.Logger;

import br.usp.iterador.ConfigurationException;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.Parameter;

/**
 * Apply value changes to any kind of field.
 * 
 * @author Guilherme Silveira
 */
public class ValueApplier {

	private static final Logger LOG = Logger.getLogger(ValueApplier.class);

	private final Application app;

	public ValueApplier(Application app) {
		this.app = app;
	}

	/**
	 * Changes someones values
	 * 
	 * @param name
	 *            field name
	 * @param value
	 *            field value
	 */
	public void applyValue(String name, double value) {
		for (Parameter p : app.getParameters()) {
			if (p.getName().equals(name)) {
				LOG.debug("Applying value on parameter " + name + " = " + value);
				p.setValue("" + value);
				return;
			}
		}
		for (int i = 1; i <= app.getDimension(); i++) {
			if (name.equals("x" + i)) {
				app.getVariables().get(i - 1).setInitialValue(value);
				return;
			}
		}
		// TODO accept intermediate values
		throw new ConfigurationException(
				"Please select parameters or initial conditions for the xscale and yscale");
	}

	public double getValue(String name) {
		for (Parameter p : app.getParameters()) {
			if (p.getName().equals(name)) {
				return Double.parseDouble(p.getValue());
			}
		}
		for (int i = 1; i <= app.getDimension(); i++) {
			if (name.equals("x" + i)) {
				return app.getVariables().get(i - 1).getInitialValue();
			}
		}
		// TODO accept intermediate values
		throw new ConfigurationException(
				"Please select parameters or initial conditions for the xscale and yscale");
	}

}
