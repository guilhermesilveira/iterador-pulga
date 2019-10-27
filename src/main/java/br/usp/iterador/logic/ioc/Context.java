package br.usp.iterador.logic.ioc;


/**
 * An attribute context.
 *
 * @author Guilherme Silveira
 * @since 1.2
 */
public interface Context {

	<T> T get(Class<T> type);

	<T> void put(Class<T> type, Object obj);

	<T> void remove(Object o);

	<T> void put(Class<T> type);

    <T> T newInstance(Class<T> clazz);
}
