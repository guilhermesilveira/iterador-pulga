package br.usp.iterador.plugin.bacia;

import java.awt.Graphics2D;

import org.apache.log4j.Logger;

import br.usp.iterador.i18n.Messages;
import br.usp.iterador.internal.logic.CompileTimeException;
import br.usp.iterador.model.Application;
import br.usp.iterador.task.LongTask;
import br.usp.iterador.universeiterator.UniverseIteratorExecutor;

/**
 * Basin task.
 * 
 * @author Guilherme Silveira
 */
public class BasinTask implements LongTask {

	private static final Logger logger = Logger.getLogger(BasinTask.class);

	private UniverseIteratorExecutor executor;

	public BasinTask(Basin basin, Application app, Graphics2D graphics,
			BasinController controller) throws CompileTimeException {
		logger.info("Using attractors: " + basin.getAttractors());
		this.executor = new UniverseIteratorExecutor(app,
				new BasinExecutorListener(basin, app, graphics, controller));
	}

	public String getName() {
		return Messages.getString("basin_of_attraction");
	}

	public double getPercentageComplete() {
		return this.executor.getPercentageComplete();
	}

	public void run() {
		executor.init();
		executor.doSquare();
		executor.finish();
	}

}
