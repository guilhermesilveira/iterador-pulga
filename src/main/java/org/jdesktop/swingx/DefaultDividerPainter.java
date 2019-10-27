package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.jdesktop.swingx.MultiSplitLayout.Divider;

public class DefaultDividerPainter extends DividerPainter {
	private MultiSplitPane pane;

	public DefaultDividerPainter(MultiSplitPane pane) {
		this.pane = pane;
	}

	public void paint(Graphics g, Divider divider) {
		Graphics2D g2d = (Graphics2D) g;
		if ((divider == pane.activeDivider()) ){//&& !pane.isContinuousLayout()) {
			g2d.setColor(Color.BLACK);
			g2d.fill(divider.getBounds());
		} else {
			g2d.setColor(Color.GRAY);
			g2d.fill(divider.getBounds());
		}
	}
}
