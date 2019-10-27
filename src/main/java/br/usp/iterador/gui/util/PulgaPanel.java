package br.usp.iterador.gui.util;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * Pulga panel with support for action listeners
 * 
 * @author Guilherme Silveira
 */
public class PulgaPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4502625090800320010L;

	private List<ActionListener> listeners = new ArrayList<ActionListener>();

	public PulgaPanel(LayoutManager layout) {
		super(layout);
	}

	public void addActionListener(ActionListener listener) {
		this.listeners.add(listener);
	}

	public void execute(ActionEvent e) {
		for (ActionListener listener : listeners) {
			listener.actionPerformed(e);
		}
	}

}
