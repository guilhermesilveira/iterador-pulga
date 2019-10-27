package br.usp.iterador.plugin.bacia.average;

import org.apache.log4j.Logger;

import br.usp.iterador.internal.logic.IterationExecuter;
import br.usp.iterador.model.Application;
import br.usp.iterador.plugin.bacia.Basin;
import br.usp.iterador.plugin.bacia.BasinController;
import br.usp.iterador.plugin.bacia.BasinLogic;
import br.usp.iterador.plugin.bacia.gui.ShowAverageSamples;

/**
 * Runs to iterate over random initial conditions.
 * 
 * @author Guilherme Silveira
 */
public class AverageThread implements Runnable {

	private static Logger logger = Logger.getLogger(AverageThread.class);

	private ShowAverageSamples frame;

	private boolean running;

	private Application app;

	private Basin basin;

	private BasinController controller;

	public AverageThread(ShowAverageSamples frame, Application app,
			BasinController controller, Basin basin) {
		this.frame = frame;
		this.app = app;
		this.basin = basin;
		this.controller = controller;
	}

	public void run() {
		try {
			IterationExecuter executer = new IterationExecuter(app,
					new BasinAverageIterationListener(controller, basin));
			while (this.running) {
				iterateOverARandomPoint(executer);
				waitSomeTime(100);
			}
		} catch (Exception e) {
			logger.error("Problem running the average thread", e);
		}
	}

	/**
	 * Iterates over a random point
	 * 
	 * @param executer
	 *            the iteration executer
	 * @return
	 */
	private void iterateOverARandomPoint(IterationExecuter executer) {

		// picks a random point
		double rx = Math.random()
				* (app.getXScale().getMax() - app.getXScale().getMin())
				+ app.getXScale().getMin();
		double ry = Math.random()
				* (app.getYScale().getMax() - app.getYScale().getMin())
				+ app.getYScale().getMin();

		logger.debug(String.format("random point: %f,%f", rx, ry));

		Double[] average = new BasinLogic(controller).iterateOverARandomPoint(
				app, executer, rx, ry);

		this.frame.addAverage(average);

	}

	/**
	 * Wait some time
	 */
	private void waitSomeTime(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			logger.error(e);
		}
	}

	/**
	 * Starts the thread
	 */
	public void init() {
		this.running = true;
		new Thread(this).start();
		new Thread(new Runnable() {
			public void run() {
				while (running) {
					frame.askForRepaint();
					waitSomeTime(2000);
				}
			}
		}).start();
	}

	/**
	 * Signalizes this thread to stop
	 */
	public void stop() {
		this.running = false;
	}

	/**
	 * Is this thread still running
	 * 
	 * @return true or false
	 */
	public boolean isRunning() {
		return running;
	}

}
