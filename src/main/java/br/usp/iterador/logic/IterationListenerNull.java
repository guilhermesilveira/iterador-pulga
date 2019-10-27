package br.usp.iterador.logic;

import java.awt.Graphics2D;

import br.usp.iterador.gui.GUIHelper;
import br.usp.iterador.internal.logic.ExecutionIterable;
import br.usp.iterador.model.Application;

/**
 * Does nothing.
 * 
 * @author Guilherme Silveira
 */
public class IterationListenerNull implements IterationListener {
	public boolean onPoint(Application dados, ExecutionIterable iteration) {
		return true;
	}

	public void init(Graphics2D g, GUIHelper helper) {
	}

	public void initData() {

	}

}
