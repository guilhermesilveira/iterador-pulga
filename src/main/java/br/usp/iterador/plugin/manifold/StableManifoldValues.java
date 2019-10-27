package br.usp.iterador.plugin.manifold;

public class StableManifoldValues {

	private int total;

	public void reset() {
		total = 0;
	}
	
	public int getTotal() {
		return total;
	}

	public void increase() {
		total++;
	}


}
