package br.usp.iterador.logic.image;

import br.usp.iterador.gui.IteratorFrame;
import br.usp.iterador.logic.PulgaAction;

/**
 * Clears the window.
 * 
 * @author Guilherme Silveira
 */
public class ClearWindow implements PulgaAction {

	public void execute(IteratorFrame frame) {
		frame.getCanvas().clear();
	}

}
