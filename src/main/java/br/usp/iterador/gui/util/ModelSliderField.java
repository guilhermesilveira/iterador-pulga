package br.usp.iterador.gui.util;

import java.awt.Component;

import javax.swing.JSlider;

import org.apache.log4j.Logger;

import br.usp.iterador.jxpath.JXPathException;
import br.usp.iterador.jxpath.JXPathResolver;

public class ModelSliderField extends BasicModel {

	private static final Logger logger = Logger.getLogger(ModelIntField.class);

	private static final long serialVersionUID = -1158678999728825060L;

	private static final double DELTA = 50;

	private JSlider component = new JSlider(0, (int) DELTA);

	private String valueField;

	private String minField;

	private String maxField;

	public ModelSliderField(String fieldName) {
		this.valueField = "value";
		this.minField = "min";
		this.maxField = "max";
		component.setPaintTicks(true);
		component.setMinorTickSpacing(2);
		component.setMajorTickSpacing(10);
	}

	public void updateField(Object o, JXPathResolver resolver)
			throws UpdateException {
		try {
			double min = grab(o, resolver, minField);
			double max = grab(o, resolver, maxField);
			double currentValue = grab(o, resolver, valueField);
			int value = (int) ((currentValue - min) * DELTA / (max - min));
			this.component.setValue(value);
		} catch (NumberFormatException e) {
			// ignores it if the value is invalid
		} catch (JXPathException e) {
			throw new UpdateException(e);
		}
	}

	private double grab(Object o, JXPathResolver resolver, String field)
			throws JXPathException {
		double currentValue = Double.parseDouble((String) resolver.resolve(
				field).get());
		logger.debug("Updating slider " + field + " from object " + o
				+ " to real value " + currentValue);
		return currentValue;
	}

	public void updateObject(Object o, JXPathResolver resolver)
			throws UpdateException {

		try {
			double min = grab(o, resolver, minField);
			double max = grab(o, resolver, maxField);
			int sliderValue = this.component.getValue();
			double value = min + sliderValue * (max - min) / DELTA;
			logger.debug("Updating object's field " + this.valueField
					+ " with slider value " + value);
			resolver.resolve(this.valueField).set("" + value);
		} catch (JXPathException e) {
			throw new UpdateException(e);
		}

	}

	public Component getComponent() {
		return component;
	}

}
