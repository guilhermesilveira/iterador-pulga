package br.usp.pulga;

import br.usp.pulga.iteration.IterationListener;

public class SimpleIteration implements Iteration {

	private final IterationRule rule;

	public SimpleIteration(IterationRule rule) {
		this.rule = rule;
	}

	public double getVariable(String name) {
		return 0;
	}

	public void iterate() {
	}

    public void addListener(IterationListener listener) {
    }

    public void init() {
    }

	public void setVariable(String name, double value) {
	}

	public Variable findVariable(String variable) {
		return null;
	}

}
