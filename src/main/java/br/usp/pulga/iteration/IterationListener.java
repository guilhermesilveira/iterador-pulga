package br.usp.pulga.iteration;

import br.usp.pulga.Iteration;

public interface IterationListener {

    void after(Iteration iteration);

    void before(Iteration iteration);

    void init(Iteration iteration);

}
