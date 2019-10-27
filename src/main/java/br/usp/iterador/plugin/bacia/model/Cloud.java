package br.usp.iterador.plugin.bacia.model;

import java.awt.Color;
import java.util.List;

/**
 * An attractors data for the basin of attraction plugin.
 * 
 * @author Guilherme Silveira
 */
public class Cloud {

	private Color color;

	private String name;

	private MyPolygon polygon;

	private boolean reverse;

	public Cloud(String name, MyPolygon polygon) {
		this.name = name;
		this.polygon = polygon;
		this.color = Color.BLUE;
	}

	/**
	 * Does this cloud contain this point?
	 */
	public boolean contains(double x, double y) {
		boolean c = this.polygon.contains(x, y);
		return (c && !reverse) || (!c && reverse);
	}

	/**
	 * @return Returns the color.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	public List<double[]> getPoints() {
		return this.polygon.getPoints();
	}

	/**
	 * @return Returns the reverse.
	 */
	public boolean isReverse() {
		return reverse;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param reverse
	 *            The reverse to set.
	 */
	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}

	public String toString() {
		return this.name + " " + this.polygon + " " + this.color;
	}

}
