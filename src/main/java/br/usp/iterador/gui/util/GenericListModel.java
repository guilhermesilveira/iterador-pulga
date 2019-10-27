package br.usp.iterador.gui.util;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


/**
 * A generic list model for any type of object.
 *
 * @author Guilherme Silveira
 * @since 1.2
 */
public class GenericListModel<T> implements ListModel {

	private final List<T> data;

	private final ListInfo<T> info;

	public GenericListModel(List<T> data, ListInfo<T> info) {
		this.data = data;
		this.info = info;
	}

	public int getSize() {
		return data.size();
	}

	public Object getElementAt(int index) {
		return info.convertToString(data.get(index));
	}

	private ArrayList<ListDataListener> listeners = new ArrayList<ListDataListener>();

	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

	public void updateData() {
		for (ListDataListener l : listeners) {
			l.contentsChanged(new ListDataEvent(this, 0, 0, this.getSize()));
		}
	}
}
