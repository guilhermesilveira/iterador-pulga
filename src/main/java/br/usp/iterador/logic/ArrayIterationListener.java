package br.usp.iterador.logic;

import java.awt.Graphics2D;
import java.util.ArrayList;

import br.usp.iterador.gui.GUIHelper;
import br.usp.iterador.internal.logic.ExecutionIterable;
import br.usp.iterador.model.Application;

/**
 * An array of iteration listeners
 *
 * @author Guilherme Silveira
 */
public class ArrayIterationListener implements IterationListener {

	private final ArrayList<IterationListener> listeners = new ArrayList<IterationListener>();

	public boolean onPoint(Application dados, ExecutionIterable iteration) {
		boolean shouldContinue = true;
		for (IterationListener il : this.listeners) {
			shouldContinue &= il.onPoint(dados, iteration);
		}
		return shouldContinue;
	}

	/**
	 * Adds a new listener
	 */
	public void add(IterationListener l) {
		this.listeners.add(l);
	}

	/**
	 * Returns its size
	 */
	public int size() {
		return listeners.size();
	}

	/**
	 * Initializes the data
	 */
	public void init(Graphics2D g, GUIHelper helper) {
		for (IterationListener il : this.listeners) {
			il.init(g, helper);
		}
	}

	public void initData() {
		for (IterationListener il : this.listeners) {
			il.initData();
		}
	}

}
