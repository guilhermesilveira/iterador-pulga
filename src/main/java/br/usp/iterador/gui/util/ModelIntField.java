package br.usp.iterador.gui.util;

import java.awt.Component;

import javax.swing.JTextField;

import org.apache.log4j.Logger;

import br.usp.iterador.jxpath.JXPathException;
import br.usp.iterador.jxpath.JXPathResolver;

/**
 * int field editor
 * 
 * @author Guilherme Silveira
 */
public class ModelIntField extends BasicModel {

	private static final Logger logger = Logger.getLogger(ModelIntField.class);

	private static final long serialVersionUID = -1158678999728825060L;

	private JTextField component = GuiFactory.decorate(new JTextField(5));

	private String fieldName;

	public ModelIntField() {
		init();
	}

	public ModelIntField(String fieldName) {
		init();
		this.fieldName = fieldName;
	}

	private void init() {
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
		int value = Integer.parseInt(this.component.getText());
		logger.debug("Updating " + this.fieldName + " with value " + value);
		try {
			resolver.resolve(this.fieldName).set(value);
		} catch (JXPathException e) {
			throw new UpdateException(e);
		}
	}

	public Component getComponent() {
		return component;
	}

	public Component getUpdateComponent() {
		return this.component;
	}
}
