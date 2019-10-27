package br.usp.pulga.iteration;

public class World {

	private final double length;

	public World(double length) {
		this.length = length;

	}

	public double locate(double pos, Axis axis) {
		return (pos - axis.min()) * length / axis.delta();
	}

	public double max() {
		return length;
	}

	public boolean contains(double val) {
		return val >= 0 && val < length;
	}

}
