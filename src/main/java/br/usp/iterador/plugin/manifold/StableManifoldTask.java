package br.usp.iterador.plugin.manifold;

import org.apache.log4j.Logger;

import br.usp.iterador.i18n.Messages;
import br.usp.iterador.internal.logic.CompileTimeException;
import br.usp.iterador.model.Application;
import br.usp.iterador.plugin.bacia.BasinTask;
import br.usp.iterador.task.LongTask;
import br.usp.iterador.universeiterator.UniverseIteratorExecutor;

/**
 * Stable manifold task.
 * 
 * @author Guilherme Silveira
 */
public class StableManifoldTask implements LongTask {

	private static final Logger LOG = Logger.getLogger(BasinTask.class);

	private UniverseIteratorExecutor executor;

	public StableManifoldTask(Application app,
			StableManifoldExecutorListener listener)
			throws CompileTimeException {
		this.executor = new UniverseIteratorExecutor(app, listener);
		LOG.debug("Preparing a stable manifold task");
	}

	public String getName() {
		return Messages.getString("stable_manifold");
	}

	public double getPercentageComplete() {
		return this.executor.getPercentageComplete();
	}

	public void run() {
		LOG.debug("Stable manifold task starting");
		executor.init();
		executor.doSquare();
		executor.finish();
		LOG.debug("Stable manifold task finished");
	}

}
