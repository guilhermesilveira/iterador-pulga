package br.usp.iterador.logic.image;

import br.usp.iterador.AbstractTestCase;

public class ClearWindowTest extends AbstractTestCase {

	public void testExecute() {
		ClearWindow action = new ClearWindow();
		action.execute(frame);
		assertTrue(frame.getCanvas().isClear());
	}

}
