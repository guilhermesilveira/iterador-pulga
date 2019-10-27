package br.usp.iterador.gui.tablemodel;

import java.util.ArrayList;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

import br.usp.iterador.model.Application;
import br.usp.iterador.model.Scale;

/**
 * Scale model for the frame.
 * 
 * @author Guilherme Silveira
 */
public class ScaleModel implements ComboBoxModel {

	private ArrayList<ListDataListener> listeners = new ArrayList<ListDataListener>();

	private Scale scale;

	private Object selected;

	private Application app;

	public ScaleModel(Scale scale, Application app) {
		this.scale = scale;
		this.app = app;
		selected = this.scale.getField();
	}

	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	public Object getElementAt(int index) {
		return app.getAvailableVariables().get(index);
	}

	public Object getSelectedItem() {
		return selected;
	}

	public int getSize() {
		return app.getAvailableVariables().size();
	}

	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

	public void setSelectedItem(Object anItem) {
		selected = anItem;
	}

	public void commit(double min, double max) {
		this.scale.setField(this.selected.toString());
		this.scale.setMax(max);
		this.scale.setMin(min);
	}

}
