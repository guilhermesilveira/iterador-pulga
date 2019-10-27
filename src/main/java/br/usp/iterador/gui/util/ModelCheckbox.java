package br.usp.iterador.gui.util;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JCheckBox;

import br.usp.iterador.i18n.Messages;
import br.usp.iterador.jxpath.JXPathException;
import br.usp.iterador.jxpath.JXPathResolver;

/**
 * A checkbox.
 * 
 * @author Guilherme Silveira
 * 
 */
public class ModelCheckbox extends BasicModel {

	private JCheckBox component = new JCheckBox();

	private String fieldName;

	/**
	 * Constructor
	 * 
	 */
	public ModelCheckbox() {
		init();
	}

	/**
	 * Constructor
	 * 
	 * @param fieldName
	 *            field name
	 */
	public ModelCheckbox(String fieldName) {
		init();
		this.fieldName = fieldName;
	}

	public ModelCheckbox(String title, String field) {
		this(field);
		this.component.setText(Messages.getString(title));
	}

	private void init() {
		this.component.setPreferredSize(new Dimension(100, 20));
	}

	public void updateField(Object o, JXPathResolver resolver)
			throws UpdateException {
		try {
			this.component.setSelected((Boolean)resolver.resolve(this.fieldName).get());
		} catch (JXPathException e) {
			throw new UpdateException(e);
		}
	}

	public void updateObject(Object o, JXPathResolver resolver)
			throws UpdateException {
		boolean value = this.component.isSelected();
		try {
			resolver.resolve(this.fieldName).set(value);
		} catch (JXPathException e) {
			throw new UpdateException(e);
		}
	}

	public Component getComponent() {
		return component;
	}

}
