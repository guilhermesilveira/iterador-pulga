package br.usp.iterador.internal.logic;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.janino.ClassBodyEvaluator;
import net.janino.Scanner;

import org.apache.log4j.Logger;

import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.logic.NewIterationListener;
import br.usp.iterador.model.Application;

/**
 * Responsible for executing the iterations.
 * 
 * @author Guilherme Silveira
 */
public class IterationExecuter {

	private static final Logger LOG = Logger.getLogger(IterationExecuter.class);

	private ExecutionIterable iterable;

	private NewIterationListener iterationListener;

	private Application app;

	public IterationExecuter(Application app) throws CompileTimeException {
		build(app);
	}

	public IterationExecuter(Application app,
			NewIterationListener iterationListener) throws CompileTimeException {
		this.iterationListener = iterationListener;
		build(app);
	}

	private void build(Application app) throws CompileTimeException {
		this.app = app;
		int dim = app.getDimension();

		// sets the initial position
		ScriptFactory factory = new ScriptFactory();
		ClassBuilder cb = new ClassBuilder();
		cb.build(factory, app);

		// creates the class

		String entireCode = cb.getCode(dim);
		if (this.iterationListener != null) {
			entireCode += "\n" + this.iterationListener.getExtraCode();
		}

		List<Class> interfaces = new ArrayList<Class>();
		interfaces.add(ExecutionIterable.class);

		if (this.iterationListener != null) {
			interfaces.add(iterationListener.getExtraInterface());
		}

		try {

			Scanner sc = new Scanner(null, new StringReader(entireCode));
			Class clazz = new ClassBodyEvaluator(sc,
					BasicExecutionIterable.class, interfaces
							.toArray(new Class[interfaces.size()]), null)
					.evaluate();
			this.iterable = (ExecutionIterable) clazz.newInstance();

		} catch (Exception e) {
			throw new CompileTimeException("Unable to compile the java code", e);
		}
	}

	/**
	 * Promotes some iteration
	 * 
	 * @param listener
	 *            listener
	 * @return the iteration when it broke
	 */
	public int promoteIteration(IterationListener listener) {

		// allows the plugins to break over the initial values
		listener.initData();

		iterable.set_X(ClassBuilder.copyInitialValues(app.getVariables()));

		LOG.debug("starting with " + Arrays.toString(iterable.get_X()));

		if (this.iterationListener != null) {
			iterationListener.init();
		}

		// for each point
		int trash = app.getTrashPoints();
		int total = (trash-1) + app.getIteratedPoints();
		int iteration = 0;
		iterationLoop: for (; iteration <= total; iteration++) {

			// if supposed to draw, call it
			if (shouldPaint(trash, iteration)) {
				if (!listener.onPoint(app, iterable)) {
					// should stop!
					LOG.debug("listener request: stopping iteration on number "
							+ iteration);
					break;
				}
			}

			for (int j = 0; j < app.getIterationPower(); j++) {
				// updates all values
				iterable.prepare();
				iterable.nextIntermediateRound();
				iterable.runPieceOfCode(trash, iteration);
				iterable.nextRound();
			}

			if (iterationListener != null
					&& !(iterationListener.onPoint(app, iterable))) {
				// should stop!!!
				LOG.debug("stopping iteration on number " + iteration);
				break iterationLoop;
			}

			// adds one
			iterable.iterate();

		}

		return iteration;

	}

	boolean shouldPaint(int trash, int iteration) {
		return (iteration + 1) > trash;
	}

}
