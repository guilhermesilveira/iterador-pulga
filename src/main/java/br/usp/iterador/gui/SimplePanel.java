package br.usp.iterador.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

import br.usp.iterador.gui.util.PulgaLabel;

/**
 * A simple panel holder
 * 
 * @author Guilherme Silveira
 * @version $Revision$
 */
public class SimplePanel extends JPanel {
	/**
	 * serial UID
	 */
	private static final long serialVersionUID = 3258410616909019187L;

	/**
	 * Simple panel
	 */
	public SimplePanel(String s, String position, Component c) {
		super(new BorderLayout());
		this.add(new PulgaLabel(s), position);
		this.add(c);
	}

	/**
	 * Simple panel
	 */
	public SimplePanel(String s, Component c) {
		this(s, BorderLayout.WEST, c);
	}

	public SimplePanel(JTextField first, JTextField second) {
		super(new GridLayout(1,2));
		this.add(first);
		this.add(second);
	}
}