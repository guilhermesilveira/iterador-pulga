package br.usp.iterador.task;

/**
 * A long task.
 * 
 * @author Guilherme Silveira
 */
public interface LongTask extends Runnable {

	public String getName();

	public double getPercentageComplete();

}
