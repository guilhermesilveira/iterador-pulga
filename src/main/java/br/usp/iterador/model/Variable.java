package br.usp.iterador.model;

import br.usp.iterador.gui.ModelType;
import br.usp.iterador.gui.util.ModelTextField;

/**
 * A variable.
 * 
 * @author Guilherme Silveira
 */
public class Variable {

	private double initialValue;

	@ModelType(component = ModelTextField.class, width = 0.4)
	private String code;

	public Variable(double initialValue, String iteration) {
		this.initialValue = initialValue;
		this.code = iteration;
	}

	public double getInitialValue() {
		return initialValue;
	}

	public void setInitialValue(double initialValue) {
		this.initialValue = initialValue;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String iteration) {
		this.code = iteration;
	}

}
