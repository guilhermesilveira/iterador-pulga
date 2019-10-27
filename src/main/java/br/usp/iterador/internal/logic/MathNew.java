package br.usp.iterador.internal.logic;

/**
 * @author Guilherme Silveira
 * 
 */
public class MathNew {

	/**
	 * Double mod
	 * 
	 * @param v
	 *            value
	 * @param r
	 *            module
	 * @return v - ((long) (v / r)) * r;
	 */
	public static double modd(double v, double r) {
		return v - (Math.floor(v / r)) * r;
	}

	public double[] soma(double r1, double i1, double r2, double i2) {
		return new double[] { r1 + r2, i1 + i2 };
	}

	public double[] multiply(double r1, double i1, double r2, double i2) {
		double r = r1 * r2, i = i1 * i2;
		i += i2 * r1 + i1 * r2;
		// - ou mais?
		r -= i1 * i2;
		return new double[] { r, i };
	}
}
