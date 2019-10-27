package br.usp.iterador.internal.logic;

/**
 * Compile time exception
 * 
 * @author Guilherme Silveira
 * @author $Revision$
 */
public class CompileTimeException extends Exception {

	private static final long serialVersionUID = -4594268248742666091L;

	public CompileTimeException() {
		super();
	}

	public CompileTimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public CompileTimeException(String message) {
		super(message);
	}

	public CompileTimeException(Throwable cause) {
		super(cause);
	}

}
