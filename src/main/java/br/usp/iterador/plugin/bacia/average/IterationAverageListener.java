package br.usp.iterador.plugin.bacia.average;

/**
 * Data storage.
 * 
 * @author Guilherme Silveira
 */
public class IterationAverageListener {

	private double total[] = new double[] { 0, 0 };

	private double temp[] = new double[] { 0, 0 };

	private int runCount = 0;

	/**
	 * Adds some value to the iteration average data
	 * 
	 * @param a
	 *            first average
	 * @param b
	 *            second average
	 */
	public void add(double a, double b) {
		total[0] += a;
		total[1] += b;
		runCount++;
	}

	public double[] getTotal() {
		return this.total;
	}

	public double[] getAverage() {
		temp[0] = total[0] / runCount;
		temp[1] = total[1] / runCount;
		return temp;
	}

	public void reset() {
		this.total[0] = this.total[1] = 0;
		runCount = 0;
	}

}
