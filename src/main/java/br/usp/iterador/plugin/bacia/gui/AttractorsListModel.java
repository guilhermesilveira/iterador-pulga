package br.usp.iterador.plugin.bacia.gui;

import java.awt.Color;
import java.math.BigInteger;
import java.util.ArrayList;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import br.usp.iterador.plugin.bacia.Basin;
import br.usp.iterador.plugin.bacia.model.Cloud;

/**
 * List model to display the selected attractors.
 * 
 * @author Guilherme Silveira
 */
public class AttractorsListModel implements ListModel {

	private Basin basin;

	private ArrayList<ListDataListener> listeners = new ArrayList<ListDataListener>();

	public AttractorsListModel(Basin basin) {
		super();
		this.basin = basin;
	}

	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	private String color(Color color) {
		return String.format("%s%s%s", duasCasas(new BigInteger(""
				+ color.getRed()).toString(16)), duasCasas(new BigInteger(""
				+ color.getGreen()).toString(16)), duasCasas(new BigInteger(""
				+ color.getBlue()).toString(16)));
	}

	private String duasCasas(String str) {
		return str.length() == 0 ? "00" : (str.length() == 1 ? "0" + str : str);
	}

	/**
	 * Returns the name of the attractor at this position
	 */
	public Object getElementAt(int index) {
		Cloud at = basin.getAttractors().get(index);
		return String.format("<html><font color=\"#%s\">%s</font></html>",
				color(at.getColor()), at.getName());
	}

	/**
	 * Returns the total number of attractors configured until now
	 */
	public int getSize() {
		return basin.getAttractors().size();
	}

	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

	public void updateAttractors() {
		for (ListDataListener l : listeners) {
			l.contentsChanged(new ListDataEvent(this, 0, 0, this.getSize()));
		}
	}
}
