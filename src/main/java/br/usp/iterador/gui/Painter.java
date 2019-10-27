package br.usp.iterador.gui;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Helps painting
 * 
 * @author Guilherme Silveira
 */
public class Painter {

	public static void paintPoint(int tx, int ty, Graphics2D g, Color color) {
		g.setColor(color);
		g.drawRect(tx, ty, 0, 0);
	}

	public static void paintPoint(int tx, int ty, Graphics2D g, Color color,
			int len) {
		g.setColor(color);
		g.fillRect(tx - len / 2, ty - len / 2, len / 2, len / 2);
	}

}
