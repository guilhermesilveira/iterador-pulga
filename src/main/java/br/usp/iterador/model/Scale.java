package br.usp.iterador.model;

/**
 * @author Guilherme Silveira
 */
public class Scale {

	private String field = "x1";

	private double min = 1, max = 0;

	public Scale() {

	}

	public Scale(String field, double min, double max) {
		this.field = field;
		this.min = min;
		this.max = max;
	}

	/**
	 * @return Returns the field.
	 */
	public String getField() {
		return field;
	}

	/**
	 * @return Returns the max.
	 */
	public double getMax() {
		return max;
	}

	/**
	 * @return Returns the min.
	 */
	public double getMin() {
		return min;
	}

	/**
	 * @param field
	 *            The field to set.
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * @param max
	 *            The max to set.
	 */
	public void setMax(double max) {
		this.max = max;
	}

	/**
	 * @param min
	 *            The min to set.
	 */
	public void setMin(double min) {
		this.min = min;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "{scale(" + field + ")" + min + " " + max + "}";
	}

	/**
	 * @param double1
	 * @return
	 */
	public boolean contains(double value) {
		return value >= min && value <= max;
	}

    public double changeValueTo(double value, Scale to) {
        
        return to.min + (to.max - to.min) * ((value - min)/(max-min));
    }
}