package br.usp.iterador.gui.util;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTextField;

import br.usp.iterador.jxpath.JXPathException;
import br.usp.iterador.jxpath.JXPathResolver;

/**
 * A double field
 * 
 * @author Guilherme Silveira
 * 
 */
public class ModelDoubleField extends BasicModel implements ModelField {

	private JTextField component = GuiFactory.decorate(new JTextField(5));

	private String fieldName;

	/**
	 * Constructor
	 * 
	 */
	public ModelDoubleField() {
		init();
	}

	/**
	 * Constructor
	 * 
	 * @param fieldName
	 *            field name
	 */
	public ModelDoubleField(String fieldName) {
		init();
		this.fieldName = fieldName;
	}

	private void init() {
		this.component.setPreferredSize(new Dimension(100, 20));
	}

	public void updateField(Object o, JXPathResolver resolver)
			throws UpdateException {
		try {
			this.component.setText("" + resolver.resolve(this.fieldName).get());
		} catch (JXPathException e) {
			throw new UpdateException(e);
		}
	}

	public void updateObject(Object o, JXPathResolver resolver)
			throws UpdateException {
		double value = Double.parseDouble(this.component.getText());
		try {
			resolver.resolve(this.fieldName).set(value);
		} catch (JXPathException e) {
			throw new UpdateException(e);
		}
	}

	public JTextField getComponent() {
		return component;
	}
	public Component getUpdateComponent() {
		return this.component;
	}
}
