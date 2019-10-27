package br.usp.iterador.gui.tablemodel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import br.usp.iterador.i18n.Messages;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.PieceOfCode;

/**
 * Valores iniciais.
 * 
 * @author Guilherme Silveira
 */
public class PieceOfCodeModel implements TableModel {

	private List<PieceOfCode> codes;

	public PieceOfCodeModel(Application app) {
		this.codes = app.getCodes().getCodes();
	}

	private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();

	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}

	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 2)
			return String.class;
		return Integer.class;
	}

	public int getColumnCount() {
		return 3;
	}

	public String getColumnName(int columnIndex) {
		if (columnIndex == 0)
			return Messages.getString("level");
		return (columnIndex == 1) ? Messages.getString("iterations") : Messages
				.getString("value");
	}

	public int getRowCount() {
		return codes.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return codes.get(rowIndex).getLevel();
		}
		if (columnIndex == 1) {
			return codes.get(rowIndex).getIterations();
		}
		return codes.get(rowIndex).getCode();
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	public void update() {
		for (TableModelListener l : listeners) {
			l.tableChanged(new TableModelEvent(this));
		}
	}

}
