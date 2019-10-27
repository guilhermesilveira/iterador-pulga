package br.usp.iterador.gui.util;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import br.usp.iterador.i18n.Messages;
import br.usp.iterador.logic.Controller;

/**
 * GUI Factory.
 *
 * @author Guilherme Silveira
 */
public class GuiFactory {

	private static final Logger LOG = Logger.getLogger(GuiFactory.class);

	public JPanel getPanel(Component... components) {
		JPanel panel = new JPanel(new GridLayout(1, components.length));
		for (Component b : components) {
			panel.add(b);
		}
		return panel;
	}

	/**
	 * Creates an internationalized label.
	 */
	public JLabel getLabel(String msg) {
		return decorate(new JLabel(Messages.getString(msg)));
	}

	/**
	 * Creates an internationalized label.
	 */
	public JLabel getLabel(String msg, int position) {
		return decorate(new JLabel(Messages.getString(msg), position));
	}

	/**
	 * I18n button.
	 */
	public JButton getButton(String msg) {
		return decorate(new JButton(Messages.getString(msg)));
	}

	public static <T extends JComponent> T decorate(T c) {
		Font f = c.getFont();
		f = f.deriveFont(f.getSize2D() * 0.8f);
		c.setFont(f);
		return c;
	}

	/**
	 * Creates a table with specific table model
	 */
	public JTable createTable(TableModel model) {

		JTable table = new JTable(model);

		// sets the double renderer
		table.setDefaultRenderer(Double.class, new BigDoubleRenderer());

		// selects the first row
		if (model.getRowCount() != 0) {
			table.setRowSelectionInterval(0, 0);
		}

		return decorate(table);

	}

	/**
	 * Returns a new scroll pane
	 *
	 * @param c
	 *            the data
	 * @return the pane
	 */
	public JScrollPane createPane(Component c) {
		JScrollPane pane = new JScrollPane();
		pane.setViewportView(c);
		pane.setPreferredSize(c.getPreferredSize());
		return pane;
	}

	/**
	 * Returns a i18n menu.
	 */
	public JMenu getMenu(String title) {
		return new JMenu(Messages.getString(title));
	}

	/**
	 * Returns a I18N menu with the selected listener.
	 */
	public JMenuItem getMenuItem(String title, ActionListener listener) {
		JMenuItem menu = new JMenuItem(Messages.getString(title));
		menu.addActionListener(listener);
		return menu;
	}

	public JButton getButton(String title, ActionListener listener) {
		JButton button = getButton(title);
		button.addActionListener(listener);
		return button;
	}

	/**
	 * Creates a jbutton based on an image
	 */
	public JButton getButton(Icon icon) {
		return decorate(new JButton(icon));
	}

	/**
	 * Creates a menu item to instantiate a class.
	 */
	public JMenuItem getMenuItem(String title, final Class<?> type,
			final Controller controller) {
		return getMenuItem(title, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.newInstance(type);
			}
		});
	}

	public JMenuItem getMenuItem(String title, String cmd,
			final Controller controller) {
		return getMenuItem(title, getLogicListener(cmd, controller));
	}

	public JPanel createBorderedPanel(String title) {
		JPanel panel = new JPanel();
		LOG.debug("Adding title");
		TitledBorder titleBorder = BorderFactory.createTitledBorder(Messages
				.getString(title));
		panel.setBorder(titleBorder);
		return panel;
	}

	public ActionListener getLogicListener(final String logic,
			final Controller controller) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.executeAction(logic);
			}
		};
	}

	public CloseButton createCloseButton() {
		return decorate(new CloseButton());
	}

}
