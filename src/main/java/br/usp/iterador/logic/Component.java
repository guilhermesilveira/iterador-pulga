package br.usp.iterador.logic;

/**
 * Defines a pulga component.
 *
 * @author Guilherme Silveira
 * @since 1.2
 */
public @interface Component {

	/**
	 * Whether it should be created right away.
	 * @return	true or false
	 */
	boolean autoCreate() default false;

}
