package br.usp.pulga;

public class Variable {

    private final int id;
    private final String name;
	private double defaultValue;

    public Variable(String name, int id, double defaultValue) {
        this.name = name;
        this.id = id;
		this.defaultValue = defaultValue;
    }

    public double getDefaultValue() {
		return defaultValue;
	}

	public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

	public void setDefaultValue(double value) {
		this.defaultValue = value;
	}

}
