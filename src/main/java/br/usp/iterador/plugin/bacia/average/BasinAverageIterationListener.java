package br.usp.iterador.plugin.bacia.average;

import br.usp.iterador.internal.logic.ExecutionIterable;
import br.usp.iterador.model.Application;
import br.usp.iterador.plugin.bacia.Basin;
import br.usp.iterador.plugin.bacia.BasinController;
import br.usp.iterador.plugin.bacia.BasinIterationListener;

public class BasinAverageIterationListener extends BasinIterationListener {

	public BasinAverageIterationListener(BasinController c, Basin basin) {
		super(c, basin);
	}

	public boolean onPoint(Application dados, ExecutionIterable iterable) {
		super.onPoint(dados, iterable);
		return true;
	}

}
