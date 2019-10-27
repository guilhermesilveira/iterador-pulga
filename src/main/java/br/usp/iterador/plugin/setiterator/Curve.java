package br.usp.iterador.plugin.setiterator;

import java.util.ArrayList;
import java.util.List;

/**
 * A parameterized curve in Rn
 * 
 * @author Guilherme Silveira
 */
public class Curve {

	private List<String> expressions = new ArrayList<String>();
    
    private int points;

	public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    /**
	 * Return an expression
	 * 
	 * @param i
	 *            i
	 * @return expression
	 */
	public String getExpression(int i) {
		updateDimension(i);
		return expressions.get(i);
	}

	/**
	 * Sets an expression
	 * 
	 * @param i
	 *            which one
	 * @param value
	 *            new value
	 */
	public void setExpression(int i, String value) {
		updateDimension(i);
		this.expressions.set(i, value);
	}

	/**
	 * @param i
	 */
	private void updateDimension(int i) {
		while (this.expressions.size() <= i) {
			this.expressions.add("x" + (i + 1));
		}
	}

}
