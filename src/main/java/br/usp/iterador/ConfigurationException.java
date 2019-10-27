package br.usp.iterador;

/**
 * Any type of config exception that the user has done: user problem are runtime exceptions
 * 
 * @author Guilherme Silveira
 */
public class ConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 1040007128343123625L;

	public ConfigurationException() {
		super();
	}

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(Throwable cause) {
		super(cause);
	}

}
