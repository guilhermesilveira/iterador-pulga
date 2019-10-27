package br.usp.iterador.gui.util;

import java.awt.Component;

import br.usp.iterador.jxpath.JXPathResolver;

public interface ModelField {

	/**
	 * Updates the component with the object's value.
	 */
	public void updateField(Object o, JXPathResolver resolver)
			throws UpdateException;

	/**
	 * The awt component.
	 * 
	 * @return the component
	 */
	public Component getComponent();

	/**
	 * Updates the object with its internal value.
	 */
	public void updateObject(Object obj, JXPathResolver resolver)
			throws UpdateException;

	void setSize(double d, double e);

	double getWidth();

	double getHeight();

}
