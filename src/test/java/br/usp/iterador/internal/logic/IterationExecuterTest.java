package br.usp.iterador.internal.logic;

import br.usp.iterador.model.Application;
import junit.framework.TestCase;

public class IterationExecuterTest extends TestCase {

	public void testPaintsStartingConditionIfTrashIsNull() throws CompileTimeException {
		Application app = new Application();
		IterationExecuter executer = new IterationExecuter(app);
		assertTrue(executer.shouldPaint(0, 0));
	}

	public void testDoesnotPaintStartingConditionIfTrashIsNotNull() throws CompileTimeException {
		Application app = new Application();
		IterationExecuter executer = new IterationExecuter(app);
		assertFalse(executer.shouldPaint(1, 0));
	}

	public void testPaintsFirstIteratedIfTrashIsNull() throws CompileTimeException {
		Application app = new Application();
		IterationExecuter executer = new IterationExecuter(app);
		assertTrue(executer.shouldPaint(0, 1));
	}

	public void testPaintsFirstIteratedIfTrashIsOne() throws CompileTimeException {
		Application app = new Application();
		IterationExecuter executer = new IterationExecuter(app);
		assertTrue(executer.shouldPaint(1, 1));
	}

	public void testDoesNotPaintFirstIteratedIfTrashIsTwo() throws CompileTimeException {
		Application app = new Application();
		IterationExecuter executer = new IterationExecuter(app);
		assertFalse(executer.shouldPaint(2, 1));
	}

	public void testPaintsSecondIteratedIfTrashIsNull() throws CompileTimeException {
		Application app = new Application();
		IterationExecuter executer = new IterationExecuter(app);
		assertTrue(executer.shouldPaint(0, 2));
	}

}
