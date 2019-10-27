package br.usp.pulga.hiperboloide;

import br.usp.pulga.Iteration;

public class InitialRule {

	private final double max;
	private final String variable;
	private final double min;

	public InitialRule(String variable, double min, double max) {
		this.variable = variable;
		this.min = min;
		this.max = max;

	}

	public void execute(int step, int steps, Iteration iteration) {
		double value = step * (max-min) / steps + min;
		iteration.findVariable(variable).setDefaultValue(value);
	}

}
