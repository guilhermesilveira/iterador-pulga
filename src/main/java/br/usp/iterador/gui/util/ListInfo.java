package br.usp.iterador.gui.util;

/**
 * An object to string translator.
 *
 * @author Guilherme Silveira
 * @since 1.2
 *
 * @param <T>
 *            object type
 */
public interface ListInfo<T> {

	String convertToString(T obj);

}
