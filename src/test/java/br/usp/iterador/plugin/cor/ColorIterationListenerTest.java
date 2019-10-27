package br.usp.iterador.plugin.cor;

import java.awt.Color;

import org.jmock.Mock;

import br.usp.iterador.AbstractTestCase;
import br.usp.iterador.internal.logic.ExecutionIterable;
import br.usp.iterador.model.Application;

public class ColorIterationListenerTest extends AbstractTestCase {

	public void testSwitchesBetweenColors() {

		Mock color = mock(ColorDataInfo.class);
		color.expects(once()).method("getNIterations").will(returnValue(100));
		color.expects(once()).method("getColorFor").with(eq(100)).will(returnValue(Color.RED));
		ColorIterationListener listener = new ColorIterationListener((ColorDataInfo) color.proxy());

		Mock iteration = mock(ExecutionIterable.class);
		iteration.expects(once()).method("getIteration").will(returnValue(200));
		iteration.expects(once()).method("setColor").with(eq(Color.RED));

		Application app = new Application();
		app.setTrashPoints(100);

		assertTrue(listener.onPoint(app, (ExecutionIterable) iteration.proxy()));

	}

	public void testDoNothingWhileNotInPlottedPoints() {

		Mock color = mock(ColorDataInfo.class);
		color.expects(once()).method("getNIterations").will(returnValue(100));
		ColorIterationListener listener = new ColorIterationListener(
				(ColorDataInfo) color.proxy());

		Mock iteration = mock(ExecutionIterable.class);
		iteration.expects(once()).method("getIteration").will(returnValue(1));

		Application app = new Application();
		app.setTrashPoints(100);

		assertTrue(listener.onPoint(app, (ExecutionIterable) iteration.proxy()));

	}

	public void testChangesTheColorWhenHitsTheFirstPoint() {

		Mock color = mock(ColorDataInfo.class);
		color.expects(once()).method("getColorFor").with(eq(0)).will(
				returnValue(Color.RED));
		ColorIterationListener listener = new ColorIterationListener(
				(ColorDataInfo) color.proxy());

		Mock iteration = mock(ExecutionIterable.class);
		iteration.expects(once()).method("getIteration").will(returnValue(100));
		iteration.expects(once()).method("setColor").with(eq(Color.RED));

		Application app = new Application();
		app.setTrashPoints(100);

		assertTrue(listener.onPoint(app, (ExecutionIterable) iteration.proxy()));

	}

}
