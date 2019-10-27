package br.usp.iterador.model;

/**
 * A piece of code to be executed
 * 
 * @author Guilherme Silveira
 */
public class PieceOfCode implements Comparable<PieceOfCode> {

	private String code = "";

	private int iterations = 100;

	private int level = 1;

	/**
	 * Default constructor
	 */
	public PieceOfCode() {
	}

	/**
	 * @param code
	 * @param iterations
	 * @param level
	 */
	public PieceOfCode(String code, int iterations, int level) {
		this.code = code;
		this.iterations = iterations;
		this.level = level;
	}

	/**
	 * Natural ordering
	 * 
	 * @param other
	 *            object
	 * @return the order
	 */
	public int compareTo(PieceOfCode o) {
		if (this.iterations != o.iterations)
			return this.iterations - o.iterations;
		return new Integer(this.level).compareTo(o.level);
	}

	/**
	 * @return
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return
	 */
	public int getIterations() {
		return iterations;
	}

	/**
	 * @return
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return
	 */
	public boolean isValid() {
		return this.iterations > 0;
	}

	/**
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @param iterations
	 */
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	/**
	 * @param level
	 */
	public void setLevel(int level) {
		this.level = level;
	}
}
