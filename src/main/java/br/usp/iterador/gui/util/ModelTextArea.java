package br.usp.iterador.gui.util;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTextArea;

import br.usp.iterador.jxpath.JXPathException;
import br.usp.iterador.jxpath.JXPathResolver;

public class ModelTextArea extends BasicModel {
	private JTextArea component;

	private String fieldName;

	public ModelTextArea() {
		init();
	}

	private void init() {
		this.component.setPreferredSize(new Dimension(400, 200));
	}

	public ModelTextArea(String fieldName) {
		this(fieldName, 3, 40);
	}

	public ModelTextArea(String fieldName, int rows, int cols) {
		this.component = new JTextArea(rows, cols);
		this.fieldName = fieldName;
		init();
	}

	public void updateField(Object o, JXPathResolver resolver)
			throws UpdateException {
		try {
			this.component.setText((String) resolver.resolve(fieldName).get());
		} catch (JXPathException e) {
			throw new UpdateException(e);
		}
	}

	public void updateObject(Object o, JXPathResolver resolver)
			throws UpdateException {
		String value = this.component.getText();
		try {
			resolver.resolve(fieldName).set(value);
		} catch (JXPathException e) {
			throw new UpdateException(e);
		}
	}

	public Component getComponent() {
		return this.component;
	}
	public Component getUpdateComponent() {
		return this.component;
	}
}
