package br.usp.iterador.gui.util;

public class ScaleField {

	private ModelDoubleField fields[];

	public ScaleField(String title) {
		fields = new ModelDoubleField[] { new ModelDoubleField(title + ".min"),
				new ModelDoubleField(title + ".max") };
	}

	public ModelDoubleField[] getFields() {
		return fields;
	}

}
