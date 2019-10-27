package br.usp.pulga;

import br.usp.pulga.iteration.IterationListener;

public interface Iteration {

	double getVariable(String name);

	void iterate();

    void addListener(IterationListener listener);

    void init();

	Variable findVariable(String variable);

}
