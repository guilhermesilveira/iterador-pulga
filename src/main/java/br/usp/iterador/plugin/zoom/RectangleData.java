package br.usp.iterador.plugin.zoom;

/**
 * Zoom data.
 * 
 * @author Guilherme Silveira
 */
public class RectangleData {

	private final double[][] coordinates = new double[2][2];

	private int numberOfPoints = 0;

	private int[] origin;

	public void reset() {
		this.numberOfPoints = 0;
	}

	/**
	 * Adds a point to the zoom.
	 */
	public void addPoint(double x, double y) {
		this.coordinates[this.numberOfPoints][0] = x;
		this.coordinates[this.numberOfPoints][1] = y;
		this.numberOfPoints++;
	}

	/**
	 * Does it contain 2 points?
	 */
	public boolean isDone() {
		return this.numberOfPoints == 2;
	}

	/**
	 * Gets the coordinates
	 * 
	 * @return Returns the coordinates.
	 */
	public double[][] getCoordinates() {
		return coordinates;
	}

	public void setOrigin(int pointX, int pointY) {
		this.origin = new int[] { pointX, pointY };
	}

	public int getNumberOfPoints() {
		return numberOfPoints;
	}

	public int[] getOrigin() {
		return origin;
	}

}
