package br.usp.pulga.hiperboloide;

import br.usp.pulga.Iteration;
import br.usp.pulga.IterationRule;

public class AngleIterationHiperoboloideTest extends HiperboloideTest{

    public void testHandlesAngleIteration() {
        IterationRule rule = getRule(0.74, 0.74, 0.75);
        rule = wrapAngle(rule);
        //System.out.println(rule.getCode());
        Iteration iteration = rule.buildIteration();
        iteration.iterate();
        //System.out.println(iteration.getVariable("omega"));
    }

}
