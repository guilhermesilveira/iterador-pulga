package br.usp.iterador.logic;

import org.jmock.Mock;

import br.usp.iterador.AbstractTestCase;

public class ArrayIterationListenerTest extends AbstractTestCase {

	public void testOnPointInvokesAllItsRecordedListeners() {
		ArrayIterationListener l = new ArrayIterationListener();
		for (int i = 0; i < 5; i++) {
			Mock mock = mock(IterationListener.class);
			mock.expects(once()).method("onPoint").will(returnValue(true));
			l.add((IterationListener) mock.proxy());
		}
		l.onPoint(null, null);
	}

	public void testOnPointReturnsFalseIfAtLeastOneReturnsFalse() {
		ArrayIterationListener l = new ArrayIterationListener();
		for (int i = 0; i < 5; i++) {
			Mock mock = mock(IterationListener.class);
			mock.expects(once()).method("onPoint").will(returnValue(i != 3));
			l.add((IterationListener) mock.proxy());
		}
		assertFalse(l.onPoint(null, null));
	}

	public void testOnPointReturnsTrueIfAllReturnsTrue() {
		ArrayIterationListener l = new ArrayIterationListener();
		for (int i = 0; i < 5; i++) {
			Mock mock = mock(IterationListener.class);
			mock.expects(once()).method("onPoint").will(returnValue(true));
			l.add((IterationListener) mock.proxy());
		}
		assertTrue(l.onPoint(null, null));
	}

}
