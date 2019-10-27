package br.usp.pulga.hiperboloide;

import br.usp.pulga.DefaultIterationRule;
import br.usp.pulga.Iteration;
import br.usp.pulga.iteration.IterationListener;

public class Hiperboloide implements MathIterable {

    private final DefaultIterationRule rule;

    private Iteration iteration;

    public Hiperboloide(double startX, double startY, double c) {
        this.rule = new DefaultIterationRule();
        rule.addVariable("x0",  startX, "x1");
        rule.addVariable("x1",  startY, "c * (1 - x0 * x0 + x1 * x1)");
        rule.addVariable("c",  c, "c");
        // System.out.println(this.rule.getCode());
        this.iteration = rule.buildIteration();
    }

    public void iterate(int k) {
        this.iteration.init();
        for (int i = 0; i < k; i++) {
            this.iteration.iterate();
        }
    }

    public void addListener(IterationListener listener) {
        add(listener);
    }

    public void add(IterationListener listener) {
        this.iteration.addListener(listener);
    }

	public Iteration getIteration() {
		return this.iteration;
	}

}
