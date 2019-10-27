package br.usp.iterador.logic;

/**
 * Default system controller.
 *
 * @author Guilherme Silveira
 * @since 1.2
 */
public interface Controller {

	/**
	 * Executes a logic
	 *
	 * @param cmd
	 *            command name
	 */
	void executeAction(String cmd);

	/**
	 * Finds an instance of this type.
	 * @param <T>	the type
	 * @param type	the type
	 * @return	the instance
	 */
	<T> T find(Class<T> type);

	/**
	 * Instantiates using dependency injection.
	 */
	<T> T newInstance(Class<T> clazz);

	/**
	 * Registers a logic.
	 */
	void registerAction(String key, Class<? extends PulgaAction> action);

	void registerDI(Object value);

	<T> void registerDI(Class<T> type);

	/**
	 * Unregisters a logic.
	 */
	void unregisterAction(String key);

	void unregisterIOC(Object o);

}