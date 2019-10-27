package br.usp.pulga;

import junit.framework.TestCase;

public class SimpleExecutorTest extends TestCase {

	public void testIterates() {
		IterationRule rule = new DefaultIterationRule();
		rule.addVariable("x0", 0, "x0 + 1");
		Iteration it = rule.buildIteration();
		it.iterate();
		double value = it.getVariable("x0");
		assertEquals(1.0, value);
	}

}
