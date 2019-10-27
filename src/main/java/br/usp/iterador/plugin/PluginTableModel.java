package br.usp.iterador.plugin;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import br.usp.iterador.gui.IteratorFrame;

/**
 * A list model for all plugins.
 * 
 * @author Guilherme Silveira
 */
public class PluginTableModel implements TableModel {

	private static final Logger LOG = Logger.getLogger(PluginTableModel.class);

	private final PluginManager pluginManager;

	private final List<TableModelListener> listeners = new ArrayList<TableModelListener>();

	private final IteratorFrame frame;

	public PluginTableModel(PluginManager pluginManager, IteratorFrame frame) {
		this.pluginManager = pluginManager;
		this.frame = frame;
	}

	/**
	 * Returns the number of plugins
	 */
	public int getRowCount() {
		return getPluginManager().getPluginClasses().size();
	}

	/**
	 * @return
	 */
	private PluginManager getPluginManager() {
		return pluginManager;
	}

	public int getColumnCount() {
		return 2;
	}

	public String getColumnName(int columnIndex) {
		if (columnIndex == 0)
			return "active";
		return "plugin";
	}

	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0)
			return Boolean.class;
		return String.class;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 0;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Class<? extends Plugin> clazz = getPluginManager().getPluginClasses()
				.get(rowIndex);
		if (columnIndex == 1) {
			return clazz.getSimpleName();
		}
		return getPluginManager().isActive(clazz);
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Boolean b = (Boolean) aValue;
		Class<? extends Plugin> clazz = getPluginManager().getPluginClasses()
				.get(rowIndex);
		if (b) {
			try {
				getPluginManager().activatePlugin(clazz, null);
			} catch (PluginActivationException e) {
				LOG.error("Unable to activate the plugin", e);
			}
		} else {
			getPluginManager().deactivatePlugin(clazz);
		}
		frame.reorganize();
	}

	public void addTableModelListener(TableModelListener l) {
		this.listeners.add(l);
	}

	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

}
