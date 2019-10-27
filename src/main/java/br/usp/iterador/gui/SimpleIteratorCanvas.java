package br.usp.iterador.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import br.usp.iterador.gui.util.PulgaRectangle;
import br.usp.iterador.internal.logic.CompileTimeException;
import br.usp.iterador.internal.logic.Drawer;
import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.ImageInfo;
import br.usp.iterador.plugin.PluginManager;
import br.usp.iterador.plugin.tool.ToolManager;
import br.usp.iterador.plugin.tool.ToolMouseListener;

/**
 * Based on ScrollablePicture.java was used by ScrollDemo.java.
 *
 * @author Guilherme Silveira
 */
@SuppressWarnings("serial")
public class SimpleIteratorCanvas extends JLabel implements Scrollable,
		MouseMotionListener, PulgaCanvas {

	private static Logger logger = Logger.getLogger(SimpleIteratorCanvas.class);

	private IteratorCanvas canvas;

	private int maxUnitIncrement = 10;

	private Application app;

	private List<PulgaRectangle> shapes = new ArrayList<PulgaRectangle>();

	public SimpleIteratorCanvas(IteratorCanvas panel, Application app,
			ToolManager manager) {
		super("Hmmm... something buggy here!!!");
		this.app = app;
		this.canvas = panel;
		// Let the user scroll by dragging to outside the window.
		setAutoscrolls(true); // enable synthetic drag events
		addMouseMotionListener(this); // handle mouse drags
		ToolMouseListener mouseListener = new ToolMouseListener(manager);
		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(mouseListener);
	}

	public void addShape(PulgaRectangle rectangle) {
		this.shapes.add(rectangle);
	}

	public void changeSize(int w, int h) {
		canvas.setPreferredSize(new Dimension(w, h));
	}

	public void clear() {
		ImageInfo info = app.getImage();
		Color color = app.getBackgroundColor();
		logger.debug("Clearing background to " + color);
		ImageHandler.getCurrentImage().getGraphics().setColor(color);
		ImageHandler.getCurrentImage().getGraphics().fillRect(0, 0,
				info.getWidth(), info.getHeight());
		repaint();
	}

	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	@Override
	public Dimension getPreferredSize() {
		return canvas.getPreferredSize();
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		if (orientation == SwingConstants.HORIZONTAL) {
			return visibleRect.width - maxUnitIncrement;
		} else {
			return visibleRect.height - maxUnitIncrement;
		}
	}

	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		// Get the current position.
		int currentPosition = 0;
		if (orientation == SwingConstants.HORIZONTAL) {
			currentPosition = visibleRect.x;
		} else {
			currentPosition = visibleRect.y;
		}

		// Return the number of pixels between currentPosition
		// and the nearest tick mark in the indicated direction.
		if (direction < 0) {
			int newPosition = currentPosition
					- (currentPosition / maxUnitIncrement) * maxUnitIncrement;
			return (newPosition == 0) ? maxUnitIncrement : newPosition;
		} else {
			return ((currentPosition / maxUnitIncrement) + 1)
					* maxUnitIncrement - currentPosition;
		}
	}

	public void mouseDragged(MouseEvent e) {
		// The user is dragging us, so scroll!
		Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
		scrollRectToVisible(r);
	}

	// Methods required by the MouseMotionListener interface:
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void paint(Graphics g) {
		if (!ImageHandler.getCurrentImage().isStillValid(app)) {
			logger.debug("Buffered image is not valid any longer...");
			ImageInfo info = app.getImage();
			ImageHandler.getCurrentImage().setSize(app, info.getWidth(),
					info.getHeight());
		}
		this.canvas.paint(g);
		for (PulgaRectangle r : this.shapes) {
			g.setColor(r.getColor());
			g.drawRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
		}
	}

	/* (non-Javadoc)
	 * @see br.usp.iterador.gui.PulgaCanvas#setMaxUnitIncrement(int)
	 */
	public void setMaxUnitIncrement(int pixels) {
		maxUnitIncrement = pixels;
	}

	/* (non-Javadoc)
	 * @see br.usp.iterador.gui.PulgaCanvas#update(br.usp.iterador.plugin.PluginManager)
	 */
	public void update(PluginManager manager) throws CompileTimeException {
		Graphics2D g = ImageHandler.getCurrentImage().getGraphics();
		ImageInfo info = app.getImage();
		GUIHelper helper = new GUIHelper(info.getWidth(), info.getHeight());
		IterationListener listener = manager.getIterationListener(app,true);
		listener.init(g, helper);
		Drawer d = new Drawer(app, listener, true, info.getWidth(), info
				.getHeight());
		d.init(g);
		d.paint(g);
		repaint();
	}

	/* (non-Javadoc)
	 * @see br.usp.iterador.gui.PulgaCanvas#clearShapes()
	 */
	public void clearShapes() {
		this.shapes.clear();
	}

}
