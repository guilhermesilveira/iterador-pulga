package br.usp.iterador.plugin.setiterator;

import java.util.ArrayList;
import java.util.List;

/**
 * Set list
 * 
 * @author Guilherme Silveira
 */
public class CurveSet {

	private List<Curve> curves = new ArrayList<Curve>();

	/**
	 * Returns a list of curves
	 * 
	 * @return the list
	 */
	public List<Curve> getCurves() {
		return new ArrayList<Curve>(curves);
	}

	/**
	 * Adds a new curve
	 * 
	 * @param curve
	 *            the new curve
	 */
	public void addCurve(Curve curve) {
		this.curves.add(curve);
	}

	/**
	 * Removes a curve
	 * 
	 * @param i
	 *            the curve
	 */
	public void removeCurve(int i) {
		this.curves.remove(i);
	}

}
