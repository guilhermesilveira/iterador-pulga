package br.usp.iterador.plugin.setiterator;

import java.awt.Component;

import javax.swing.JTextField;

import br.usp.iterador.gui.util.BasicModel;
import br.usp.iterador.gui.util.UpdateException;
import br.usp.iterador.jxpath.JXPathResolver;

public class CurveField extends BasicModel {

	private JTextField component = new JTextField();

	private int expression;

	private Curve curve;

	public CurveField(Curve curve, int i) {
		this.curve = curve;
		this.expression = i;
	}

	public Component getComponent() {
		return component;
	}

	public void updateField(Object o, JXPathResolver resolver)
			throws UpdateException {
		this.component.setText(curve.getExpression(this.expression));
	}

	public void updateObject(Object o, JXPathResolver resolver)
			throws UpdateException {
		String value = this.component.getText();
		curve.setExpression(this.expression, value);
	}

}
