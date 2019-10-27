package br.usp.iterador.util;

/**
 * A refleciton exception
 * @author guilherme
 *
 */
public class ReflectionException extends Exception {

	private static final long serialVersionUID = -261487659224835360L;

	public ReflectionException() {
		super();
	}

	public ReflectionException(String message) {
		super(message);
	}

	public ReflectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReflectionException(Throwable cause) {
		super(cause);
	}

}
