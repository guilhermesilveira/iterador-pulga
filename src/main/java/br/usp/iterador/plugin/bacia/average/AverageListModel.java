package br.usp.iterador.plugin.bacia.average;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import br.usp.iterador.model.Application;
import br.usp.iterador.plugin.bacia.Basin;

/**
 * Shows all averages displayed until now
 *
 * @author Guilherme Silveira
 */
public class AverageListModel implements ListModel, AverageListener {

	private List<Double[]> averages;

	public AverageListModel(List<Double[]> averages) {
		this.averages = averages;
	}

	public int getSize() {
		return averages.size();
	}

	private DecimalFormat df = new DecimalFormat("###0.######",
			new DecimalFormatSymbols(Locale.ENGLISH));

	public Object getElementAt(int index) {
		Double d[] = averages.get(index);
		return df.format(d[0]) + " , " + df.format(d[1]);
	}

	private ArrayList<ListDataListener> listeners = new ArrayList<ListDataListener>();

	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

	public void updateAverages(Basin b) {
		for (ListDataListener l : listeners) {
			l.contentsChanged(new ListDataEvent(this, 0, 0, this.getSize()));
		}
	}

	public void clear() {
	}

	public double[] getTotal() {
		return null;
	}

	public void initData() {
	}

	public void pinta(double[] x, HashMap<String, Integer> valores,
			Application dados) {
	}

}
