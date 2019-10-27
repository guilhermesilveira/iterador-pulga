package br.usp.iterador.task;

import org.apache.log4j.Logger;

/**
 * Repaints a window every 5 seconds.
 * 
 * @author Guilherme Silveira
 */
public class RepaintThread implements Runnable {

	private static final int PAUSE = 5 * 1000;

	private static final Logger LOG = Logger.getLogger(RepaintThread.class);

	private final LongTaskManager manager;

	public RepaintThread(LongTaskManager manager) {
		this.manager = manager;
	}

	public void run() {
		LOG.debug("Starting repaint thread for manager " + manager);
		while (this.manager.isTaskAlive()) {
			try {
				manager.updatePercentage();
				Thread.sleep(PAUSE);
			} catch (InterruptedException e) {
				LOG.error("Interrupted exception", e);
			}
		}
		manager.updatePercentage();
		LOG.debug("Finishing repaint thread for manager " + manager);
	}

}
