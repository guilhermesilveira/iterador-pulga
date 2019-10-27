package br.usp.iterador.plugin.tool;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


/**
 * Listens to mouse click on the canvas.
 *
 * @author Guilherme Silveira
 */
public class ToolMouseListener implements MouseListener, MouseMotionListener {

	private final ToolManager manager;

	public ToolMouseListener(ToolManager manager) {
		this.manager = manager;
	}

	/**
	 * Mouse clicked.
	 */
	public void mouseClicked(MouseEvent e) {
		Tool tool = manager.getCurrentTool();
		if (tool != null) {
			tool.onClick((int) e.getPoint().getX(), (int) e.getPoint().getY());
		}
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
	}

	/**
	 * Mouse has moved.
	 *
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
		Tool tool = manager.getCurrentTool();
		if (tool != null) {
			tool.mouseMoved((int) e.getPoint().getX(), (int) e.getPoint()
					.getY());
		}
	}

}
