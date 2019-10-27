package br.usp.iterador.logic.system;

import org.jmock.Mock;

import br.usp.iterador.AbstractTestCase;
import br.usp.iterador.gui.IteratorFrame;

public class RedrawTest extends AbstractTestCase {

	public void testInvokesAnUpdate() {
		Redraw r = new Redraw();
		Mock frame = mock(IteratorFrame.class);
		frame.expects(once()).method("updateDrawing");
		r.execute((IteratorFrame) frame.proxy(), null);
	}

}
