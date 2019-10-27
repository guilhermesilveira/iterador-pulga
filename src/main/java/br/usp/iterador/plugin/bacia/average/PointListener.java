package br.usp.iterador.plugin.bacia.average;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.apache.log4j.Logger;

/**
 * @author Guilherme Silveira
 * 
 */
public class PointListener implements MouseListener {
	
	private static final Logger logger = Logger.getLogger(PointListener.class);

	private Object newAttractorMutex = new Object();

	private Point lastPoint;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		this.lastPoint = new Point(e.getPoint());
		if ((e.getButton() == MouseEvent.BUTTON2)
				|| (e.getButton() == MouseEvent.BUTTON3)) {
			this.lastPoint = null;
		}
		synchronized (newAttractorMutex) {
			newAttractorMutex.notify();
		}
	}

	/**
	 * Locks for the next point
	 * 
	 * @return
	 * 
	 */
	public Point waitForNextPoint() {
		synchronized (newAttractorMutex) {
			try {
				newAttractorMutex.wait();
			} catch (InterruptedException e) {
				logger.error(e);
			}
			return lastPoint;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
	}

}
