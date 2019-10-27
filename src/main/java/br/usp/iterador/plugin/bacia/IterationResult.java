package br.usp.iterador.plugin.bacia;

/**
 * Results of an iteration
 * 
 * @author Guilherme Silveira
 * 
 */
public class IterationResult {

	private double average[];

	private int iterations;

	public IterationResult(double[] average, int iterations) {
		this.average = average;
		this.iterations = iterations;
	}

	public double[] getAverages() {
		return average;
	}

	public int getIterations() {
		return iterations;
	}

}
