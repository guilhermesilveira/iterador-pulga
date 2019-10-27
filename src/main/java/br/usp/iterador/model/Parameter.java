package br.usp.iterador.model;

import br.usp.iterador.gui.ModelType;
import br.usp.iterador.gui.util.ModelSliderField;
import br.usp.iterador.gui.util.ModelTextField;

/**
 * A fixed parameter.
 * 
 * @author Guilherme Silveira
 */
public class Parameter implements Sliderble {

	@ModelType(component = ModelTextField.class)
	private String name, value, min, max;

	public Parameter(String name, String value, String min, String max) {
		this.name = name;
		this.value = value;
		this.min = min;
		this.max = max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getMax() {
		return max;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@ModelType(component = ModelSliderField.class, width= 0.4)
	public SliderData getSlider() {
		return new SliderData(this);
	}

}
