package br.usp.iterador.plugin;

/**
 * Plugin activation and deactivation exception
 * 
 * @author Guilherme Silveira
 */
public class PluginActivationException extends Exception {

	/**
	 * Serial uid
	 */
	private static final long serialVersionUID = 10000L;

	/**
	 * 
	 */
	public PluginActivationException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PluginActivationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public PluginActivationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public PluginActivationException(Throwable cause) {
		super(cause);
	}

}
