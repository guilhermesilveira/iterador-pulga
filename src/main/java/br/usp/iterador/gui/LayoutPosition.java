package br.usp.iterador.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class LayoutPosition {

	private static final Logger logger = Logger.getLogger(LayoutPosition.class);

	private JPanel panel;

	private GridBagConstraints c;

	private int row = 0, col = 0, width = 1, height = 1, cols = 0;

	private double weightx = 0.1, weighty = 0.1;

	public LayoutPosition(JPanel panel) {
		this.panel = panel;
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1, 1, 1, 1);
		c.weighty = 1.0;
	}

	private void add(Component component) {
		logger.debug(String.format(
				"x = %d,y = %d, wx = %f, wy = %f, width = %d, %s", col, row,
				weightx, weighty, width, component.toString()));
		c.gridx = col;
		c.gridy = row;
		c.gridheight = height;
		c.gridwidth = width;
		c.weightx = weightx;
		c.weighty = weighty;
		panel.add(component, c);
		this.cols = Math.max(this.cols, c.gridx + width);
	}

	public void nextLine() {
		row++;
		col = 0;
	}

	public void nextColumn() {
		col += this.width;
	}

	public void add(Component c, double d, double e) {
		// this.width = width;
		// this.height = height;
		setSize(d, e);
		add(c);
	}

	public void setSize(double d, double e) {
		// this.width = width;
		// this.height = height;
		this.weightx = d;
		this.weighty = e;
	}

	public int getCols() {
		return this.cols;
	}

	public void setPlace(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public void setWidth(int w) {
		this.width = w;
	}

}
