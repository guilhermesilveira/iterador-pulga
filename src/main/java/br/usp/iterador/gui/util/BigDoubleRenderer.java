package br.usp.iterador.gui.util;

import java.awt.Component;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * A double renderer with up to 15 digits precision.
 * 
 * @author Guilherme Silveira
 */
public class BigDoubleRenderer implements TableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		if (!(value instanceof Double)) {
			return table.getDefaultRenderer(value.getClass())
					.getTableCellRendererComponent(table, value, isSelected,
							hasFocus, row, column);
		}

		DecimalFormat formatter = new DecimalFormat("0.####################");
		formatter.setDecimalFormatSymbols(new DecimalFormatSymbols(
				Locale.ENGLISH));
		value = formatter.format(((Double) value).doubleValue());

		return table.getDefaultRenderer(Double.class)
				.getTableCellRendererComponent(table, value, isSelected,
						hasFocus, row, column);
	}

}
