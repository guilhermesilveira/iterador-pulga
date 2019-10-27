package br.usp.pulga.hiperboloide;

import java.util.ArrayList;
import java.util.List;

import br.usp.pulga.Iteration;

public class SetIterationListener {

	private final Iteration iteration;

	private final List<InitialRule> rules;

	private final int steps;

	public SetIterationListener(int steps, Iteration iteration) {
		this.steps = steps;
		this.iteration = iteration;
		this.rules = new ArrayList<InitialRule>();

	}

	public void iterate(int k) {
		for (int step = 0; step < steps; step++) {
			iteration.init();
			for (InitialRule rule : rules) {
				rule.execute(step, steps, iteration);
			}
			for (int j = 0; j < k; j++) {
				iteration.iterate();
			}
		}
	}

	public void add(InitialRule rule) {
		this.rules.add(rule);
	}

}
