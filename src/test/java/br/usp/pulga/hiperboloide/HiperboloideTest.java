package br.usp.pulga.hiperboloide;

import junit.framework.TestCase;
import br.usp.pulga.AngleIterationRule;
import br.usp.pulga.DefaultIterationRule;
import br.usp.pulga.Iteration;
import br.usp.pulga.IterationRule;

public abstract class HiperboloideTest extends TestCase {

	public Iteration getIteration(double startX, double startY, double c) {
		IterationRule rule = getRule(startX, startY, c);
		return rule.buildIteration();
	}

	protected IterationRule getRule(double startX, double startY, double c) {
		IterationRule rule = new DefaultIterationRule();
		// rule.create("x0").starts(startX).iterate("x1");
		rule.addVariable("x0",  startX, "x1");
		rule.addVariable("x1",  startY, "c * (1 - x0 * x0 + x1 * x1)");
		rule.addVariable("c",  c, "c");
		return rule;
	}

	protected IterationRule wrapAngle(IterationRule rule) {
		return new AngleIterationRule(rule, 0, 0);
	}

}
