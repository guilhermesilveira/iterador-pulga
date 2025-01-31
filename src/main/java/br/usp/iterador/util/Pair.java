package br.usp.iterador.util;

/**
 * A pair representation
 * @author Guilherme Silveira
 *
 * @param <A>	First type
 * @param <B>	Second type
 */
public class Pair<A, B> {

	private A first;
	private B second;

	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	public A getFirst() {
		return first;
	}

	public B getSecond() {
		return second;
	}
	
	@Override
	public String toString() {
		return "[Pair: " + first + "," + second + "]";
	}
}
