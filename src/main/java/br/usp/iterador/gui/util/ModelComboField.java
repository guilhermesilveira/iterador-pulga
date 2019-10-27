package br.usp.iterador.gui.util;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JComboBox;

import br.usp.iterador.gui.tablemodel.ScaleModel;
import br.usp.iterador.jxpath.JXPathResolver;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.Scale;

/**
 * Combo box.
 * 
 * @author Guilherme Silveira
 */
public class ModelComboField extends BasicModel {

	private JComboBox component;

	private Scale scale;

	public ModelComboField(Application app, Scale scale) {
		this.component = new JComboBox(new ScaleModel(scale, app));
		this.component.setPreferredSize(new Dimension(100, 20));
		this.scale = scale;
	}

	public void updateField(Object o, JXPathResolver resolver)
			throws UpdateException {
		this.component.setSelectedItem(this.scale.getField());
	}

	public void updateObject(Object o, JXPathResolver resolver)
			throws UpdateException {
		this.scale.setField(this.component.getSelectedItem().toString());
	}

	public Component getComponent() {
		return component;
	}

	public Component getUpdateComponent() {
		return this.component;
	}

}
