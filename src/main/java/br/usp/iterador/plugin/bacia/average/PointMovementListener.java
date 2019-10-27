package br.usp.iterador.plugin.bacia.average;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


/**
 * @author Guilherme Silveira
 * @version $Revision$
 */
public class PointMovementListener implements MouseMotionListener{

	private Point lastPoint;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
		this.lastPoint = new Point(e.getPoint());
	}

	/**
	 * @return Returns the lastPoint.
	 */
	public Point getLastPoint() {
		return lastPoint;
	}

	public void mouseDragged(MouseEvent e) {
	}

}
