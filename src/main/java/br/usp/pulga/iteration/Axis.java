package br.usp.pulga.iteration;

public class Axis {

    private final double min;

    private final double max;

	private final String fieldName;

    public Axis(double min, double max, String fieldName) {
        this.min = min;
        this.max = max;
		this.fieldName = fieldName;
    }

    public String getFieldName() {
		return fieldName;
	}

	public double min() {
        return min;
    }

    public double delta() {
        return max - min;
    }

    public double max() {
        return max;
    }

}
